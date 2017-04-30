package com.iconectiv.irsf.portal.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
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

import com.iconectiv.irsf.portal.model.common.CustomerDefinition;
import com.iconectiv.irsf.portal.model.common.ProviderBillingId;
import com.iconectiv.irsf.portal.model.common.RangeNdc;
import com.iconectiv.irsf.portal.repositories.common.CustomerDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.common.RangeNdcRepository;
import com.iconectiv.irsf.util.JsonHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-cfg.xml", "classpath:spring-jpa.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
public class RangeNdcRepositoryTest {
	
	private static Logger log = LoggerFactory.getLogger(RangeNdcRepositoryTest.class);
	@Autowired
	RangeNdcRepository repository;
	

	
	@Test
	public void testQueryrangeNdc() {
		List<String> codeList = new ArrayList<String>();
		List<String> tosList = new ArrayList<String>();
		List<String> tosDescList = new ArrayList<String>();
		List<String> providerList = new ArrayList<String>();
		Page<RangeNdc> pageList = null; 
		List<RangeNdc> list = null;
		PageRequest page = new PageRequest(0, 10);
		
		codeList.add("51");
		tosList.add("U");
		tosDescList.add("G,Geographic");
		providerList.add("Afghan Telecom");
		
		list = repository.findRangeNdcbyRule1(codeList);
		System.out.println("number of rows: " + list.size());
		
		pageList = repository.findRangeNdcbyRule12(tosList, tosDescList, page);
		for (RangeNdc ndc: pageList)
			log.info(JsonHelper.toJson(ndc));
		
		
		pageList = repository.findRangeNdcbyRule28(tosList, tosDescList, providerList, page);
		for (RangeNdc ndc: pageList)
			log.info(JsonHelper.toJson(ndc));
		
		
		 
	}
}
