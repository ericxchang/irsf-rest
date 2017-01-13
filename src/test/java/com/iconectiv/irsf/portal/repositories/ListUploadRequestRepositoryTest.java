package com.iconectiv.irsf.portal.repositories;

import com.iconectiv.irsf.portal.model.common.ListUploadRequest;
import com.iconectiv.irsf.portal.repositories.common.ListUploadRequestRepository;
import com.iconectiv.irsf.portal.util.JsonHelper;
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

import java.util.Date;

import static org.junit.Assert.assertNull;

/**
 * Created by echang on 1/12/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-cfg.xml", "classpath:spring-jpa.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})

public class ListUploadRequestRepositoryTest {
    private static Logger log = LoggerFactory.getLogger(ListUploadRequestRepositoryTest.class);
    @Autowired
    private ListUploadRequestRepository repository;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testCRUDAction() throws Exception {
        ListUploadRequest request = new ListUploadRequest();
        request.setAccount("cust01");
        request.setListName("black01");
        request.setPath("/tmp/irsf/cust01/black01.txt");
        request.setStatus("ready");
        request.setLastUpdated(new Date());
        request = repository.save(request);
        log.info("Save request {}", request.getId());

        log.info(JsonHelper.toJson(repository.findOne(request.getId())));
        repository.delete(request.getId());

        assertNull(repository.findOne(request.getId()));
    }


}