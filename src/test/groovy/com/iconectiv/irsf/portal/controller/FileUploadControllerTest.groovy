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
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations=["classpath:spring-cfg.xml", "classpath:spring-jpa.xml"])
@WebAppConfiguration
class FileUploadControllerTest extends GroovyTestCase {
	private static Logger log = LoggerFactory.getLogger(FileUploadControllerTest.class);

	@Autowired WebApplicationContext wac;
	@Autowired MockHttpSession session;
	@Autowired MockHttpServletRequest request;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}


	@Test
	public void testUploadRequests() throws Exception {
		MockMultipartFile firstFile = new MockMultipartFile("file", "blacklist01.txt", "text/plain", "phone=7321010001".getBytes());
		MockMultipartFile secondFile = new MockMultipartFile("file", "blacklist02.txt", "text/plain", "phone=7321010002".getBytes());
		MockMultipartFile thirdFile = new MockMultipartFile("file", "blacklist03.txt", "text/plain", "phone=7321010003".getBytes());
		
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/uploadBlackList")
						.file(firstFile).file(secondFile).file(thirdFile)
						.param("customer", "cust03"));
		sleep(20*1000) 
	}
}
