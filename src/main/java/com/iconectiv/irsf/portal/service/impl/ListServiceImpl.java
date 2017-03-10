package com.iconectiv.irsf.portal.service.impl;

import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.model.customer.ListDefintion;
import com.iconectiv.irsf.portal.model.customer.ListDetails;
import com.iconectiv.irsf.portal.model.customer.ListUploadRequest;
import com.iconectiv.irsf.portal.repositories.customer.ListDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.customer.ListDetailsRepository;
import com.iconectiv.irsf.portal.repositories.customer.ListUploadRequestRepository;
import com.iconectiv.irsf.portal.service.FileHandlerService;
import com.iconectiv.irsf.portal.service.ListService;
import com.iconectiv.irsf.portal.service.ListUploadService;
import com.iconectiv.irsf.portal.util.AppConstants;
import com.iconectiv.irsf.portal.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by echang on 1/11/2017.
 */
@Service
@Transactional
public class ListServiceImpl implements ListService {
    private static Logger log = LoggerFactory.getLogger(ListServiceImpl.class);
    
    @Autowired
    private ListUploadRequestRepository listUploadRepo;
    @Autowired
    private ListDefinitionRepository listRepo;
    @Autowired
    private ListDetailsRepository listDetailRepo;
    @Autowired
	private FileHandlerService fileService;
    @Autowired
    private ListUploadService lstUploadService;
    
    @Transactional
    @Override
    public void processListUploadRequest(ListUploadRequest uploadReq) {
        log.info("Start parsing black list file {}", uploadReq.getId());
        StringBuilder errorList = new StringBuilder();
        List<ListDetails> listEntries = new ArrayList<>();

        
        try {
			lstUploadService.parseBlackWhiteListData(uploadReq, listEntries, errorList);
        	
        	if (errorList.length() > 0) {
        		updateUploadRequestWithErrorMessage(uploadReq, errorList.toString());
        		return;
        	}

        	//TODO batch save
        	CustomerContextHolder.setCustomer(uploadReq.getCustomerName());
        	listDetailRepo.save(listEntries);
        	
        	uploadReq.setStatus(AppConstants.COMPLETE);
            listUploadRepo.save(uploadReq);
            log.info("Complete parsing list file {}", JsonHelper.toJson(uploadReq));
        } catch (Exception e) {
            log.error("Error to parse black list: \n", e);
        }

    }

	@Override
	public Integer createListDefinition(String customer, String listName, String listType, String user) {
		CustomerContextHolder.setCustomer(customer);

		ListDefintion listDefintion = new ListDefintion();
		listDefintion.setListName(listName);
		listDefintion.setType(listType);
		listDefintion.setCustomerName(customer);
		listDefintion.setCreateBy(user);
		listDefintion.setLastUpdatedBy(user);
		listDefintion.setCreateTimestamp(new Date());
		listDefintion.setLastUpdated(new Date());
		listDefintion = listRepo.save(listDefintion);

		return listDefintion.getId();
	}

	public ListUploadRequest createUploadRequest(String customer, String user, ListDefintion listDef, String delimiter) {
    	CustomerContextHolder.setCustomer(customer);
        ListUploadRequest uploadReq = new ListUploadRequest();
        uploadReq.setDelimiter(delimiter);
        uploadReq.setStatus(AppConstants.PROCESS);
        uploadReq.setListRefId(listDef.getId());
        uploadReq.setLastUpdated(new Date());
        uploadReq.setLastUpdatedBy(listDef.getLastUpdatedBy());

        uploadReq = listUploadRepo.save(uploadReq);
		return uploadReq;
	}

	@Override
    public ListDefintion getListDetails(String customer, String listName) {
        CustomerContextHolder.setCustomer(customer);
        ListDefintion listDef = listRepo.findOneByListName(listName);
        listDef.setListUploadRequests(listUploadRepo.findAllByListRefId(listDef.getId()));

        listDef.getListUploadRequests().forEach(uploadReq -> {
            uploadReq.setListDetailsList(listDetailRepo.findAllByUpLoadRefId(uploadReq.getId()));
        });
        return listDef;
    }

    @Override
    @Transactional
    public void deleteListDefinition(String customer, String listName) {
        CustomerContextHolder.setCustomer(customer);
        listRepo.deleteByListName(listName);
        return;
    }


    @Override
	@Transactional
	public ListUploadRequest saveUploadRequest(String customer, ListDefintion listDef, MultipartFile file, String delimiter, String user) {
		ListUploadRequest uploadReq = createUploadRequest(customer, user, listDef, delimiter);
		log.debug(file.getContentType());
		
		if (fileService.getFileSize(file) == 0) {
			String errorMessage = file.getOriginalFilename() + " is empty";
		    log.error(errorMessage);
			updateUploadRequestWithErrorMessage(uploadReq, errorMessage);
			return null;
		}
		
        if (!file.getContentType().equals("text/plain")) {
            String errorMessage = file.getOriginalFilename() + " is NOT ascii file " + file.getContentType();
            log.error(errorMessage);
            updateUploadRequestWithErrorMessage(uploadReq, errorMessage);
            return null;
        }
		
		uploadReq = listUploadRepo.save(uploadReq);
        uploadReq.setCustomerName(customer);
        uploadReq.setData(fileService.getContentAsList(file));
        return uploadReq;
	}

	private void updateUploadRequestWithErrorMessage(ListUploadRequest uploadReq, String data) {
		uploadReq.setErrorData(data);
		uploadReq.setStatus(AppConstants.FAIL);
		uploadReq.setLastUpdated(new Date());
		listUploadRepo.save(uploadReq);
	}
}
