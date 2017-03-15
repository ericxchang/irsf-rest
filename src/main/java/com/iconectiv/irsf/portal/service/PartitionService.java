package com.iconectiv.irsf.portal.service;

import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.customer.PartitionDefintion;
import com.iconectiv.irsf.portal.model.customer.RuleDefinition;

public interface PartitionService {
	void exportPartitionData(Integer partitionId, String userName) throws AppException;
	void addRule(PartitionDefintion partition, RuleDefinition rule, String userName) throws AppException;
	void removeRule(PartitionDefintion partition, Integer ruleId, String userName) throws AppException;
}
