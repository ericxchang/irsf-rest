package com.iconectiv.irsf.portal.service.impl;

import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.core.*;
import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.common.*;
import com.iconectiv.irsf.portal.model.customer.*;
import com.iconectiv.irsf.portal.repositories.common.CustomerDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.common.EventNotificationRepository;
import com.iconectiv.irsf.portal.repositories.customer.*;
import com.iconectiv.irsf.portal.service.*;
import com.iconectiv.irsf.util.DateTimeHelper;
import io.jsonwebtoken.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class PartitionServiceImpl implements PartitionService {
	private static Logger log = LoggerFactory.getLogger(PartitionServiceImpl.class);

	@Autowired
	private PartitionDefinitionRepository partitionDefRepo;
	@Autowired
	private PartitionExportHistoryRepository exportRepo;
	@Autowired
	private PartitionDataDetailsRepository partitionDataRepo;
	@Autowired
	private RuleDefinitionRepository ruleRepo;
	@Autowired
	private AuditTrailService auditService;
	@Autowired
	private MobileIdDataService mobileIdService;
	@Autowired
	private ListDefinitionRepository listDefinitionRepo;
	@Autowired
	private EventNotificationService eventService;
	@Autowired
	private EventNotificationRepository eventRepo;
	@Autowired
	private CustomerDefinitionRepository customerRepo;
	@Autowired
	private	ListService listService;
	@Autowired
	@Lazy
	private PartitionExportService exportService;

	@Async
	@Override
	public void refreshPartition(UserDefinition loginUser, Integer partitionId) {
        CustomerContextHolder.setSchema(loginUser.getSchemaName());

        PartitionDefinition partition = partitionDefRepo.findOne(partitionId);

		if (partition == null) {
			log.error("Invalid partition Id {}", partitionId);
			return;
		}
		
		syncRefreshPartition(loginUser, partition);
	}

	private PartitionDefinition syncRefreshPartition(UserDefinition loginUser, PartitionDefinition partition) {
		Assert.notNull(partition);
		
		CustomerContextHolder.setSchema(loginUser.getSchemaName());
		String prevStatus = partition.getStatus();
        try {
			validateParitionStatus(partition);

			partition.setStatus(PartitionStatus.InProgress.value());
			partitionDefRepo.save(partition);

			refreshParitionData(loginUser, partition);
		} catch (AppException e) {
			log.error("Error to refresh partition:", e);
			partition.setStatus(prevStatus);
			partitionDefRepo.save(partition);
		}
		return partition;
	}

    @Override
	public void refreshParitionData(UserDefinition loginUser, PartitionDefinition partition) throws AppException {
		Assert.notNull(partition);

		List<PartitionDataDetails> partitionDataList = generateDraftData(partition);
        persistDraftData(loginUser, partition, partitionDataList);
	}

	/*
	 * To export partition: 1. copy data to parition_export_history data and
	 * move the status of current partition to "Locked" 2. clear data from
	 * partition_data_details table 3. clone a new partition with status of
	 * "fresh" and underlying rule(s) 4. create event to send export data to EI
	 * system
	 */
	@Override
	@Async
	public void exportPartition(UserDefinition loginUser, Integer partitionId) throws AppException {
		CustomerContextHolder.setSchema(loginUser.getSchemaName());
		PartitionDefinition partition = partitionDefRepo.findOne(partitionId);

		if (partition == null) {
			throw new AppException("Invalid partition Id");
		}
		String prevStatus = partition.getStatus();
		
		try {
			log.info("exportPartition: partitionId: {}, status: {}", partitionId, partition.getStatus());

			boolean refresh = validateParitionExportStatus(partition);
			if (refresh) {
				partition = syncRefreshPartition(loginUser, partition); 
			}

			PartitionExportHistory partHist = exportPartitionData(loginUser, partition);

			sendPartitionEvent(loginUser, partition.getId(), EventTypeDefinition.Partition_Export.value(), "export data are ready for partition " + partition.getName());

			CustomerDefinition customer = customerRepo.findByCustomerName(loginUser.getCustomerName());
			if (customer.getExportTarget() != null) {
				log.info("exportPartition: calling sendExportFile2EI(): partitionId: {}, status: {}", partitionId, partition.getStatus());
				exportService.sendExportFile2EI(loginUser, partHist, customer.getExportTarget());
			}

		} catch (AppException e) {
			log.error("Error to export partition:", e);
			partition.setStatus(prevStatus);
			partitionDefRepo.save(partition);
		}

	}

	@Override
	public void sendPartitionEvent(UserDefinition loginUser, Integer partitionId, String type, String message) {
		EventNotification event = new EventNotification();
		event.setCreateTimestamp(DateTimeHelper.nowInUTC());
		event.setEventType(type);
		event.setReferenceId(partitionId);
		event.setCustomerName(loginUser.getCustomerName());
		event.setStatus("new");
		event.setMessage(message);
		eventRepo.save(event);
		eventService.broadcastPartitionEvent(loginUser.getCustomerId(), event);	
	}


	@Override
	@Async
	public void resendPartition(UserDefinition loginUser, Integer exportPartitionId) {
		CustomerDefinition customer = customerRepo.findByCustomerName(loginUser.getCustomerName());
		log.info("exportPartition: exportPartitionId: {}", exportPartitionId);
		PartitionExportHistory partHist = exportRepo.findOne(exportPartitionId);

		if (customer.getExportTarget() != null) {
			log.info("exportPartition: calling sendExportFile2EI(): partitionId: {}", partHist.getPartitionId());
			exportService.sendExportFile2EI(loginUser, partHist, customer.getExportTarget());
		}

	}


	private void validateParitionStatus(PartitionDefinition partition) throws AppException {
		Assert.notNull(partition);

		if (partition.getStatus().equals(PartitionStatus.InProgress.value())) {
			throw new AppException("System is generating partition data set");
		}

		if (partition.getStatus().equals(PartitionStatus.Exported.value())) {
			throw new AppException("Partition has been exported");
		}

	}

	private boolean validateParitionExportStatus(PartitionDefinition partition) throws AppException {
		boolean refreshFlag = false;
		Assert.notNull(partition);

		if (partition.getStatus().equals(PartitionStatus.InProgress.value())) {
			throw new AppException("System is exporting partition data set");
		}
		if (partition.getStatus().equals(PartitionStatus.Exported.value())) {
			throw new AppException("Partition has been exported");
		}
		if (partition.getStatus().equals(PartitionStatus.Stale.value())) {
			log.info("partition data status is stale, refresh it");
			refreshFlag = true;
		}
		if (partition.getStatus().equals(PartitionStatus.Fresh.value())) {
			log.info("partition data status is fresh, refresh it");
			refreshFlag = true;
		}

		return refreshFlag;
	}

	@Transactional
	private PartitionExportHistory exportPartitionData(UserDefinition loginUser, PartitionDefinition partition) throws AppException {
		Assert.notNull(partition);

		List<String> wlDataType = new ArrayList<>();
		List<String> nonWLDataType = new ArrayList<>();
		wlDataType.add(PartitionDataType.WhiteList.value());
		nonWLDataType.add(PartitionDataType.BlackList.value());
		nonWLDataType.add(PartitionDataType.Rule.value());

		PartitionExportHistory partHist = new PartitionExportHistory();

		String prevStatus = partition.getStatus();
		try {
			log.debug("exportPartitionData(): partitionId: {}; status: {}", partition.getId(), partition.getStatus());
			partition.setStatus(PartitionStatus.InProgress.value());

			log.debug("exportPartitionData(): partitionId: {}; set status to:  {}", partition.getId(), partition.getStatus());
			partitionDefRepo.save(partition);

			EventNotification event = eventRepo.findTop1ByCustomerNameAndEventTypeOrderByCreateTimestampDesc(
					AppConstants.IRSF_DATA_LOADER_CUSTOMER_NAME, AppConstants.IRSF_DATA_LOADER_EVENT_TYPE);

			List<PartitionDataDetails> partitionDataListLong = partitionDataRepo.findAllByPartitionId(partition.getId());
			List<String> partitionDataListShort = partitionDataRepo.findDistinctDialPatternByPrtitionId(partition.getId(), nonWLDataType);
			List<String> whiteList = partitionDataRepo.findDistinctDialPatternByPrtitionId(partition.getId(),wlDataType);

			partHist.setExportFileLong(buildPartitionDataLong(partitionDataListLong));
			partHist.setExportFileShort(StringUtils.collectionToDelimitedString(partitionDataListShort, "\n").getBytes());
			partHist.setExportWhitelist(StringUtils.collectionToDelimitedString(whiteList, "\n").getBytes());

			partHist.setExportFileLongSize(partitionDataListLong.size());
			partHist.setExportFileShortSize(partitionDataListShort.size());
			partHist.setExportWhitelistSize(whiteList.size());

			log.info(
					"exportPartitionData(): partitionId: {}, size of exportFileLong: {}, size of exportFileShort: {}, size of exportWhitelist: {}",
					partition.getId(), partHist.getExportFileLongSize(), partHist.getExportFileShortSize(),
					partHist.getExportWhitelistSize());

			partHist.setExportDate(DateTimeHelper.nowInUTC());
			partHist.setOrigPartitionId(partition.getOrigPartitionId());
			partHist.setPartitionId(partition.getId());
			partHist.setStatus(PartitionExportStatus.Success.value());
			partHist.setReason(AuditTrailActionDefinition.Export_Partition_Data);
			partHist.setMidDataLoadTime(event.getCreateTimestamp());

			log.debug("exportPartitionData(): save PartitionExportHistory");
			partHist = exportRepo.save(partHist);

			partition.setStatus(PartitionStatus.Exported.value());
			partition.setLastExportDate(DateTimeHelper.nowInUTC());
			partition.setLastUpdatedBy(loginUser.getUserName());

			log.debug("exportPartitionData(): update  PartitionDefinition");
			partitionDefRepo.save(partition);


			log.debug("exportPartitionData(): clone partitionDefinition");
			PartitionDefinition newPartition = clonePartition(loginUser, partition);

			log.debug("exportPartitionData():: move PartitionDataDetails: old: {}; new: {} ", partition.getId(), newPartition.getId());

			partitionDataRepo.movePartition(partition.getId(), newPartition.getId());

			auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Export_Partition_Data, "export partition data set " + partition.getId());
		} catch (Exception e) {
			log.error("Error on export partition data", e);

			partition.setStatus(prevStatus);
			log.debug("exportPartitionData(): partitionId: {}; reset status back to:  {}", partition.getId(), prevStatus);
			partitionDefRepo.save(partition);
			throw new AppException(e);
		}

		return partHist;
	}

	private byte[] buildPartitionDataLong(List<PartitionDataDetails> list) {
		log.info("buildPartitionDataLong(): number of rows: {}", list.size());
		StringBuilder sb = new StringBuilder();
		for (PartitionDataDetails p : list) {
			sb.append(p.toCSVheader(AppConstants.CSV_COMMON_SEPERATOR));
			sb.append("\n");
		}
		list.clear();
		return sb.toString().getBytes();

	}

	/*
	    1. delete existing data;
	    2. persist partition data to data table;
	    3. update partition status to draft
	 */
    @Transactional
	private void persistDraftData(UserDefinition loginUser, PartitionDefinition partition, List<PartitionDataDetails> partitionDataList) {
        log.debug("generateDraftData: delete all partition data for partitionId: {}", partition.getId());
        partitionDataRepo.deleteByPartitionId(partition.getId());
        partitionDataRepo.batchUpdate(partitionDataList);
        partition.setStatus(PartitionStatus.Draft.value());
        partition.setDraftDate(DateTimeHelper.nowInUTC());
        partition.setLastUpdated(DateTimeHelper.nowInUTC());
        partition.setLastUpdatedBy(loginUser.getUserName());
        partitionDefRepo.save(partition);

        sendPartitionEvent(loginUser, partition.getId(), EventTypeDefinition.Partition_Draft.value(), "draft data are ready for partition " + partition.getName());

        auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Refresh_Partition_Data, "generated partition draft data set:" + partition.getId());
    }


	private List<PartitionDataDetails> generateDraftData(final PartitionDefinition partition) {
        List<PartitionDataDetails> partitionDataList = new ArrayList<>();

		List<RuleDefinition> rules = partition.getRuleDefinitions();
		if (rules == null || rules.isEmpty()) {
			rules = ruleRepo.findAllByPartitionId(partition.getId());
		}

		rules.parallelStream().forEach(rule -> {
            generatePartitionDataFromRule(partition, rule, partitionDataList);
        });

        log.info("Generating {} partition data from rule", partitionDataList.size());

		if (partition.getWlId() != null) {
			generateListData(partition, partition.getWlId(), partitionDataList);
		}

		if (partition.getBlId() != null) {
            generateListData(partition, partition.getBlId(), partitionDataList);
		}

        log.info("Completed generating partition data {}", partitionDataList.size());
		return partitionDataList;
	}

	//TODO use pageination
	private void generateListData(PartitionDefinition partition, Integer listId, final List<PartitionDataDetails> partitionDataList) {
		ListDefinition listDef = listDefinitionRepo.findOne(listId);

		List<ListDetails> listData = listService.getListDetailDataByListId(listId);

		listData.stream().forEach(listDetail -> {
		    if (listDetail.isActive()) {
                partitionDataList.add(listDetail.toPartitionDataDetails(partition, listDef));
            }
        });

		return;
	}

    //TODO use pageination
	private void generatePartitionDataFromRule(PartitionDefinition partition, RuleDefinition rule, final List<PartitionDataDetails> partitionDataList) {
		RangeQueryFilter filter;

        if (!rule.isActive()) {
            log.info("skip inactive rule, rule_id: {}", rule.getId());
            return;
        }

        try {
			filter = rule.getRangeQueryFilter();
		} catch (AppException e) {
			log.error("can't parse RangeQueryFilter - {}, skip ruleId: {}, details: {}", e.getMessage(), rule.getId(), rule.getDetails());
			return;
		}

		log.debug("retrieve all partition data for ruleId: {}", rule.getId());

		if (AppConstants.RANGE_NDC_TYPE.equals(rule.getDataSource())) {
			List<RangeNdc> dataList = mobileIdService.findAllRangeNdcByFilters(filter);
			dataList.stream().forEach(entry -> {
                partitionDataList.add(entry.toPartitionDataDetails(partition, rule));
            });
		} else if (AppConstants.PREMIUM_RANGE_TYPE.equals(rule.getDataSource())) {
			List<Premium> dataList = mobileIdService.findAllPremiumRangeByFilters(filter);
            dataList.stream().forEach(entry -> {
                partitionDataList.add(entry.toPartitionDataDetails(partition, rule));
            });
		} else {
			log.error("Unknown data source: {}, rule_id: {}", rule.getDataSource(), rule.getId());
		}

		return;
	}

	private PartitionDefinition clonePartition(UserDefinition loginUser, PartitionDefinition partition) {
		if (partition.getOrigPartitionId() == null) {
			partition.setOrigPartitionId(partition.getId());
		}
		partition.setId(null);
		partition.setStatus(PartitionStatus.Fresh.value());
		partition.setLastUpdated(DateTimeHelper.nowInUTC());
		partition.setLastExportDate(null);
		partition.setDraftDate(null);
		partition.setLastUpdatedBy("cloned");
		partitionDefRepo.save(partition);

		List<String> ruleIds = cloneRules(loginUser, partition);

		partition.setRuleIds(String.join(",", ruleIds));
		partitionDefRepo.save(partition);
		auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Clone_Partition, "clone new partition " + partition.getId());

		return partition;
	}

	private List<String> cloneRules(UserDefinition loginUser, final PartitionDefinition partition) {
		List<String> ruleIds = new ArrayList<>();
		for (String ruleId : partition.getRuleIds().split(",")) {
			RuleDefinition rule = ruleRepo.findOne(Integer.valueOf(ruleId));
			Assert.notNull(rule);

			// mark the old one inActive
			rule.setActive(false);
			ruleRepo.save(rule);

			// create a new rule
			rule.setId(null);
			rule.setLastUpdated(DateTimeHelper.nowInUTC());
			rule.setCreatedBy("cloned");
			rule.setPartitionId(partition.getId());
			rule.setActive(true);
			ruleRepo.save(rule);

			ruleIds.add(rule.getId().toString());
			auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Clone_Rule,"clone new rule " + rule.getId());

			if (log.isDebugEnabled())
				log.debug("Clone new rule {}", rule.getId());
		}
		return ruleIds;
	}

	@Transactional
	@Override
	public void addRule(UserDefinition loginUser, PartitionDefinition partition, RuleDefinition rule) throws AppException {
		try {
			Assert.notNull(partition);
			Assert.notNull(rule);

			if (rule.getPartitionId() != null && !rule.getPartitionId().equals(partition.getId())) {
				throw new AppException("rule " + rule.getId() + " has assigned to different partition.");
			}

			Set<String> ruleIds = new HashSet<>();

			if (partition.getRuleIds() != null) {
				Collections.addAll(ruleIds, partition.getRuleIds().split(","));
			}
			ruleIds.add(rule.getId().toString());

			updateRuleId(partition, ruleIds, loginUser.getUserName());
			auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Add_Rule_To_Partition,
					"append rule " + rule.getId() + " to partition " + partition.getId());

			checkStale(loginUser, partition, "new rule is added");
		} catch (Exception e) {
			log.error("Fail to add rule to parition: ", e);
			throw new AppException(e);
		}
	}

	@Transactional
	@Override
	public void removeRule(UserDefinition loginUser, PartitionDefinition partition, Integer ruleId)
			throws AppException {
		try {
			Assert.notNull(partition);
			Assert.notNull(ruleId);

			Set<String> ruleIds = new HashSet<>();
			Collections.addAll(ruleIds, partition.getRuleIds().split(","));

			if (ruleIds.contains(ruleId.toString())) {
				ruleIds.remove(ruleId.toString());
			}

			updateRuleId(partition, ruleIds, loginUser.getUserName());

			// mark rule as inactive
			RuleDefinition rule = ruleRepo.findOne(ruleId);
			if (rule != null) {
				rule.setActive(false);
				rule.setPartitionId(null);
				ruleRepo.save(rule);
				auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Update_Rule, "deactive rule " + ruleId);
			}

			auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Remove_Rule_From_Partition, "remove rule " + ruleId + " from partition " + partition.getId());
			checkStale(loginUser, partition, "rule is removed");
		} catch (Exception e) {
			log.error("Fail to remove rule to parition: ", e);
			throw new AppException(e);
		}

	}

	@Override
	public PartitionDefinition removeRule(UserDefinition loginUser, Integer partitionId, Integer ruleId) throws AppException {
		try {
			PartitionDefinition partition = partitionDefRepo.findOne(partitionId);
			removeRule(loginUser, partition, ruleId);
			return partition;
		} catch (Exception e) {
			log.error("Fail to remove rule to parition: ", e);
			throw new AppException(e);
		}
	}

	@Override
	public PartitionDefinition getPartitionDetails(Integer partitionId) throws AppException {
		PartitionDefinition partition = partitionDefRepo.findOne(partitionId);
		if (partition == null) {
			throw new AppException("Invalid partition id " + partitionId);
		}

		partition.setPartitionExportHistories(exportRepo.findAllByOrigPartitionId(partition.getOrigPartitionId()));

		if (partition.getRuleIds() == null) {
			return partition;
		}
		for (String ruleId : partition.getRuleIds().split(",")) {
			partition.addRule(ruleRepo.findOne(Integer.valueOf(ruleId)));
		}

		return partition;
	}

	@Override
	public List<PartitionDefinition> getAllActivePartitions() {
		List<PartitionDefinition> partitions = partitionDefRepo.findAllActivePartitions();

		if (log.isDebugEnabled()) log.debug("active partition size: {}", partitions.size());
		for (PartitionDefinition partition : partitions) {
			Integer origId = partition.getOrigPartitionId();
			if (origId == null) {
				origId = partition.getId();
			}
			partition.setPartitionExportHistories(exportRepo.findAllByOrigPartitionId(origId));

			String ruleIds = partition.getRuleIds();

			if (ruleIds != null) {
				for (String ruleId : ruleIds.trim().split(",")) {
					partition.addRule(ruleRepo.findOne(Integer.valueOf(ruleId)));
				}
			}
		}

		return partitions;
	}

	@Transactional
	@Override
	public void savePartition(UserDefinition loginUser, PartitionDefinition partition) throws AppException {
		String action = AuditTrailActionDefinition.Create_Partition;

		if (partition.getId() != null) {
			action = AuditTrailActionDefinition.Update_Partition;
		} else {
			partition.setStatus(PartitionStatus.Fresh.value());
		}

		partition.setCustomerName(loginUser.getCustomerName());
		partition.setLastUpdated(DateTimeHelper.nowInUTC());
		partition.setLastUpdatedBy(loginUser.getUserName());

		partitionDefRepo.save(partition);

		if (partition.getOrigPartitionId() == null) {
			partition.setOrigPartitionId(partition.getId());
			partitionDefRepo.save(partition);
		}

		auditService.saveAuditTrailLog(loginUser, action, "partition id: " + partition.getId());
	}

	private void updateRuleId(PartitionDefinition partition, Set<String> ruleIds, String userName) {
		partition.setRuleIds(StringUtils.collectionToCommaDelimitedString(ruleIds));
		partition.setLastUpdated(DateTimeHelper.nowInUTC());
		partition.setLastUpdatedBy(userName);
		partitionDefRepo.save(partition);
	}


	@Override
	public void checkStale(UserDefinition loginUser, PartitionDefinition partition, String reason) {
		if (partition.getStatus().equals(PartitionStatus.Draft.value())) {
			log.info("Change partition {} to stale", partition.getId());
			setPartitionToStale(loginUser, partition, reason);
		}
	}

	@Override
	public void checkStale(UserDefinition loginUser, Integer partitionId, String reason) {
		PartitionDefinition partition = partitionDefRepo.findOne(partitionId);
		checkStale(loginUser, partition, reason);
	}

	@Override
	public void checkStale(UserDefinition loginUser, ListDefinition listDefinition, String reason) {
		List<PartitionDefinition> partitions = partitionDefRepo.findAllActivePartitions();

		for(PartitionDefinition partition: partitions) {
			if (partition.getStatus().equals(PartitionStatus.Draft.value())) {
				Integer blId = partition.getBlId();
				Integer wlId = partition.getWlId();

				if ( blId != null && blId == listDefinition.getId()) {
					setPartitionToStale(loginUser, partition, "attached black list " + listDefinition.getListName() + " has been updated");
				}
				else if ( wlId != null && wlId == listDefinition.getId()) {
					setPartitionToStale(loginUser, partition, "attached white list " + listDefinition.getListName() + " has been updated");
				}
			}
		}

	}

	private void setPartitionToStale(final UserDefinition loginUser, PartitionDefinition partition, final String reason) {
		partition.setStatus(PartitionStatus.Stale.value());
		partitionDefRepo.save(partition);

		EventNotification event = new EventNotification();
		event.setCreateTimestamp(DateTimeHelper.nowInUTC());
		event.setEventType(EventTypeDefinition.Partition_Stale.value());
		event.setReferenceId(partition.getId());
		event.setCustomerName(loginUser.getCustomerName());
		event.setStatus("new");
		event.setMessage("Partition " + partition.getName() + " is staled due to " + reason);
		event.setLastUpdatedBy("system");
		eventRepo.save(event);
		
		//broadcast to web client
		eventService.broadcastPartitionEvent(loginUser.getCustomerId(), event);	
	}


}
