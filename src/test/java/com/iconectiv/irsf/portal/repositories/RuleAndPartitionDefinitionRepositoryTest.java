package com.iconectiv.irsf.portal.repositories;

import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.core.DialPatternType;
import com.iconectiv.irsf.portal.core.PartitionStatus;
import com.iconectiv.irsf.portal.model.customer.PartitionDefinition;
import com.iconectiv.irsf.portal.model.customer.RuleDefinition;
import com.iconectiv.irsf.portal.repositories.customer.PartitionDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.customer.RuleDefinitionRepository;
import com.iconectiv.irsf.util.DateTimeHelper;
import com.iconectiv.irsf.util.JsonHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.Date;

import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-cfg.xml", "classpath:spring-jpa.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})

public class RuleAndPartitionDefinitionRepositoryTest {
	private static Logger log = LoggerFactory.getLogger(RuleAndPartitionDefinitionRepositoryTest.class);
	@Autowired
	RuleDefinitionRepository ruleRepo;
	@Autowired
	PartitionDefinitionRepository partitionRepo;
	

	private PartitionDefinition createPartition() {
		PartitionDefinition partition = new PartitionDefinition();
		partition.setCustomerName("junitcust01");
		partition.setName("junit-part-" + DateTimeHelper.formatDate(new Date(), "yyyyMMddSS"));
		partition.setDescription("my parittion for junit");
		partition.setDraftDate(new Date());
		partition.setLastUpdated(new Date());
		partition.setLastUpdatedBy("junit");
		partition.setRuleIds("1,2");
		partition.setStatus(PartitionStatus.Draft.value());
		partitionRepo.save(partition);
		log.info(JsonHelper.toPrettyJson(partition));
		
		return partition;
	}
	

	@Test
	public void testAdAndDeleteRuleDefinition() {
    	CustomerContextHolder.setSchema("cust01");
		PartitionDefinition partition = createPartition();
		
		RuleDefinition rule = new RuleDefinition();
		rule.setPartitionId(partition.getId());
		rule.setActive(true);
		rule.setDetails("{key:value}");
		rule.setCreatedBy("junit");
		rule.setCreateTimestamp(new Date());
		rule.setDataSource("range");
		rule.setLastUpdated(new Date());
		rule.setLastUpdatedBy("junit");
		rule.setDialPatternType(DialPatternType.Prime2.value());
		
		ruleRepo.save(rule);
		log.info(JsonHelper.toPrettyJson(rule));
		
		partitionRepo.delete(partition.getId());
		
		assertNull(ruleRepo.findOne(rule.getId()));
		
	}

}
