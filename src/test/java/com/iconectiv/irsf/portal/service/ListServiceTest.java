package com.iconectiv.irsf.portal.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

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

import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.model.customer.ListDetails;
import com.iconectiv.irsf.portal.repositories.customer.ListDetailsRepository;
import com.iconectiv.irsf.util.JsonHelper;
import com.iconectiv.irsf.util.ListDetailConvert;
import com.iconectiv.irsf.util.ListHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-cfg.xml", "classpath:spring-jpa.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})

public class ListServiceTest {
	private static Logger log = LoggerFactory.getLogger(ListServiceTest.class);
	
	@Autowired
	ListService listService;
	
	@Autowired
	ListDetailsRepository listRepo;
	
    @Test
    public void testQueryListByListId() throws Exception {
    	CustomerContextHolder.setSchema("cust01");
    	List<ListDetails> results = listService.getListDetailDataByListId(74);
    	log.info(JsonHelper.toPrettyJson(results));
    	log.info("Total return: " + results.size());
    }

    @Test
    public void testQueryListByListIdPage() throws Exception {
    	CustomerContextHolder.setSchema("cust01");
    	
    	PageRequest page = new PageRequest(0, 5);
    	Page<Object[]> results = listRepo.findAllDetailsByListRefId(74, page);
    	
    	final Page<ListDetails> detailsPage = results.map(new ListDetailConvert());
    	
    	log.info(JsonHelper.toPrettyJson(detailsPage));
    	log.info("Total return: " + results.getSize());
    }

    @Test
    public void testQueryListByUploadId() throws Exception {
    	CustomerContextHolder.setSchema("cust01");
    	List<ListDetails> results = listService.getListDetailDataByUploadId(118);
    	log.info(JsonHelper.toPrettyJson(results));
    	log.info("Total return: " + results.size());
    }
 }
