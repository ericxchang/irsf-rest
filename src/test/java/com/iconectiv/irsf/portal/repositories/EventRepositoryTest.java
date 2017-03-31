package com.iconectiv.irsf.portal.repositories;

import java.util.Date;
import java.util.List;

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

import com.iconectiv.irsf.portal.core.EventTypeDefinition;
import com.iconectiv.irsf.portal.model.common.EventNotification;
import com.iconectiv.irsf.portal.repositories.common.EventNotificationRepository;
import com.iconectiv.irsf.util.DateTimeHelper;
import com.iconectiv.irsf.util.JsonHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-cfg.xml", "classpath:spring-jpa.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})

public class EventRepositoryTest {
	private static Logger log = LoggerFactory.getLogger(EventRepositoryTest.class);
	@Autowired
	EventNotificationRepository eventRepo;
	
	@Before
	public void setUp() throws Exception {
	}
	

	@Test
	public void testEventQuery() {
		Date myDate = DateTimeHelper.getFirstDayOfMonth(-1).toDate();
		List<EventNotification> events = eventRepo.findAllByCustomerNameAndCreateTimestampGreaterThanOrderByCreateTimestampDesc("junitCust", myDate);
		log.info("Find {} events", events.size());
		
		EventNotification event = eventRepo.findTop1ByCustomerNameAndEventTypeOrderByCreateTimestampDesc("junitCust", EventTypeDefinition.List_Update.value());
		log.info(JsonHelper.toPrettyJson(event));

		event = eventRepo.findTop1ByEventTypeOrderByCreateTimestampDesc(EventTypeDefinition.List_Update.value());
		log.info(JsonHelper.toPrettyJson(event));
	}

}
