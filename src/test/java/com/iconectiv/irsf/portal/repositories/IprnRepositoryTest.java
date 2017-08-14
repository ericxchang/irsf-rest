package com.iconectiv.irsf.portal.repositories;

import com.iconectiv.irsf.portal.model.common.Premium;
import com.iconectiv.irsf.portal.repositories.common.PremiumRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.iconectiv.irsf.portal.model.common.Iprn;
import com.iconectiv.irsf.portal.repositories.common.IprnRepository;
import com.iconectiv.irsf.util.JsonHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-cfg.xml", "classpath:spring-jpa.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})

public class IprnRepositoryTest {
	private static Logger log = LoggerFactory.getLogger(IprnRepositoryTest.class);
	
	@Autowired
	IprnRepository repository;
	@Autowired
	PremiumRepository preRepo;
	
	@Test
	public void testQueryIprn() throws Exception {
		PageRequest page = new PageRequest(3, 5);
		Page<Iprn> result = repository.findAll(page);
		log.info(JsonHelper.toJson(result));
	}

	@Test
	public void testQueryPremium() throws Exception {
		PageRequest page = new PageRequest(3, 5);
		Page<Premium> result = preRepo.findAll(page);
		log.info(JsonHelper.toPrettyJson(result));
	}

}
