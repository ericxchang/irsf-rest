package com.iconectiv.irsf.portal.repositories.common;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iconectiv.irsf.portal.model.common.UserDefinition;

/**
 * Created by echang on 1/12/2017.
 */
public interface UserDefinitionRepository extends CrudRepository<UserDefinition, Integer>{
	
	List<UserDefinition> findAllByCustomerId(Integer customerId);
	
	@Query("select ud from UserDefinition ud, CustomerDefinition cd where ud.customerId=cd.id and cd.customerName=?1")
	List<UserDefinition> findAllByCustomerName(String customerName);

	UserDefinition findOneByUserName(String userName);
	
}
