package com.iconectiv.irsf.portal.service

import com.iconectiv.irsf.portal.config.CustomerContextHolder
import com.iconectiv.irsf.portal.core.DialPatternType
import com.iconectiv.irsf.portal.core.PartitionStatus
import com.iconectiv.irsf.portal.exception.AppException
import com.iconectiv.irsf.portal.model.customer.PartitionDefintion
import com.iconectiv.irsf.portal.model.customer.RuleDefinition
import com.iconectiv.irsf.portal.repositories.customer.PartitionDefinitionRepository
import com.iconectiv.irsf.portal.repositories.customer.RuleDefinitionRepository
import com.iconectiv.irsf.portal.util.DateTimeHelper
import com.iconectiv.irsf.portal.util.JsonHelper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = ["classpath:spring-cfg.xml", "classpath:spring-jpa.xml"])
@WebAppConfiguration
 class PartitionServiceTest {
	private static Logger log = LoggerFactory.getLogger(PartitionServiceTest.class)
	@Autowired
	PartitionService service
	@Autowired
	RuleDefinitionRepository ruleRepo
	@Autowired
	PartitionDefinitionRepository partitionRepo
	
	private String customer = "cust01"
	private String user = "junit"
	
	@Before
	void before() {
		CustomerContextHolder.setCustomer(customer)
	}
	
	@Test
	void testExportPartitionData() {
		PartitionDefintion partition = createPartition()

        (1..3).each {
			addRule(partition)
		}

		service.exportPartitionData(partition.getId(), user)
	}
	
	private PartitionDefintion createPartition() {
		PartitionDefintion partition = new PartitionDefintion()
		partition.setCustomerName(customer)
		partition.setName("junit-part-" + DateTimeHelper.formatDate(new Date(), "yyyyMMddSS"))
		partition.setDescription("my parittion for junit")
		partition.setDraftDate(new Date())
		partition.setLastUpdated(new Date())
		partition.setLastUpdatedBy("junit")
		partition.setStatus(PartitionStatus.Draft.value())
		partitionRepo.save(partition)
		log.info(JsonHelper.toPrettyJson(partition))
		
		return partition
	}
	
	private void addRule(PartitionDefintion partition) throws AppException {
		RuleDefinition rule = new RuleDefinition()
		rule.setPartitionId(partition.getId())
		rule.setActive(true)
		rule.setDetails("{key:value}")
		rule.setCreatedBy("junit")
		rule.setCreateTimestamp(new Date())
		rule.setDataSource("range")
		rule.setLastUpdated(new Date())
		rule.setLastUpdatedBy("junit")
		rule.setDialPatternType(DialPatternType.Prime2.value())
		
		ruleRepo.save(rule)
		log.info("create new rule: " + JsonHelper.toJson(rule))
		service.addRule(partition, rule, user)
	}

}
