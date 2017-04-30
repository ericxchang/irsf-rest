package com.iconectiv.irsf.portal.controller;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

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

import com.iconectiv.irsf.jwt.JWTUtil;
import com.iconectiv.irsf.portal.core.DialPatternType;
import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.RuleDefinition;
import com.iconectiv.irsf.util.DateTimeHelper;
import com.iconectiv.irsf.util.JsonHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-cfg.xml", "classpath:spring-jpa.xml"})
@WebAppConfiguration
public class EventServiceControllerTest  {
	private static Logger log = LoggerFactory.getLogger(EventServiceControllerTest.class);
	
	@Autowired WebApplicationContext wac; 
	@Autowired MockHttpSession session;
	@Autowired MockHttpServletRequest request;
	
	private MockMvc mockMvc;
	private UserDefinition loginUser;
	private String token;

	@Before
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        loginUser = new UserDefinition();
        loginUser.setUserName("user01");
        loginUser.setCustomerId(1);
        loginUser.setRole(PermissionRole.CustAdmin.value());
        loginUser.setSchemaName("cust01");
        loginUser.setCustomerName("Verizon");

        token = JWTUtil.createToken(loginUser);
    }

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testQueryEvent() throws Exception {
		Date myDate = DateTimeHelper.getDateBefore(30).toDate();
        ResultActions action = mockMvc.perform(get("/event" + "?lastQueryTime=" + myDate.getTime()).header("authorization", "Bearer " + token)).andExpect(status().isOk());
        String result = action.andReturn().getResponse().getContentAsString();

       assertTrue(result.lastIndexOf("success") > 1);
    }
 }
