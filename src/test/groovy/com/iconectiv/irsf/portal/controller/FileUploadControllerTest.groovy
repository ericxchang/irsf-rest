package com.iconectiv.irsf.portal.controller

import com.iconectiv.irsf.portal.repositories.customer.ListDefinitionRepository
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

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
    @Autowired
    ListDefinitionRepository listDefRepo

    private MockMvc mockMvc

    @Before
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
    }


    @Test
    void testUploadRequests() throws Exception {
        try {
            withPool {
                ["cust01", "cust02", "cust03"].eachParallel {
                    log.info("Processing upload request from customer $it")
                    MockMultipartFile firstFile = new MockMultipartFile("file", "blacklist01.txt", "text/plain", "(732)5678901,02212016,partner provided, verizon wireless\n7325678902,02212016,partner provided, verizon wireless".getBytes())
                    MockMultipartFile secondFile = new MockMultipartFile("file", "blacklist02.txt", "text/plain", "(732)5678903,02212016,partner provided, verizon wireless\n7325678904,02212016,partner provided, verizon wireless".getBytes())
                    MockMultipartFile thirdFile = new MockMultipartFile("file", "blacklist03.txt", "text/plain", "(732)5678901,02212016,partner provided, verizon wireless\n7325AB8902,02212016,partner provided, verizon wireless".getBytes())

                    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build()

                    def action = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/uploadListFile")
                            .file(firstFile).file(secondFile).file(thirdFile)
                            .param("customer", it).param("listType", "BL").param("listName", "junit-blackList-01").param("listId", '').param("delimiter", ","))
                    def result = action.andReturn().getResponse().getContentAsString()
                    log.info(result)

                    //assert result.indexOf("success") > 0
                }
            }

            sleep(10 * 1000)
        } finally {
            withPool {
                ["cust01", "cust02", "cust03"].eachParallel {
                    def action = mockMvc.perform(get("/list/${it}/junit-blackList-01")).andExpect(status().isOk())
                    def result = action.andReturn().getResponse().getContentAsString()
                    log.info(result)

                    action = mockMvc.perform(delete("/list/delete/${it}/junit-blackList-01")).andExpect(status().isOk())
                    result = action.andReturn().getResponse().getContentAsString()
                    log.info(result)
                }
            }
        }
    }
}
