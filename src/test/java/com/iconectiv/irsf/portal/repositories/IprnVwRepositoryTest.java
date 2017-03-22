package com.iconectiv.irsf.portal.repositories;

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

import com.iconectiv.irsf.portal.model.common.IprnVw;
import com.iconectiv.irsf.portal.repositories.common.IprnVwRepository;
import com.iconectiv.irsf.portal.util.JsonHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-cfg.xml", "classpath:spring-jpa.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})

public class IprnVwRepositoryTest {
	private static Logger log = LoggerFactory.getLogger(IprnVwRepositoryTest.class);
	
	@Autowired
	IprnVwRepository repository;
	
	@Test
	public void testQueryIprnVw() throws Exception {
		PageRequest page = new PageRequest(0, 5);
		Page<IprnVw> result = repository.findAll(page);
		log.info(JsonHelper.toJson(result));
	}
}
