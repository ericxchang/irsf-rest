package com.iconectiv.irsf.portal.controller;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.core.RangeNdcQueryFilter;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.ListDefintion;
import com.iconectiv.irsf.portal.model.customer.ListDetails;
import com.iconectiv.irsf.portal.model.customer.PartitionDefinition;
import com.iconectiv.irsf.portal.repositories.customer.ListDefinitionRepository;
import com.iconectiv.irsf.util.JsonHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-cfg.xml", "classpath:spring-jpa.xml"})
@WebAppConfiguration
public class MobileIdDatasetControllerTest {
	private static Logger log = LoggerFactory.getLogger(MobileIdDatasetControllerTest.class);
	
	@Autowired WebApplicationContext wac; 
	@Autowired MockHttpSession session;
	@Autowired MockHttpServletRequest request;
	
	private MockMvc mockMvc;
	private static String token;

	private UserDefinition loginUser = new UserDefinition();

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
	public void testQueryIPRN() throws Exception {
		ResultActions action = mockMvc.perform(get("/iprn").header("authorization", "Bearer " + token)).andExpect(status().isOk());
		String result = action.andReturn().getResponse().getContentAsString();
		
		log.info(result);
		
		assertTrue(result.lastIndexOf("success") > 1);
	}

	@Test
	public void testQueryRangeNDC() throws Exception {
		ResultActions action = mockMvc.perform(get("/rangeNDC").header("authorization", "Bearer " + token)).andExpect(status().isOk());
		String result = action.andReturn().getResponse().getContentAsString();
		
		log.info(result);
		
		assertTrue(result.lastIndexOf("success") > 1);
	}
	@Test
	public void testQueryRangeNDCWithFilters() throws Exception {
		ResultActions action = mockMvc.perform(get("/ndc?codeList=51&tosList=U&tosDescList=G,Geographic&providerList=Afghan Telecom").header("authorization", "Bearer " + token)).andExpect(status().isOk());
		String result = action.andReturn().getResponse().getContentAsString();
		
		log.info(result);
		
		assertTrue(result.lastIndexOf("success") > 1);
	}
	@Test
	public void testFindRangeNdcWithFilterOptions() throws Exception {
		List<String> codeList = new ArrayList<String>();
		List<String> iso2List = new ArrayList<String>();
		List<String> tosList = new ArrayList<String>();
		List<String> tosDescList = new ArrayList<String>();
		List<String> providerList = new ArrayList<String>();
		codeList.add("93");
		codeList.add("886");
		iso2List.add("AL");
		//tosList.add("U");
		tosDescList.add("P");
		tosDescList.add("G,Geographic");
		providerList.add("Aircel");
		
		RangeNdcQueryFilter filter =  new RangeNdcQueryFilter();
		filter.setCodeList(codeList); 
		filter.setIso2List(iso2List);
		filter.setProviderList(providerList);
		filter.setTosDescList(tosDescList);		
		filter.setTosList(tosList);
		filter.setPageNo(0);
		filter.setLimit(10);
		
		log.info(JsonHelper.toPrettyJson(filter));
		
		ResultActions action = mockMvc.perform(post("/findRangeNdc").header("authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(JsonHelper.toJson(filter)));
		String result = action.andReturn().getResponse().getContentAsString();
		log.info(result);
		assertTrue(result.lastIndexOf("success") > 1);
	}

}
