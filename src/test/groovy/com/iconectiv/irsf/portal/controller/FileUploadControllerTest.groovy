package com.iconectiv.irsf.portal.controller

import com.iconectiv.irsf.jwt.JWTUtil
import com.iconectiv.irsf.portal.core.PermissionRole
import com.iconectiv.irsf.portal.model.common.UserDefinition
import com.iconectiv.irsf.portal.repositories.customer.ListDefinitionRepository
import com.iconectiv.irsf.util.DateTimeHelper
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
	void testLoadLargeFile() throws Exception {
		def listName = "large-" + DateTimeHelper.formatDate(new Date(), 'yyyyMMddHHmmSS')
		
		def data = this.getClass().getResource('/irsf_blacklist.csv').text
		
		try {
            withPool {
                ["cust03"].eachParallel {
                    log.info("Processing upload request from customer $it")
                    MockMultipartFile firstFile = new MockMultipartFile("file", "blacklist01.txt", "text/plain", data.getBytes())

                    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build()
					def token = createToken(it)
					
                    def action = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/uploadListFile")
                            .file(firstFile)
                            .param("listType", "BL").param("listName",listName).param("listId", '').param("delimiter", ",").header("Authorization", "Bearer " + token))
                    def result = action.andReturn().getResponse().getContentAsString()
                    log.info(result)

                    //assert result.indexOf("success") > 0
                }
            }

            sleep(10 * 1000)
        } finally {
            withPool {
                ["cust03"].eachParallel {
					def token = createToken(it)
                    def action = mockMvc.perform(get("/list/${listName}").header("Authorization", "Bearer " + token)).andExpect(status().isOk())
                    def result = action.andReturn().getResponse().getContentAsString()
                    log.info(result)
                }
            }
        }
    
	}


    @Test
    void testUploadRequests() throws Exception {
		def listName = "junit-BL-" + DateTimeHelper.formatDate(new Date(), 'yyyyMMddHHmmSS')
        try {
            withPool {
                ["cust01", "cust02", "cust03"].eachParallel {
                    log.info("Processing upload request from customer $it")
                    MockMultipartFile firstFile = new MockMultipartFile("file", "blacklist01.txt", "text/plain", "(732)5678901,02212016,partner provided, verizon wireless\n7325678902,02212016,partner provided, verizon wireless".getBytes())
                    MockMultipartFile secondFile = new MockMultipartFile("file", "blacklist02.txt", "text/plain", "(732)5678901,02212016,partner provided, verizon wireless\n7325678902,02212016,partner provided, verizon wireless".getBytes())
                    MockMultipartFile thirdFile = new MockMultipartFile("file", "blacklist03.txt", "text/plain", "(732)5678904,02212016,partner provided, verizon wireless\n7325AB8902,02212016,partner provided, verizon wireless".getBytes())

                    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build()
					def token = createToken(it)
                    def action = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/uploadListFile")
                            .file(firstFile).file(secondFile).file(thirdFile).
                            param("listType", "BL").param("listName",listName).param("listId", '').param("delimiter", ",").header("Authorization", "Bearer " + token))
                    def result = action.andReturn().getResponse().getContentAsString()
                    log.info(result)

                    //assert result.indexOf("success") > 0
                }
            }

            sleep(30 * 1000)
        } finally {
            withPool {
                ["cust01", "cust02", "cust03"].eachParallel {
					def token = createToken(it)
                    def action = mockMvc.perform(get("/list/${listName}").header("Authorization", "Bearer " + token)).andExpect(status().isOk())
                    def result = action.andReturn().getResponse().getContentAsString()
                    log.info(result)

                    action = mockMvc.perform(delete("/list/${listName}").header("Authorization", "Bearer " + token)).andExpect(status().isOk())
                    result = action.andReturn().getResponse().getContentAsString()
                    log.info(result)
                }
            }
        }
    }
	
	def createToken(schemaName) {
		UserDefinition loginUser = new UserDefinition()
		loginUser.setUserName("user-${schemaName}")
		loginUser.setCustomerId(1)
		loginUser.setRole(PermissionRole.CustAdmin.value())
		loginUser.setSchemaName(schemaName)
		loginUser.setCustomerName("customer-${schemaName}")
		
		return JWTUtil.createToken(loginUser)
	}

}
