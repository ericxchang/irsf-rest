package com.iconectiv.irsf.portal.service;

import static org.junit.Assert.assertTrue;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-cfg.xml", "classpath:spring-jpa.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})

public class MobileIdDataServiceTest {
	private static Logger log = LoggerFactory.getLogger(MobileIdDataServiceTest.class);
	
	@Autowired
	MobileIdDataService midDataService;
	
	@Test
	public void testGetMatchCCNDC() throws Exception {
		assertTrue(midDataService.findMatchingCCNDC("12814759123").equals("12814759"));
		assertTrue(midDataService.findMatchingCCNDC("13128833456789").equals("1312883"));
	}
}
