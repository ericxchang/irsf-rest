package com.iconectiv.irsf.portal.repositories;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Before;
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

import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.core.PartitionStatus;
import com.iconectiv.irsf.portal.model.customer.PartitionDefinition;
import com.iconectiv.irsf.portal.repositories.customer.PartitionDefinitionRepository;
import com.iconectiv.irsf.util.DateTimeHelper;
import com.iconectiv.irsf.util.JsonHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-cfg.xml", "classpath:spring-jpa.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})

public class PartitionDefinitionRepositoryTest {
	private static Logger log = LoggerFactory.getLogger(PartitionDefinitionRepositoryTest.class);
	@Autowired
	PartitionDefinitionRepository partitionRepo;
	
	@Before
	public void setUp() throws Exception {
    	CustomerContextHolder.setSchema("cust01");
	}
	
	@Test
	public void testFindByOrigPartitionId() {
		List<PartitionDefinition> partitions = partitionRepo.findAllByOrigPartitionIdOrderByIdDesc(1);
		log.info(JsonHelper.toPrettyJson(partitions));
	}

	@Test
	public void testStalepartitionByBL() {
		PartitionDefinition partition = createPartition();
		partitionRepo.staleDraftPartitionsByBListId(1000);
		partition = partitionRepo.findOne(partition.getId());
		log.debug(JsonHelper.toJson(partition));
		partitionRepo.delete(partition);

		assertTrue(partition.getStatus().equals(PartitionStatus.Stale.value()));
		assertTrue(partition.getDraftDate() == null);
	}

	@Test
	public void testStalepartitionByWL() {
		PartitionDefinition partition = createPartition();
		partitionRepo.staleDraftPartitionsByWListId(1001);
		partition = partitionRepo.findOne(partition.getId());
		log.debug(JsonHelper.toJson(partition));
		partitionRepo.delete(partition);

		assertTrue(partition.getStatus().equals(PartitionStatus.Stale.value()));
		assertTrue(partition.getDraftDate() == null);
	}

	@Test
	public void testStalepartitionByRule() {
		PartitionDefinition partition = createPartition();
		partitionRepo.staleDraftPartitionsByRuleId(2);
		partition = partitionRepo.findOne(partition.getId());
		log.debug(JsonHelper.toJson(partition));
		partitionRepo.delete(partition);

		assertTrue(partition.getStatus().equals(PartitionStatus.Stale.value()));
		assertTrue(partition.getDraftDate() == null);
	}

	private PartitionDefinition createPartition() {
		PartitionDefinition partition = new PartitionDefinition();
		partition.setCustomerName("junitcust01");
		partition.setName("junit-part-" + DateTimeHelper.formatDate(new Date(), "yyyyMMddSS"));
		partition.setDescription("my parittion for junit");
		partition.setDraftDate(new Date());
		partition.setLastUpdated(new Date());
		partition.setLastUpdatedBy("junit");
		partition.setBlId(1000);
		partition.setWlId(1001);
		partition.setRuleIds("1,2");
		partition.setStatus(PartitionStatus.Draft.value());
		partitionRepo.save(partition);
		log.info(JsonHelper.toPrettyJson(partition));
		
		return partition;
	}
}