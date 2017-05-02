package com.iconectiv.irsf.portal.controller;

import com.iconectiv.irsf.jwt.JWTUtil;
import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.model.common.Provider;
import com.iconectiv.irsf.portal.model.common.RangeQueryFilter;
import com.iconectiv.irsf.portal.model.common.TosAndTosDescType;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        loginUser.setUserName("user01");
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
	public void testQueryPremium() throws Exception {
		ResultActions action = mockMvc.perform(get("/premium").header("authorization", "Bearer " + token)).andExpect(status().isOk());
		String result = action.andReturn().getResponse().getContentAsString();
		
		log.info(result);
		
		assertTrue(result.lastIndexOf("success") > 1);
	}
	
	@Test
	public void testQueryProviders() throws Exception {
		ResultActions action = mockMvc.perform(get("/providers").header("authorization", "Bearer " + token)).andExpect(status().isOk());
		String result = action.andReturn().getResponse().getContentAsString();
		
		log.info(result);
		
		assertTrue(result.lastIndexOf("success") > 1);
	}
	

	@Test
	public void testQueryTOS() throws Exception {
		ResultActions action = mockMvc.perform(get("/tos").header("authorization", "Bearer " + token)).andExpect(status().isOk());
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
		List<TosAndTosDescType> tosDescList = new ArrayList<TosAndTosDescType>();
		List<Provider> providerList = new ArrayList<Provider>();
		TosAndTosDescType tosDesc = new TosAndTosDescType();
		Provider prov = new Provider();
		
		codeList.add("93");
		codeList.add("886");
		iso2List.add("AL");
		
		tosDesc.setTos("P");
		tosDescList.add(tosDesc);
		tosDesc = new TosAndTosDescType();
		tosDesc.setTos("G");
		List<String> tdList = new ArrayList<String>();
		tdList.add("Geographic");
		tosDesc.setTosDescs(tdList);
		
		prov.setProvider("Aircel");
		providerList.add(prov);
		
		
		RangeQueryFilter filter =  new RangeQueryFilter();
		filter.setCodeList(codeList); 
		filter.setIso2List(iso2List);
		filter.setProviderList(providerList);
		filter.setTosDescList(tosDescList);		
	

		filter.setPageNo(0);
		filter.setLimit(10);
		
		log.info(JsonHelper.toPrettyJson(filter));
		
		ResultActions action = mockMvc.perform(post("/findRangeNdc").header("authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(JsonHelper.toJson(filter)));
		String result = action.andReturn().getResponse().getContentAsString();
		log.info(result);
		assertTrue(result.lastIndexOf("success") > 1);
	}

	@Test
	public void testFindPremiumWithFilterOptions() throws Exception {
		List<String> codeList = new ArrayList<String>();
		List<String> iso2List = new ArrayList<String>();
		List<String> tosList = new ArrayList<String>();
		List<TosAndTosDescType> tosDescList = new ArrayList<TosAndTosDescType>();
		List<Provider> providerList = new ArrayList<Provider>();
		TosAndTosDescType tosDesc = new TosAndTosDescType();
		Provider prov = new Provider();
		
		codeList.add("247");
		codeList.add("886");
		iso2List.add("AC");
		//tosList.add("U");
		tosDesc.setTos("G");
		tosDescList.add(tosDesc);
		tosDesc = new TosAndTosDescType();
		tosDesc.setTos("S");
		List<String> tdList = new ArrayList<String>();
		tdList.add("Operator Services");
		tosDesc.setTosDescs(tdList);
		tosDescList.add(tosDesc);
		prov.setProvider("Sure South Atlantic Limited");
		providerList.add(prov);
		
		RangeQueryFilter filter =  new RangeQueryFilter();
		filter.setCodeList(codeList); 
		filter.setIso2List(iso2List);
		filter.setProviderList(providerList);
		filter.setTosDescList(tosDescList);		
		filter.setAfterLastObserved("2017-01-01");
		filter.setPageNo(0);
		filter.setLimit(10);
		
		log.info(JsonHelper.toPrettyJson(filter));
		
		ResultActions action = mockMvc.perform(post("/findPremium").header("authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(JsonHelper.toJson(filter)));
		String result = action.andReturn().getResponse().getContentAsString();
		log.info(result);
		assertTrue(result.lastIndexOf("success") > 1);
	}

}
