package com.iconectiv.irsf.portal.service;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

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
	
	@Test
	public void testFindMatchCountry() throws Exception {
		assertTrue(midDataService.findMatchingCountry("1", "AI").getCountry().equals("Anguilla"));
		assertTrue(midDataService.findMatchingCountry("1", "AG").getCountry().equals("Antigua and Barbuda"));
	}
	
	@Test
	public void testFinProvider() throws Exception {
		assertTrue(midDataService.findProviderByBillingId("127962").equals("1 Net Telekom"));
		assertTrue(midDataService.findProviderByBillingId("128876").equalsIgnoreCase("zoom"));

		assertTrue(midDataService.findBillingIdsByProvider("zNET telekom Zrt.").equals(Arrays.asList(new String[]{"128303"}) ));
	}

	@Test
	public void testGetTosCount() throws Exception {
		assertTrue(midDataService.getTotalTOSCount("F") == 11);
		assertTrue(midDataService.getTotalTOSCount("U") == 1);
	}
}
