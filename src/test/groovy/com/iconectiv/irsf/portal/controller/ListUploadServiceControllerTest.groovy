package com.iconectiv.irsf.portal.controller

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;;;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations=["classpath:spring-cfg.xml", "classpath:spring-jpa.xml"])
@WebAppConfiguration
class ListUploadServiceControllerTest extends GroovyTestCase {
	private static Logger log = LoggerFactory.getLogger(ListServiceControllerTest.class);

	@Autowired WebApplicationContext wac;
	@Autowired MockHttpSession session;
	@Autowired MockHttpServletRequest request;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}


	@Test
	public void testMultiUploadRequests() throws Exception {
		["cust01", "cust02", "cust03"].parallelStream().each ({
			def action = mockMvc.perform(get("/uploadBlackList/" + it));
			def result = action.andReturn().getResponse().getContentAsString();
			log.info(result)
			assert result.indexOf("success") > 0
		});
	
		sleep(20*1000) 
	}
}
