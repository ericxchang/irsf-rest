package com.iconectiv.irsf.portal.repositories.common;

import com.iconectiv.irsf.portal.model.common.CustomerDefinition;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by echang on 1/12/2017.
 */
public interface CustomerDefinitionRepository extends CrudRepository<CustomerDefinition, Integer>{
	CustomerDefinition findByCustomerName(String customerName);

	List<CustomerDefinition> findAllByActive(boolean active);

	CustomerDefinition findOneBySchemaName(String string);
}
