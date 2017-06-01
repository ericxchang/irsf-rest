package com.iconectiv.irsf.portal.service;

import com.iconectiv.irsf.core.EIResponse;
import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.common.CustomerDefinition;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.PartitionExportHistory;
import org.springframework.scheduling.annotation.Async;

/**
 * Created by echang on 5/28/2017.
 */
public interface PartitionExportService {

    @Async
    void resendPartition(UserDefinition loginUser, Integer exportPartitionId);

    void sendExportFile2EI(UserDefinition loginUser, PartitionExportHistory partHist, String url);

    byte[] createExportFiles(UserDefinition loginUser, PartitionExportHistory partHist, String fileName);

    EIResponse uploadFiles(String uploadFleName, byte[] data, String url, String customer, Integer exportPartitionId);

    void updateStatus(UserDefinition loginUser, EIResponse eiStatus) throws AppException;

    void cleanExportHistory(CustomerDefinition customer);
}
