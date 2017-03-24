package com.iconectiv.irsf.portal.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.iconectiv.irsf.jwt.JWTUtil;
import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.model.common.UserDefinition;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-cfg.xml", "classpath:spring-jpa.xml"})
@WebAppConfiguration
public class PartitionServiceControllerTest {
	private static Logger log = LoggerFactory.getLogger(PartitionServiceControllerTest.class);
	
	@Autowired WebApplicationContext wac; 
	@Autowired MockHttpSession session;
	@Autowired MockHttpServletRequest request;
	
	private MockMvc mockMvc;
	
	@Before
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetPartitionDetail() throws Exception {
		UserDefinition loginUser = new UserDefinition();
		loginUser.setUserName("guiuser01");
		loginUser.setCustomerId(1);
		loginUser.setRole(PermissionRole.CustAdmin.value());	
		loginUser.setSchemaName("cust01");
		loginUser.setCustomerName("Verizon");
		
		String token = JWTUtil.createToken(loginUser);
		
		ResultActions action = mockMvc.perform(get("/partition/4").header("Authorization", "Bearer " + token)).andExpect(status().isOk());
		String result = action.andReturn().getResponse().getContentAsString();
		
		log.info(result);
		
		assertTrue(result.lastIndexOf("success") > 1);
	}

}
