package com.iconectiv.irsf.portal.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iconectiv.irsf.json.vaidation.JsonValidationException;
import com.iconectiv.irsf.portal.core.AuditTrailActionDefinition;
import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.common.RangeQueryFilter;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.PartitionDefinition;
import com.iconectiv.irsf.portal.model.customer.RuleDefinition;
import com.iconectiv.irsf.portal.repositories.customer.PartitionDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.customer.RuleDefinitionRepository;
import com.iconectiv.irsf.portal.service.AuditTrailService;
import com.iconectiv.irsf.portal.service.RuleService;
import com.iconectiv.irsf.util.JsonHelper;

/**
 * Created by echang on 1/11/2017.
 */
@Service
@Transactional
public class RuleServiceImpl implements RuleService {
	private static Logger log = LoggerFactory.getLogger(RuleServiceImpl.class);

	@Autowired
	private RuleDefinitionRepository ruleRepo;
	@Autowired
	private PartitionDefinitionRepository partitionRepo;
	@Autowired
	private AuditTrailService auditService;

	@Transactional
	@Override
	public void updateRule(UserDefinition loginUser, RuleDefinition rule) throws AppException {
		String action = AuditTrailActionDefinition.Update_Rule;

		if (rule.getId() == null) {
			throw new AppException("Missing rule Id");
		}

		if (rule.getPartitionId() == null) {
			throw new AppException("Missing partition Id");
		}

		rule.setDetails(cleanRuleDetail(rule.getDetails()));
		rule.setLastUpdated(new Date());
		rule.setLastUpdatedBy(loginUser.getUserName());
		rule = ruleRepo.save(rule);
		auditService.saveAuditTrailLog(loginUser, action, "rule id: " + rule.getId());

		// TODO raise rule update event to check partition status
	}

	// clean up rule detail, remove GUI specific data
	private JsonNode cleanRuleDetail(String ruleDetail) throws AppException {
		try {
			if (log.isDebugEnabled()) log.debug("orignal detail: " + ruleDetail);
			RangeQueryFilter filterObj = JsonHelper.fromJson(ruleDetail, RangeQueryFilter.class);
			filterObj.setPageNo(null);
			filterObj.setLimit(null);
			 ObjectMapper mapper = new ObjectMapper();
			 
			JsonNode result = mapper.readTree(JsonHelper.toJson(filterObj));

			if (log.isDebugEnabled())
				log.debug("rule detail to save: " + result);
			return result;
		} catch (JsonValidationException e) {
			log.error("Error to convert json:", e);
			throw new AppException(e.getMessage());
		} catch (Exception e) {
			log.error("Error to convert json:", e);
			throw new AppException(e.getMessage());
		}

	}

	@Transactional
	@Override
	public void createRule(UserDefinition loginUser, RuleDefinition rule) throws AppException {
		String action = AuditTrailActionDefinition.Create_Rule;

		if (rule.getPartitions() == null || rule.getPartitions().isEmpty()) {
			throw new AppException("Need assign at least One partition");
		}
		
		rule.setDetails(cleanRuleDetail(rule.getDetails()));

		rule.setCreateTimestamp(new Date());
		rule.setCreatedBy(loginUser.getUserName());

		rule.setLastUpdated(new Date());
		rule.setLastUpdatedBy(loginUser.getUserName());

		for (PartitionDefinition partition : rule.getPartitions()) {
			rule.setId(null);
			rule.setPartitionId(partition.getId());
			rule = ruleRepo.save(rule);
			auditService.saveAuditTrailLog(loginUser, action, "created rule id: " + rule.getId());
			addRuleToPartition(loginUser, rule);
		}

	}

	private void addRuleToPartition(UserDefinition loginUser, RuleDefinition rule) throws AppException {
		log.info("Add rule id {} to partition {}", rule.getId(), rule.getPartitionId());
		PartitionDefinition partition = partitionRepo.findOne(rule.getPartitionId());
		if (partition == null) {
			throw new AppException("partition " + rule.getPartitionId() + " does NOT exist");
		}

		String ruleIds = partition.getRuleIds();
		if (ruleIds == null) {
			ruleIds = rule.getId().toString();
		} else if (ruleIds.indexOf(rule.getId()) < 0) {
			ruleIds += "," + rule.getId();
			if (log.isDebugEnabled())
				log.debug("");
		} else {
			return;
		}

		partition.setRuleIds(ruleIds);
		partitionRepo.save(partition);
		auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Add_Rule_To_Partition,
		        "append rule " + rule.getId() + " to partition " + partition.getId());
		return;
	}
}
