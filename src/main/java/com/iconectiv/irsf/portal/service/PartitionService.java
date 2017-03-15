package com.iconectiv.irsf.portal.service;

import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.customer.PartitionDefinition;
import com.iconectiv.irsf.portal.model.customer.RuleDefinition;

public interface PartitionService {
	void exportPartitionData(Integer partitionId, String userName) throws AppException;
	void addRule(PartitionDefinition partition, RuleDefinition rule, String userName) throws AppException;
	void removeRule(PartitionDefinition partition, Integer ruleId, String userName) throws AppException;
	PartitionDefinition getPartitionDetails(Integer partitionId) throws AppException;
}
