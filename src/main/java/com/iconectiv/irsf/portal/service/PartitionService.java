package com.iconectiv.irsf.portal.service;

import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.PartitionDefinition;
import com.iconectiv.irsf.portal.model.customer.RuleDefinition;

import java.util.List;

public interface PartitionService {
	void refreshPartition(UserDefinition loginUser, PartitionDefinition partitionId) throws AppException;
	void exportPartition(UserDefinition loginUser, PartitionDefinition partitionId) throws AppException;
	void addRule(UserDefinition loginUser, PartitionDefinition partition, RuleDefinition rule) throws AppException;
	void removeRule(UserDefinition loginUser, PartitionDefinition partition, Integer ruleId) throws AppException;
	void savePartition(UserDefinition loginUser, PartitionDefinition partition) throws AppException;
	PartitionDefinition getPartitionDetails(Integer partitionId) throws AppException;
	List<PartitionDefinition> getAllActivePartitions();
}
