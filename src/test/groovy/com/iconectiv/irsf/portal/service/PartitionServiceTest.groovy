package com.iconectiv.irsf.portal.service

import com.iconectiv.irsf.portal.config.CustomerContextHolder
import com.iconectiv.irsf.portal.core.DialPatternType
import com.iconectiv.irsf.portal.core.PartitionStatus
import com.iconectiv.irsf.portal.exception.AppException
import com.iconectiv.irsf.portal.model.common.CustomerDefinition
import com.iconectiv.irsf.portal.model.common.UserDefinition
import com.iconectiv.irsf.portal.model.customer.PartitionDefinition
import com.iconectiv.irsf.portal.model.customer.RuleDefinition
import com.iconectiv.irsf.portal.repositories.common.CustomerDefinitionRepository
import com.iconectiv.irsf.portal.repositories.common.UserDefinitionRepository
import com.iconectiv.irsf.portal.repositories.customer.PartitionDefinitionRepository
import com.iconectiv.irsf.portal.repositories.customer.RuleDefinitionRepository
import com.iconectiv.irsf.util.DateTimeHelper
import com.iconectiv.irsf.util.JsonHelper
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
    @Autowired
    UserDefinitionRepository userRepo
    @Autowired
    CustomerDefinitionRepository customerRepo
	def schema = "cust01"
	def customer = "junitCust"
	def user = "junit"
	
	@Before
	void before() {
		CustomerContextHolder.setSchema(schema)
	}

	@Test
	void testQueryAllActivePartition() throws Exception {
		CustomerContextHolder.setSchema("cust01")
		def results = service.getAllActivePartitions()
		log.info(JsonHelper.toPrettyJson(results))
		log.info("Total return: " + results.size())
	}
	
	@Test
	void testExportPartitionData() {
		PartitionDefinition partition = createPartition()

        (1..3).each {
			addRule(partition)
		}

		service.exportPartitionData(partition.getId(), user)
	}
	
	private PartitionDefinition createPartition() {
		PartitionDefinition partition = new PartitionDefinition()
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
	
	private void addRule(PartitionDefinition partition) throws AppException {
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

	@Test
	void testRefreshPartition() {
        PartitionDefinition partition = partitionRepo.findOne(26)
        UserDefinition loginUser = userRepo.findOneByUserName("user01")
        CustomerDefinition customer = customerRepo.findOne(loginUser.getCustomerId())
        loginUser.setCustomerName(customer.getCustomerName())
        service.refreshParitionData(loginUser, partition)
	}

}
