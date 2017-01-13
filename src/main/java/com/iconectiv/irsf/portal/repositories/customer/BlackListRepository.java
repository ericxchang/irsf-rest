package com.iconectiv.irsf.portal.repositories.customer;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.iconectiv.irsf.portal.model.customer.BlackList;

/**
 * Created by echang on 1/12/2017.
 */
public interface BlackListRepository extends CrudRepository<BlackList, Integer>{
	
	@Modifying
	@Transactional
	void deleteAllByCustomerId(String customerId);

}
