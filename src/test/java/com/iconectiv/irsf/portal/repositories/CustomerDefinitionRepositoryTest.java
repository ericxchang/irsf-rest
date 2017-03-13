package com.iconectiv.irsf.portal.repositories;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;


import com.iconectiv.irsf.portal.model.common.CustomerDefinition;
import com.iconectiv.irsf.portal.repositories.common.CustomerDefinitionRepository;
import com.iconectiv.irsf.portal.util.JsonHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-cfg.xml", "classpath:spring-jpa.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})

public class CustomerDefinitionRepositoryTest {
	private static Logger log = LoggerFactory.getLogger(CustomerDefinitionRepositoryTest.class);
	@Autowired
	CustomerDefinitionRepository customerRepo;
	
	@Test
	public void testAdAndDeletedCustomerDefinition() {
		CustomerDefinition customer = new CustomerDefinition();
		customer.setCustomerName("JUNITCustomer1");

		
		customer = customerRepo.save(customer);
		log.info(JsonHelper.toPrettyJson(customer));

		customerRepo.delete(customer);

		customer = customerRepo.findByCustomerName("JUNITCustomer1");
		Assert.assertNull(customer);
	}

}
