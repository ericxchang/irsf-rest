package com.iconectiv.irsf.portal.service.impl;

import com.iconectiv.irsf.portal.core.AuditTrailActionDefinition;
import com.iconectiv.irsf.portal.core.PartitionStatus;
import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.PartitionDefinition;
import com.iconectiv.irsf.portal.model.customer.RuleDefinition;
import com.iconectiv.irsf.portal.repositories.customer.PartitionDataDetailsRepository;
import com.iconectiv.irsf.portal.repositories.customer.PartitionDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.customer.PartitionExportHistoryRepository;
import com.iconectiv.irsf.portal.repositories.customer.RuleDefinitionRepository;
import com.iconectiv.irsf.portal.service.AuditTrailService;
import com.iconectiv.irsf.portal.service.PartitionService;
import io.jsonwebtoken.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public void refreshPartition(UserDefinition loginUser, PartitionDefinition partition) throws AppException {
        validateParitionStatus(partition);

        partition.setStatus(PartitionStatus.Processing.value());
        partitionDefRepo.save(partition);
        refreshParitionData(loginUser, partition);

    }

    @Transactional
    @Async
    private void refreshParitionData(UserDefinition loginUser, PartitionDefinition partition) throws AppException{
        try {
            generateDraftData(partition);

            partition.setStatus(PartitionStatus.Draft.value());
            partition.setDraftDate(new Date());
			partition.setLastUpdatedBy(loginUser.getUserName());
            partitionDefRepo.save(partition);

            auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Refresh_Partition_Data, "generated partition draft data set:" + partition.getId());
        } catch (Exception e) {
            log.error("Error on export partition data", e);
            throw new AppException(e);
        }
    }

    /*
	 * To export partition: 1. copy data to parition_export_history data and
	 * move the status of current partition to "Locked" 2. clear data from
	 * partition_data_details table 3. clone a new partition with status of
	 * "fresh" and underlying rule(s) 4. create event to send export data to EI
	 * system
	 */
	@Override
    public void exportPartition(UserDefinition loginUser, PartitionDefinition partition) throws AppException {
        String status = validateParitionStatus(partition);
    }

    private String validateParitionStatus(PartitionDefinition partition) throws AppException {
        partition = partitionDefRepo.findOne(partition.getId());
        Assert.notNull(partition);

        if (partition.getStatus().equals(PartitionStatus.Processing.value())) {
            throw new AppException("System is generating parttion data set");
        }
        return partition.getStatus();
    }


    @Transactional
    @Async
	private void exportPartitionData(UserDefinition loginUser, PartitionDefinition partition) throws AppException {
		try {
			checkPartitionStale(partition);

			partition.setStatus(PartitionStatus.Locked.value());
			partition.setLastExportDate(new Date());
			partition.setLastUpdatedBy(loginUser.getUserName());
			partitionDefRepo.save(partition);

			partitionDataRepo.deleteByPartitionId(partition.getId());

			clonePartition(loginUser, partition);
            auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Export_Partition_Data, "export partition data set " + partition.getId());
		} catch (Exception e) {
			log.error("Error on export partition data", e);
			throw new AppException(e);
		}
	}

	/*
	 * The following condition can cause staled draft data set:
	 * 1. BL/WL list change
	 * 2. Rule(s) change
	 * 3. New MobileID data set
	 */
    private boolean checkPartitionStale(PartitionDefinition partition) {
		boolean isStale = false;
		
		Date draftTime = partition.getDraftDate();
		if (isStale) {
			partition.setStatus(PartitionStatus.Stale.value());
			partitionDefRepo.save(partition);
		}
		
		return isStale;
	}

	private void generateDraftData(final PartitionDefinition partition) {
		// TODO copy data to partition_export_history; create event to send
		// export data
		
		partition.setDraftDate(new Date());
		partitionDefRepo.save(partition);
	}

	private void clonePartition(UserDefinition loginUser, PartitionDefinition partition) {
		if (partition.getOrigPartitionId() == null) {
			partition.setOrigPartitionId(partition.getId());
		}
		partition.setId(null);
		partition.setStatus(PartitionStatus.Fresh.value());
		partition.setLastUpdated(new Date());
		partition.setLastExportDate(null);
		partition.setDraftDate(null);
		partition.setLastUpdatedBy("cloned");
		partitionDefRepo.save(partition);

		List<String> ruleIds = cloneRules(loginUser, partition);

		partition.setRuleIds(String.join(",", ruleIds));
		partitionDefRepo.save(partition);
        auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Clone_Partition, "clone new partition " + partition.getId());

		return;
	}

	private List<String> cloneRules(UserDefinition loginUser, final PartitionDefinition partition) {
		List<String> ruleIds = new ArrayList<>();
		for (String ruleId : partition.getRuleIds().split(",")) {
			RuleDefinition rule = ruleRepo.findOne(Integer.valueOf(ruleId));
            Assert.notNull(rule);

            //mark the old one inActive
            rule.setActive(false);
            ruleRepo.save(rule);

            //create a new rule
			rule.setId(null);
			rule.setLastUpdated(new Date());
			rule.setCreatedBy("cloned");
			rule.setPartitionId(partition.getId());
			rule.setActive(true);
			ruleRepo.save(rule);

			ruleIds.add(rule.getId().toString());
            auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Clone_Rule, "clone new rule " + rule.getId());

			if (log.isDebugEnabled())
				log.debug("Clone new rule {}", rule.getId());
		}
		return ruleIds;
	}

	@Transactional
	@Override
	public void addRule(UserDefinition loginUser, PartitionDefinition partition, RuleDefinition rule)  throws AppException {
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
			auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Add_Rule_To_Partition, "append rule " + rule.getId() + " to partition " + partition.getId());
		} catch (Exception e) {
			log.error("Fail to add rule to parition: ", e);
			throw new AppException(e);
		}
	}

	@Transactional
	@Override
	public void removeRule(UserDefinition loginUser, PartitionDefinition partition, Integer ruleId)  throws AppException {
		try {
            Assert.notNull(partition);
            Assert.notNull(ruleId);

			Set<String> ruleIds = new HashSet<>();
			Collections.addAll(ruleIds, partition.getRuleIds().split(","));

			if (ruleIds.contains(ruleId.toString())) {
				ruleIds.remove(ruleId.toString());
			}

			updateRuleId(partition, ruleIds, loginUser.getUserName());
			
			//mark rule as inactive
			RuleDefinition rule = ruleRepo.findOne(ruleId);
			if (rule != null) {
				rule.setActive(false);
				rule.setPartitionId(null);
				ruleRepo.save(rule);
				auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Update_Rule, "deactive rule " + ruleId);
			}

			auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Remove_Rule_To_Partition, "remove rule " + ruleId + " from partition " + partition.getId());

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
		for (PartitionDefinition partition: partitions) {
			Integer origId = partition.getOrigPartitionId();
			if (origId == null) {
				origId = partition.getId();
			}
			partition.setPartitionExportHistories(exportRepo.findAllByOrigPartitionId(origId));
			
			String ruleIds = partition.getRuleIds();
			
			if (ruleIds != null) {
				for (String ruleId : ruleIds.split(",")) {
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
        partition.setLastUpdated(new Date());
        partition.setLastUpdatedBy(loginUser.getUserName());
        
        partition = partitionDefRepo.save(partition);
        
        if (partition.getOrigPartitionId() == null) {
        	partition.setOrigPartitionId(partition.getId());
        	partitionDefRepo.save(partition);
        }
        
        auditService.saveAuditTrailLog(loginUser, action, "partition id: " + partition.getId());
    }

    private void updateRuleId(PartitionDefinition partition, Set<String> ruleIds, String userName) {
		partition.setRuleIds(StringUtils.collectionToCommaDelimitedString(ruleIds));
		partition.setLastUpdated(new Date());
		partition.setLastUpdatedBy(userName);
		partitionDefRepo.save(partition);
	}

}
