package com.iconectiv.irsf.portal.controller;

import com.iconectiv.irsf.jwt.JWTUtil;
import com.iconectiv.irsf.portal.core.MessageDefinition;
import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.model.common.CustomerDefinition;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.repositories.common.CustomerDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.common.UserDefinitionRepository;
import com.iconectiv.irsf.util.JsonHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-cfg.xml", "classpath:spring-jpa.xml"})
@WebAppConfiguration
public class AuthServiceControllerTest {
	private static Logger log = LoggerFactory.getLogger(AuthServiceControllerTest.class);
	
	@Autowired WebApplicationContext wac; 
	@Autowired MockHttpSession session;
	@Autowired MockHttpServletRequest request;
	@Autowired
	CustomerDefinitionRepository custRepo;
	@Autowired
	UserDefinitionRepository userRepo;
	
	private MockMvc mockMvc;
	private static String token;
	
	@Before
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateUserRequest() throws Exception {
		CustomerDefinition customer = custRepo.findOneBySchemaName("cust03");
		
		UserDefinition user = new UserDefinition();
		user.setUserName("guiuser03");
		user.setCustomerId(customer.getId());
		user.setRole(PermissionRole.CustAdmin.value());	
		user.setLastUpdatedBy("guiuser01");
		user.setPassword("irsf");
		user.setFirstName("first");
		user.setLastName("last");
		user.setEmail("user@iconectiv.com");
		
		ResultActions action = mockMvc.perform(post("/createUser").contentType(MediaType.APPLICATION_JSON).content(JsonHelper.toJson(user)));
		String result = action.andReturn().getResponse().getContentAsString();
		
		log.info(result);
		
		assertTrue(result.lastIndexOf(MessageDefinition.Create_User_Success) > 1);
		
		userRepo.deleteByUserName("guiuser03");
	}

	@Test
	public void testInvalidUser() throws Exception {
		UserDefinition loginUser = new UserDefinition();
		loginUser.setUserName("guiuser01");
		loginUser.setCustomerId(1);
		loginUser.setRole(PermissionRole.API.value());
		token = JWTUtil.createToken(loginUser);
		
		ResultActions action = mockMvc.perform(get("/list/blacklist").header("Authorization", "Bearer ")).andExpect(status().isForbidden());
		String result = action.andReturn().getResponse().getContentAsString();
		
		log.info(result);

	}
	
	@Test
	public void testInvalidPermission() throws Exception {
		UserDefinition loginUser = new UserDefinition();
		loginUser.setUserName("guiuser01");
		loginUser.setCustomerId(1);
		loginUser.setRole(PermissionRole.API.value());
		token = JWTUtil.createToken(loginUser);
		
		ResultActions action = mockMvc.perform(get("/list/blacklist").header("Authorization", "Bearer " + token)).andExpect(status().isForbidden());
		String result = action.andReturn().getResponse().getContentAsString();
		
		log.info(result);

	}
}
