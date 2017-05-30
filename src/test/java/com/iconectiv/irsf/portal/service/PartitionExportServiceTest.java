package com.iconectiv.irsf.portal.service;

/**
 * Created by echang on 5/29/2017.
 */
import com.iconectiv.irsf.core.ByteFile;
import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.model.common.CustomerDefinition;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.PartitionExportHistory;
import com.iconectiv.irsf.portal.repositories.common.CustomerDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.common.UserDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.customer.PartitionExportHistoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
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


}
