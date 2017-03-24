package com.iconectiv.irsf.portal.repositories.common;

import org.springframework.data.repository.CrudRepository;

import com.iconectiv.irsf.portal.model.common.CustomerDefinition;

/**
 * Created by echang on 1/12/2017.
 */
public interface CustomerDefinitionRepository extends CrudRepository<CustomerDefinition, Integer>{
	CustomerDefinition findByCustomerName(String customerName);

	CustomerDefinition findOneBySchemaName(String string);
}
