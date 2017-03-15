package com.iconectiv.irsf.portal.repositories;

import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-cfg.xml", "classpath:spring-jpa.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})

public class UserDefinitionRepositoryTest {
	private static Logger log = LoggerFactory.getLogger(UserDefinitionRepositoryTest.class);
	
	@Autowired
	UserService userService;

	@Autowired
	BCryptPasswordEncoder encoder;
	
	@Test
	public void testAdAndDeletedUserDefinition() throws Exception {
		UserDefinition user = new UserDefinition();
		String raw = "1234";

		user.setUserName("junitTestUser");
		user.setRole(PermissionRole.Admin.value());
		user.setFirstName("admin");
		user.setLastName("junit");
		user.setCreateTimestamp(new Date());
		user.setLastUpdated(new Date());
		user.setPassword(raw);
		userService.updateUser(user);
		
		UserDefinition loginUser = userService.findUserByName("junitTestUser");
		assertNotNull(loginUser);
		
		log.info("in db: {}", loginUser.getPassword());
		boolean result = encoder.matches(raw, loginUser.getPassword());
		
		log.info("login pass match result: " + result);

		//log.info(JsonHelper.toPrettyJson(loginUser));
		
		userService.deleteUser(user.getId());		
	}
}
