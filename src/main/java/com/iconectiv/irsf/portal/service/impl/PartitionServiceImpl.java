package com.iconectiv.irsf.portal.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.iconectiv.irsf.portal.core.PartitionStatus;
import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.customer.PartitionDefinition;
import com.iconectiv.irsf.portal.model.customer.RuleDefinition;
import com.iconectiv.irsf.portal.repositories.customer.PartitionDataDetailsRepository;
import com.iconectiv.irsf.portal.repositories.customer.PartitionDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.customer.PartitionExportHistoryRepository;
import com.iconectiv.irsf.portal.repositories.customer.RuleDefinitionRepository;
import com.iconectiv.irsf.portal.service.PartitionService;

import io.jsonwebtoken.lang.Assert;


@Service
public class PartitionServiceImpl implements PartitionService {
	private static Logger log = LoggerFactory.getLogger(PartitionServiceImpl.class);

	@Autowired
	private PartitionDefinitionRepository defRepo;
	@Autowired
	private PartitionExportHistoryRepository exportRepo;
	@Autowired
	private PartitionDataDetailsRepository dataRepo;
	@Autowired
	private RuleDefinitionRepository ruleRepo;

	/*
	 * To export partition: 1. copy data to parition_export_history data and
	 * move the status of current partition to "Locked" 2. clear data from
	 * partition_data_details table 3. clone a new partition with status of
	 * "fresh" and underlying rule(s) 4. create event to send export data to EI
	 * system
	 */
	@Transactional
	@Override
	public void exportPartitionData(Integer partitionId, String userName) throws AppException {
		try {
			PartitionDefinition partition = defRepo.findOne(partitionId);
            Assert.notNull(partition);

			generateExportData(partition);

			partition.setStatus(PartitionStatus.Locked.value());
			defRepo.save(partition);

			dataRepo.deleteByPartitionId(partitionId);

			clonePartition(partition, userName);

		} catch (Exception e) {
			log.error("Error on export partition data", e);
			throw new AppException(e);
		}

	}

	private void generateExportData(final PartitionDefinition partition) {
		// TODO copy data to partition_export_history; create event to send
		// export data

	}

	private void clonePartition(PartitionDefinition partition, String userName) {
		partition.setId(null);
		partition.setStatus(PartitionStatus.Fresh.value());
		partition.setLastUpdated(new Date());
		partition.setLastExportDate(null);
		partition.setDraftDate(null);
		partition.setLastUpdatedBy(userName);
		defRepo.save(partition);

		List<String> ruleIds = cloneRules(partition);

		partition.setRuleIds(String.join(",", ruleIds));
		defRepo.save(partition);

		return;
	}

	private List<String> cloneRules(final PartitionDefinition partition) {
		List<String> ruleIds = new ArrayList<>();
		for (String ruleId : partition.getRuleIds().split(",")) {
			RuleDefinition rule = ruleRepo.findOne(Integer.valueOf(ruleId));
            Assert.notNull(rule);

			rule.setId(null);
			rule.setPartitionId(partition.getId());
			ruleRepo.save(rule);

			ruleIds.add(rule.getId().toString());

			if (log.isDebugEnabled())
				log.debug("Clone new rule {}", rule.getId());
		}
		return ruleIds;
	}

	@Transactional
	@Override
	public void addRule(PartitionDefinition partition, RuleDefinition rule, String userName)  throws AppException {
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

			updateRuleId(partition, ruleIds, userName);
		} catch (Exception e) {
			log.error("Fail to add rule to parition: ", e);
			throw new AppException(e);
		}
	}

	@Transactional
	@Override
	public void removeRule(PartitionDefinition partition, Integer ruleId, String userName)  throws AppException {
		try {
            Assert.notNull(partition);
            Assert.notNull(ruleId);

			Set<String> ruleIds = new HashSet<>();
			Collections.addAll(ruleIds, partition.getRuleIds().split(","));

			if (ruleIds.contains(ruleId.toString())) {
				ruleIds.remove(ruleId.toString());
			}

			updateRuleId(partition, ruleIds, userName);
		} catch (Exception e) {
			log.error("Fail to remove rule to parition: ", e);
			throw new AppException(e);
		}

	}

	@Override
	public PartitionDefinition getPartitionDetails(Integer partitionId) throws AppException {
		PartitionDefinition partition = defRepo.findOne(partitionId);
		if (partition == null) {
			throw new AppException("Invalid partition id " + partitionId);
		}

		for (String ruleId : partition.getRuleIds().split(",")) {
			partition.addRule(ruleRepo.findOne(Integer.valueOf(ruleId)));
		}

		return partition;
	}

	private void updateRuleId(PartitionDefinition partition, Set<String> ruleIds, String userName) {
		partition.setRuleIds(StringUtils.collectionToCommaDelimitedString(ruleIds));
		partition.setLastUpdated(new Date());
		partition.setLastUpdatedBy(userName);
		defRepo.save(partition);
	}

}
