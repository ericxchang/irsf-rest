package com.iconectiv.irsf.portal.repositories;

import com.iconectiv.irsf.portal.model.common.ProviderBillingId;
import com.iconectiv.irsf.portal.repositories.common.ProviderBillingIdRepository;
import com.iconectiv.irsf.util.JsonHelper;
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

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-cfg.xml", "classpath:spring-jpa.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
public class ProviderBillingIdRepositoryTest {
	private static Logger log = LoggerFactory.getLogger(ProviderBillingIdRepositoryTest.class);
	@Autowired
	ProviderBillingIdRepository repository;
	
	
	@Test
	public void testFindProvider() {
			
		List<ProviderBillingId> list = repository.findByProvider("012 Smile");
		for (ProviderBillingId p: list)
			log.info(JsonHelper.toJson(p));
	}

	@Test
	public void testFindDistinctProvider() {
		List<ProviderBillingId> list = repository.findAllGroupByProvider();
		log.info("total items: {}", list.size());
		log.info(JsonHelper.toPrettyJson(list));
	}
}
