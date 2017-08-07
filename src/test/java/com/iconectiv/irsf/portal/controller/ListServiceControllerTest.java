package com.iconectiv.irsf.portal.controller;

import com.iconectiv.irsf.jwt.JWTUtil;
import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.ListDefinition;
import com.iconectiv.irsf.portal.model.customer.ListDetails;
import com.iconectiv.irsf.portal.repositories.customer.ListDefinitionRepository;
import com.iconectiv.irsf.util.JsonHelper;
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

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-cfg.xml", "classpath:spring-jpa.xml"})
@WebAppConfiguration
public class ListServiceControllerTest {
	private static Logger log = LoggerFactory.getLogger(ListServiceControllerTest.class);
	
	@Autowired WebApplicationContext wac; 
	@Autowired MockHttpSession session;
	@Autowired MockHttpServletRequest request;
	
	@Autowired
	private ListDefinitionRepository listDefRepo;
	
	private MockMvc mockMvc;
	private static String token;

	private UserDefinition loginUser = new UserDefinition();

	@Before
	public void setUp() throws Exception {
    	CustomerContextHolder.setSchema("cust03");
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        loginUser = new UserDefinition();
        loginUser.setUserName("user03");
        loginUser.setCustomerId(3);
        loginUser.setRole(PermissionRole.CustAdmin.value());
        loginUser.setSchemaName("cust03");
        loginUser.setCustomerName("Verizon");
		token = JWTUtil.createToken(loginUser);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testQueryTopListRequest() throws Exception {
		ResultActions action = mockMvc.perform(get("/lists/BL").header("authorization", "Bearer " + token)).andExpect(status().isOk());
		String result = action.andReturn().getResponse().getContentAsString();
		
		log.info(result);
		
		assertTrue(result.lastIndexOf("success") > 1);
	}

	@Test
	public void testQueryListDataRequest() throws Exception {
		ResultActions action = mockMvc.perform(get("/listDetail/74").header("authorization", "Bearer " + token)).andExpect(status().isOk());
		String result = action.andReturn().getResponse().getContentAsString();
		
		log.info(result);
		
		assertTrue(result.lastIndexOf("success") > 1);
	}
	
	@Test
	public void testQueryListDataPageRequest() throws Exception {
		ResultActions action = mockMvc.perform(get("/listDetail?id=74&limit=5").header("authorization", "Bearer " + token)).andExpect(status().isOk());
		String result = action.andReturn().getResponse().getContentAsString();
		
		log.info(result);
		
		assertTrue(result.lastIndexOf("success") > 1);
	}
	

	@Test
	public void testQueryListRequest() throws Exception {
		List<ListDefinition> lists = listDefRepo.findTop3ByTypeAndActiveOrderByLastUpdatedDesc("BL", true);
		
		if (lists == null) {
			return;
		}
		
		ListDefinition listDefinition = lists.get(0);

		ResultActions action = mockMvc.perform(get("/list?listId=" + listDefinition.getId()).header("authorization", "Bearer " + token)).andExpect(status().isOk());
		String result = action.andReturn().getResponse().getContentAsString();
		
		log.info(result);
		
		assertTrue(result.lastIndexOf("success") > 1);
	}

	@Test
	public void testInvalidUser() throws Exception {
		ResultActions action = mockMvc.perform(get("/list/1").header("authorization", "Bearer ")).andExpect(status().isForbidden());
		String result = action.andReturn().getResponse().getContentAsString();
		
		log.info(result);
	}
	
	@Test
	public void testGetDialPatternDetail() throws Exception {
		ListDetails listDetail = new ListDetails();
		listDetail.setDialPattern("17326994567");
		listDetail.setNotes("something");
		
        ResultActions action = mockMvc.perform(post("/dialPattern").header("authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(JsonHelper.toJson(listDetail)));
        String result = action.andReturn().getResponse().getContentAsString();
		
		log.info(result);

	}
	
	
	@Test
	public void testInvalidPermission() throws Exception {
		UserDefinition loginUser = new UserDefinition();
		loginUser.setUserName("guiuser01");
		loginUser.setCustomerId(1);
		loginUser.setRole(PermissionRole.API.value());
		token = JWTUtil.createToken(loginUser);
		
		ResultActions action = mockMvc.perform(get("/list/1").header("authorization", "Bearer " + token)).andExpect(status().isForbidden());
		String result = action.andReturn().getResponse().getContentAsString();
		
		log.info(result);

	}
	
	@Test
    public void testAddListEntryRequest() throws Exception {
        CustomerContextHolder.setSchema(loginUser.getSchemaName());
		List<ListDefinition> lists = listDefRepo.findTop3ByTypeAndActiveOrderByLastUpdatedDesc("BL", true);
		
		if (lists == null) {
			return;
		}
		
		ListDefinition listDefinition = lists.get(0);
				
		ListDetails listDetails = new ListDetails();
		listDetails.setListRefId(listDefinition.getId());
		listDetails.setDialPattern("756893456789");
		listDetails.setCustomerDate(new Date());
		listDetails.setReason("add by junit");
		
		ListDetails[] myListRecords = {listDetails};
		
        ResultActions action = mockMvc.perform(post("/listDetails/create").header("authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(JsonHelper.toJson(myListRecords)));
        String result = action.andReturn().getResponse().getContentAsString();
        log.info(result);
        assertTrue(result.lastIndexOf("success") > 1);
    }
	
	@Test
    public void testUploadRequest() throws Exception {
        CustomerContextHolder.setSchema(loginUser.getSchemaName());
		List<ListDefinition> lists = listDefRepo.findTop3ByTypeAndActiveOrderByLastUpdatedDesc("BL", true);
		
		if (lists == null) {
			return;
		}
		
		ListDefinition listDefinition = lists.get(0);
				
		ListDetails listDetails = new ListDetails();
		listDetails.setListRefId(listDefinition.getId());
		listDetails.setDialPattern("756893456789");
		listDetails.setCustomerDate(new Date());
		listDetails.setReason("add by junit");
		
		ListDetails[] myListRecords = {listDetails};
		
        ResultActions action = mockMvc.perform(post("/listDetails/create").header("authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(JsonHelper.toJson(myListRecords)));
        String result = action.andReturn().getResponse().getContentAsString();
        log.info(result);
        assertTrue(result.lastIndexOf("success") > 1);
    }

	
}
