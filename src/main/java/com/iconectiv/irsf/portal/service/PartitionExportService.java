package com.iconectiv.irsf.portal.service;

import com.iconectiv.irsf.core.EIResponse;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.PartitionExportHistory;

/**
 * Created by echang on 5/28/2017.
 */
public interface PartitionExportService {

	void sendExportFile2EI(UserDefinition loginUser, PartitionExportHistory partHist, String url);

    byte[] createExportFiles(UserDefinition loginUser, PartitionExportHistory partHist, String fileName);

    EIResponse uploadFiles(String uploadFleName, byte[] data, String url);

}
