package com.iconectiv.irsf.portal.repositories;

import java.util.Set;

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

import com.iconectiv.irsf.portal.repositories.common.CcNdcIndexRepository;
import com.iconectiv.irsf.portal.service.MobileIdDataService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-cfg.xml", "classpath:spring-jpa.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})

public class CcNDCRepositoryTest {
	private static Logger log = LoggerFactory.getLogger(CcNDCRepositoryTest.class);
	
	@Autowired
	CcNdcIndexRepository repository;
	@Autowired
	MobileIdDataService midDataService;
	
	@Test
	public void testQueryCache() throws Exception {
		log.info("1st query ....");
		Set<String> ccNDC = repository.findAllItem();
		log.info("return data {}", ccNDC.size());

		//do again
		log.info("2nd query ....");
		Set<String> ccNDC1 = repository.findAllItem();
		log.info("return data {}", ccNDC1.size());
		
		midDataService.cleanCache();
		log.info("3rd query ....");
		Set<String> ccNDC3 = repository.findAllItem();
		log.info("return data {}", ccNDC3.size());

	}
}
