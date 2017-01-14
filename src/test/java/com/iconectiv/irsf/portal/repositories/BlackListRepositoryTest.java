package com.iconectiv.irsf.portal.repositories;

import org.junit.Before;
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

import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.model.customer.BlackList;
import com.iconectiv.irsf.portal.repositories.customer.BlackListRepository;
import com.iconectiv.irsf.portal.util.JsonHelper;

/**
 * Created by echang on 1/12/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-cfg.xml", "classpath:spring-jpa.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})

public class BlackListRepositoryTest {
    private static Logger log = LoggerFactory.getLogger(BlackListRepositoryTest.class);
    @Autowired
    private BlackListRepository repository;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testCreateBlackList() throws Exception {
    	CustomerContextHolder.setCustomer("cust01");
        BlackList item = new BlackList();
        item.setCustomerId("cust01");
        item.setPhone("7326991234");
        item.setListName("blacklist");
        repository.save(item);
        
        log.info("Save request {}", item.getId());

        log.info(JsonHelper.toJson(repository.findOne(item.getId())));

    }


}