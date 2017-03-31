package com.iconectiv.irsf.portal.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iconectiv.irsf.portal.core.AuditTrailActionDefinition;
import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.PartitionDefinition;
import com.iconectiv.irsf.portal.model.customer.RuleDefinition;
import com.iconectiv.irsf.portal.repositories.customer.PartitionDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.customer.RuleDefinitionRepository;
import com.iconectiv.irsf.portal.service.AuditTrailService;
import com.iconectiv.irsf.portal.service.RuleService;

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
	public void saveRule(UserDefinition loginUser, RuleDefinition rule) throws AppException {
        String action = AuditTrailActionDefinition.Create_Rule;

        if (rule.getPartitionId() == null) {
        	throw new AppException("Missing partition Id");
        }
        
        if (rule.getId() != null) {
            action = AuditTrailActionDefinition.Update_Rule;
        } else {
        	rule.setCreateTimestamp(new Date());
        	rule.setCreatedBy(loginUser.getUserName());
        }

        ruleRepo.save(rule);
        auditService.saveAuditTrailLog(loginUser, action, "rule id: " + rule.getId());
        
        addRuleToPartition(loginUser, rule);
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
		}
		else if (ruleIds.indexOf(rule.getId()) < 0) {
			ruleIds += "," + rule.getId();
			if (log.isDebugEnabled()) log.debug("");
		} else {
			return;
		}
		
		partition.setRuleIds(ruleIds);
		partitionRepo.save(partition);
		auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Add_Rule_To_Partition, "append rule " + rule.getId() + " to partition " + partition.getId());
		return;
	}
}
