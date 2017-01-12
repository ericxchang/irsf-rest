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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-cfg.xml", "classpath:spring-jpa-test.xml"})
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
    public void testUploadList() throws Exception {
        ResultActions action = mockMvc.perform(get("/uploadBlackList"));
        String result = action.andReturn().getResponse().getContentAsString();

        log.info(result);

        Thread.sleep(30*1000);
    }


}
