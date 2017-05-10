package com.iconectiv.irsf.portal.repositories;import static org.junit.Assert.assertTrue;import java.util.Date;import java.util.List;import org.junit.Before;import org.junit.Test;import org.junit.runner.RunWith;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.test.context.ContextConfiguration;import org.springframework.test.context.TestExecutionListeners;import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;import org.springframework.test.context.support.DirtiesContextTestExecutionListener;import org.springframework.test.context.transaction.TransactionalTestExecutionListener;import org.springframework.transaction.annotation.Transactional;import com.iconectiv.irsf.portal.config.CustomerContextHolder;import com.iconectiv.irsf.portal.model.customer.ListDetails;import com.iconectiv.irsf.portal.repositories.customer.ListDetailsRepository;import com.iconectiv.irsf.util.JsonHelper;/** * Created by echang on 1/12/2017. */@RunWith(SpringJUnit4ClassRunner.class)@ContextConfiguration(locations={"classpath:spring-cfg.xml", "classpath:spring-jpa.xml"})@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,        DirtiesContextTestExecutionListener.class,        TransactionalTestExecutionListener.class})public class ListDetailsRepositoryTest {    private static Logger log = LoggerFactory.getLogger(ListDetailsRepositoryTest.class);    @Autowired    private ListDetailsRepository lstDetailRepo;    @Before    public void setUp() throws Exception {    	CustomerContextHolder.setSchema("cust01");    }    @Test    public void testQueryListByListId() throws Exception {    	CustomerContextHolder.setSchema("cust01");    	List<Object[]> results = lstDetailRepo.findAllDetailsByListRefId(74);    	log.info(JsonHelper.toPrettyJson(results));    	log.info("Total return: " + results.size());    }    @Test    public void testQueryListByUploadId() throws Exception {    	CustomerContextHolder.setSchema("cust01");    	List<Object[]> results = lstDetailRepo.findAllDetailsByUpLoadRefId(118);    	log.info(JsonHelper.toPrettyJson(results));;    	log.info("Total return: " + results.size());    }    @Test    public void testBatchInsertRequest() throws Exception {    	int refId = 33;    	CustomerContextHolder.setSchema("cust01");    	List<ListDetails> entities = lstDetailRepo.findAllByUpLoadRefId(refId);       	    	int initSize = entities.size();    	log.info("Before insert list size: " + initSize);    	    	entities.forEach(entity -> entity.setId(null));    	    	lstDetailRepo.batchUpdate(entities);    	    	entities = lstDetailRepo.findAllByUpLoadRefId(refId);    	    	int endSize = entities.size();    	log.info("After insert list size: " + endSize);    	assertTrue(endSize == initSize*2);    }    @Test    public void testBatchUpdateRequest() throws Exception {    	int refId = 50;    	CustomerContextHolder.setSchema("cust01");    	List<ListDetails> entities = lstDetailRepo.findAllByUpLoadRefId(refId);       	    	int initSize = entities.size();    	log.info("Before update list size: " + initSize);    	    	entities.forEach(entity -> entity.setLastUpdated(new Date()));    	    	lstDetailRepo.batchUpdate(entities);    	    	entities = lstDetailRepo.findAllByUpLoadRefId(refId);    	    	int endSize = entities.size();    	log.info("After update list size: " + endSize);    	assertTrue(endSize == initSize);    }}