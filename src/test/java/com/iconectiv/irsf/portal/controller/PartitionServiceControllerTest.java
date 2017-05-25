package com.iconectiv.irsf.portal.controller;

import com.iconectiv.irsf.jwt.JWTUtil;
import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.core.PartitionStatus;
import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.model.common.RangeQueryFilter;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.PartitionDefinition;
import com.iconectiv.irsf.portal.model.customer.RuleDefinition;
import com.iconectiv.irsf.portal.repositories.customer.PartitionDefinitionRepository;
import com.iconectiv.irsf.util.DateTimeHelper;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-cfg.xml", "classpath:spring-jpa.xml"})
@WebAppConfiguration
public class PartitionServiceControllerTest  {
	private static Logger log = LoggerFactory.getLogger(PartitionServiceControllerTest.class);
	
	@Autowired WebApplicationContext wac; 
	@Autowired MockHttpSession session;
	@Autowired MockHttpServletRequest request;
	
	@Autowired PartitionDefinitionRepository partitionRepo;
	
	private MockMvc mockMvc;
	private UserDefinition loginUser;
	private String token;

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
        CustomerContextHolder.setSchema(loginUser.getSchemaName());
    }

	@After
	public void tearDown() throws Exception {

	}

	//@Test
	public void testQueryActivePartition() throws Exception {
        ResultActions action = mockMvc.perform(get("/partitions").header("authorization", "Bearer " + token)).andExpect(status().isOk());
        String result = action.andReturn().getResponse().getContentAsString();
        log.info(result);
        Map<String, Object>  rv = JsonHelper.fromJson(result, Map.class);
        if (rv != null) {
        	log.info("rv: {}", rv.toString());
        	Set<String> k = rv.keySet();
        	Iterator it = k.iterator();
        	while(it.hasNext()) {
        		String key = (String) it.next();
        		//log.info(key);
        		log.info("key: '{}', value: {}", key, rv.get(key).toString());
        		if (key.equals("data")) {
          			List<PartitionDefinition> partition =  JsonHelper.fromJson(rv.get(key), List.class);
          			log.info(JsonHelper.toPrettyJson(partition));
        		}
        			
        	}
        }
  
	}

	//@Test
	public void testPartitionCRUD() throws Exception {
		PartitionDefinition partition = createPartition();
		updatePartition(partition);
        refrehPartition(partition);
        queryPartition(partition);
	}
	@Test
	public void testQueryPartition() throws Exception {
			PartitionDefinition partition = partitionRepo.findOne(new Integer(16));
	        queryPartition(partition);
	}

	@Test
	public void testRefreshPartitionwithRules() throws Exception {
		Integer partId = 20;
		log.info("testExportPartition(): URL:  {}", ("/partition/refresh/" + partId));
		ResultActions action = mockMvc.perform(get("/partition/refresh/" + partId).header("authorization", "Bearer " + token));

		String result = action.andReturn().getResponse().getContentAsString();

		log.info("test refresh partition(): {}", result);

		Thread.sleep(5 * 1000);

	}
	@Test
	public void testExportPartition() throws Exception {
    	Integer partId = 20;
    	 log.info("testExportPartition(): URL:  {}", ("/partition/export/" + partId));
    	ResultActions action = mockMvc.perform(get("/partition/export/" + partId).header("authorization", "Bearer " + token));
    	
    	//ResultActions action = mockMvc.perform(get("/partition/export/" + partId).header("authorization", "Bearer " + token)).andExpect(status().isOk());
        String result = action.andReturn().getResponse().getContentAsString();

        log.info("testExportPartition(): {}", result);
        
		Thread.sleep(2 * 1000);
    }
	
	@Test
	public void testResendPartition() throws Exception {
    	Integer partId = 16;
    	 log.info("testResendPartition(): URL:  {}", ("/partition/resend/" + partId));
    	ResultActions action = mockMvc.perform(get("/partition/resend/" + partId).header("authorization", "Bearer " + token));
    	
    	//ResultActions action = mockMvc.perform(get("/partition/export/" + partId).header("authorization", "Bearer " + token)).andExpect(status().isOk());
        String result = action.andReturn().getResponse().getContentAsString();

        log.info("testExportPartition(): {}", result);
        
		Thread.sleep(2 * 1000);
    }
  
    private void queryPartition(PartitionDefinition partition) throws Exception {
        ResultActions action = mockMvc.perform(get("/partition/" + partition.getId()).header("authorization", "Bearer " + token)).andExpect(status().isOk());
        String result = action.andReturn().getResponse().getContentAsString();

        log.info(result);

        assertTrue(result.lastIndexOf("success") > 1);
    }
    private PartitionDefinition queryPartition(Integer partId) throws Exception {
    	PartitionDefinition partition = null;
    	ResultActions action = mockMvc.perform(get("/partition/" + partId).header("authorization", "Bearer " + token)).andExpect(status().isOk());
        String result = action.andReturn().getResponse().getContentAsString();

        log.info(result);
        Map<String, Object>  rv = JsonHelper.fromJson(result, Map.class);
        log.info(""+ rv.containsKey("data"));
        if (rv != null) {
        	log.info("rv: {}", rv.toString());
        	Set<String> k = rv.keySet();
        	Iterator it = k.iterator();
        	while(it.hasNext()) {
        		String key = (String) it.next();
        		//log.info(key);
        		log.info("key: '{}', value: {}", key, rv.get(key).toString());
        		if (key.equals("data")) {
          			partition =  JsonHelper.fromJson(rv.get(key), PartitionDefinition.class);
          			log.info(JsonHelper.toPrettyJson(partition));
        		}
        			
        	}
        }
        return partition;
    }


    private void refrehPartition(PartitionDefinition partition) throws Exception {
        ResultActions action = mockMvc.perform(post("/partition/refresh").header("authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(JsonHelper.toJson(partition)));
        String result = action.andReturn().getResponse().getContentAsString();
        log.info(result);
        assertTrue(result.lastIndexOf("success") > 1);
    }
    private void refrehPartition(Integer partitionId) throws Exception {
        ResultActions action = mockMvc.perform(get("/partition/refresh/" + partitionId).header("authorization", "Bearer " + token)).andExpect(status().isOk());
        String result = action.andReturn().getResponse().getContentAsString();
        log.info(result);
        assertTrue(result.lastIndexOf("success") > 1);
    }
    
    private void updatePartition(PartitionDefinition partition) throws Exception {
        partition.setRuleIds("2");

        ResultActions action = mockMvc.perform(post("/partition/save").header("authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(JsonHelper.toJson(partition)));
        String result = action.andReturn().getResponse().getContentAsString();
        log.info(result);
        assertTrue(result.lastIndexOf("success") > 1);
    }

    private PartitionDefinition createPartition() throws Exception {
		PartitionDefinition partition = new PartitionDefinition();
		partition.setCustomerName("junitcust01");
		partition.setName("junit-part-" + DateTimeHelper.formatDate(new Date(), "yyyyMMddSS"));
		partition.setDescription("my parittion for junit");
		partition.setStatus(PartitionStatus.Fresh.value());
		partition.setLastUpdated(new Date());
		partition.setLastUpdatedBy("junit");

		ResultActions action = mockMvc.perform(post("/partition/save").header("authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).content(JsonHelper.toJson(partition)));
		String result = action.andReturn().getResponse().getContentAsString();

		log.info(result);

		assertTrue(result.lastIndexOf("success") > 1);

		partition = JsonHelper.fromJson(result, "data", PartitionDefinition.class);
		
		return partition;
	}
	

}
