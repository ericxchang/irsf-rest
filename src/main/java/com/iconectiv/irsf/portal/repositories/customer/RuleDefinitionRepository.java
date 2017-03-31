package com.iconectiv.irsf.portal.repositories.customer;

import com.iconectiv.irsf.portal.model.customer.RuleDefinition;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by echang on 3/14/2017.
 */
public interface RuleDefinitionRepository extends CrudRepository<RuleDefinition, Integer>{

	List<RuleDefinition> findAllByActive(boolean isActive);
}
