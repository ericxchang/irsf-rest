package com.iconectiv.irsf.portal.service;

/**
 * Created by echang on 5/29/2017.
 */
import com.iconectiv.irsf.core.ByteFile;
import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.model.common.CustomerDefinition;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.PartitionDataDetails;
import com.iconectiv.irsf.portal.model.customer.PartitionExportHistory;
import com.iconectiv.irsf.portal.repositories.common.CustomerDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.common.UserDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.customer.PartitionDataDetailsRepository;
import com.iconectiv.irsf.portal.repositories.customer.PartitionExportHistoryRepository;
import com.iconectiv.irsf.util.SerializeHelper;
import org.junit.Assert;
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

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-cfg.xml", "classpath:spring-jpa.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})


public class PartitionExportServiceTest {
    private final static Logger log = LoggerFactory.getLogger(PartitionExportServiceTest.class);

    @Autowired
    FileHandlerService fileService;
    @Autowired
    PartitionExportService exportService;
    @Autowired
    PartitionExportHistoryRepository exportRepo;
    @Autowired
    UserDefinitionRepository userRepo;
    @Autowired
    CustomerDefinitionRepository customerRepo;
    @Autowired
    PartitionDataDetailsRepository dataDetailsRepository;

    @Test
    public void testExportToEI() {
        CustomerContextHolder.setSchema("cust01");
        PartitionExportHistory exportEntry = exportRepo.findOne(7);
        UserDefinition loginUser = userRepo.findOneByUserName("user01");
        CustomerDefinition customer = customerRepo.findOne(loginUser.getCustomerId());
        loginUser.setCustomerName(customer.getCustomerName());
        loginUser.setSchemaName(customer.getSchemaName());
        exportService.sendExportFile2EI(loginUser, exportEntry, "https://NJ01APP5058:9433/upload");
    }



    @Test
    public void testSavePartitionFile() {
        CustomerContextHolder.setSchema("cust01");
        PartitionExportHistory exportEntry = exportRepo.findOne(7);
        List<ByteFile> files = new ArrayList<>();
        files.add(new ByteFile("blockfile.csv", exportEntry.getExportFileShort()));
        files.add(new ByteFile("whitelistfile.csv", exportEntry.getExportWhitelist()));
        fileService.saveZipFile("/tmp/irsf-export.zip", files);
    }

    @Test
    public void testSerilizeDeserialize() {
        CustomerContextHolder.setSchema("cust01");
        List<PartitionDataDetails> partitionDataListLong = dataDetailsRepository.findAllByPartitionId(32);

        byte[] data = SerializeHelper.serialize(partitionDataListLong);

        List<PartitionDataDetails> result = (List<PartitionDataDetails>) SerializeHelper.deserialize(data);

        log.info("total size: {}", result.size());
        Assert.assertTrue(partitionDataListLong.size() == result.size());
    }


}
