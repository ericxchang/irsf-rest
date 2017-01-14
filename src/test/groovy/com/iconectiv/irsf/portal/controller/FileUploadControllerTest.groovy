package com.iconectiv.irsf.portal.controller

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpSession
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

import static groovyx.gpars.GParsPool.withPool

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = ["classpath:spring-cfg.xml", "classpath:spring-jpa.xml"])
@WebAppConfiguration
class FileUploadControllerTest extends GroovyTestCase {
    private static Logger log = LoggerFactory.getLogger(FileUploadControllerTest.class)

    @Autowired
    WebApplicationContext wac
    @Autowired
    MockHttpSession session
    @Autowired
    MockHttpServletRequest request

    private MockMvc mockMvc

    @Before
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
    }


    @Test
    void testUploadRequests() throws Exception {
        withPool {
            ["cust01", "cust02", "cust03"].eachParallel {
                log.info("Processing upload request from customer $it")
                MockMultipartFile firstFile = new MockMultipartFile("file", "blacklist01.txt", "text/plain", "country=usa, phone=7321010001\ncountry=usa, phone=7321010002".getBytes())
                MockMultipartFile secondFile = new MockMultipartFile("file", "blacklist02.txt", "text/plain", "country=usa, phone=7321010002".getBytes())
                MockMultipartFile thirdFile = new MockMultipartFile("file", "blacklist03.txt", "text/plain", "country=usa, phone=7321010003".getBytes())
                MockMultipartFile fourthFile = new MockMultipartFile("file", "blacklist04.txt", "text/plain", "".getBytes())

                MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build()
                def action = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/uploadBlackList")
                        .file(firstFile).file(secondFile).file(thirdFile).file(fourthFile)
                        .param("customer", it))
                def result = action.andReturn().getResponse().getContentAsString()
                log.info(result)
                assert result.indexOf("success") > 0
            }
        }

        sleep(60 * 1000)
    }
}
