package com.iconectiv.irsf.portal.repositories.customer;

import com.iconectiv.irsf.portal.model.customer.RuleDefinition;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by echang on 3/14/2017.
 */
public interface RuleDefinitionRepository extends CrudRepository<RuleDefinition, Integer>{

	List<RuleDefinition> findAllByActive(boolean isActive);

	List<RuleDefinition> findAllByPartitionIdAndActive(Integer partitionId, boolean active);

    @Modifying
    @Transactional
    void deleteAllByPartitionId(Integer parittionId);

	@Modifying
	@Transactional
	void deleteAllById(List<Integer> ids);
}
