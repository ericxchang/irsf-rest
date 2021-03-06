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
import com.iconectiv.irsf.util.JsonHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-cfg.xml", "classpath:spring-jpa.xml"})
@WebAppConfiguration
public class RuleServiceControllerTest  {
	private static Logger log = LoggerFactory.getLogger(RuleServiceControllerTest.class);
	
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
        loginUser.setUserName("guiuser01");
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
	public void testRuleCRUD() throws Exception {
		RuleDefinition rule = createRule();
		updateRule(rule);
        queryRule(rule);
	}

    private void queryRule(RuleDefinition rule) throws Exception {
        ResultActions action = mockMvc.perform(get("/rule/" + rule.getId()).header("authorization", "Bearer " + token)).andExpect(status().isOk());
        String result = action.andReturn().getResponse().getContentAsString();

        log.info(result);

        assertTrue(result.lastIndexOf("success") > 1);
    }

    private void updateRule(RuleDefinition rule) throws Exception {
		rule.setDialPatternType(DialPatternType.Prime3.value());

        ResultActions action = mockMvc.perform(post("/rule/create").header("authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(JsonHelper.toJson(rule)));
        String result = action.andReturn().getResponse().getContentAsString();
        log.info(result);
        assertTrue(result.lastIndexOf("success") > 1);
    }

    private RuleDefinition createRule() throws Exception {
		RuleDefinition rule = new RuleDefinition();
		rule.setDetails("{rule details}");
		rule.setDialPatternType(DialPatternType.Prime2.value());
		rule.setDataSource("datasource");
		rule.setLastUpdated(new Date());
		rule.setLastUpdatedBy("junit");
		rule.setPartitionId(1);
		//TODO add partition list

		ResultActions action = mockMvc.perform(post("/rule/update").header("authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(JsonHelper.toJson(rule)));
		String result = action.andReturn().getResponse().getContentAsString();

		log.info(result);

		assertTrue(result.lastIndexOf("success") > 1);

		rule = JsonHelper.fromJson(result, "data", RuleDefinition.class);
		
		return rule;
	}
	

}
