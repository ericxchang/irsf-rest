package com.iconectiv.irsf.portal.service;

import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.ListDefinition;
import com.iconectiv.irsf.portal.model.customer.PartitionDefinition;
import com.iconectiv.irsf.portal.model.customer.RuleDefinition;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PartitionService {
	void refreshPartition(UserDefinition loginUser, Integer partitionId);

    @Transactional
    void refreshPartitionData(UserDefinition loginUser, PartitionDefinition partition) throws AppException;

    void exportPartition(UserDefinition loginUser, Integer partitionId) throws AppException;
	void addRule(UserDefinition loginUser, PartitionDefinition partition, RuleDefinition rule) throws AppException;
	
	void removeRule(UserDefinition loginUser, PartitionDefinition partition, Integer ruleId) throws AppException;
	PartitionDefinition removeRule(UserDefinition loginUser, Integer partitionId, Integer ruleId) throws AppException;

	void savePartition(UserDefinition loginUser, PartitionDefinition partition) throws AppException;
	PartitionDefinition getPartitionDetails(Integer partitionId) throws AppException;
	List<PartitionDefinition> getAllActivePartitions();
	
	void checkStale(UserDefinition loginUser, PartitionDefinition partition, String reason);
	void checkStale(UserDefinition loginUser, ListDefinition listDefinition, String reason);
	void checkStale(UserDefinition loginUser, Integer partitionId, String reason);
}
