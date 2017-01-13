package com.iconectiv.irsf.portal.controller;

/**
 * Created by echang on 1/11/2017.
 */
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-cfg.xml", "classpath:spring-jpa.xml"})
@WebAppConfiguration
public class ListServiceControllerTest {
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
    	List<String> customers = new ArrayList<>(Arrays.asList("cust01", "cust02", "cust03"));
    	
    	customers.stream().parallel().forEach(customer -> {
            ResultActions action;
			try {
				action = mockMvc.perform(get("/uploadBlackList/" + customer));
	            String result = action.andReturn().getResponse().getContentAsString();
	            log.info(result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    	});
        Thread.sleep(30*1000);
    }


}
