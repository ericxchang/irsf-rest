package com.iconectiv.irsf.portal.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.iconectiv.irsf.portal.core.AppConstants;
import com.iconectiv.irsf.portal.core.AuditTrailActionDefinition;
import com.iconectiv.irsf.portal.core.EventTypeDefinition;
import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.common.AuditTrail;
import com.iconectiv.irsf.portal.model.common.EventNotification;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.ListDefintion;
import com.iconectiv.irsf.portal.model.customer.ListDetails;
import com.iconectiv.irsf.portal.model.customer.ListUploadRequest;
import com.iconectiv.irsf.portal.repositories.customer.ListDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.customer.ListDetailsRepository;
import com.iconectiv.irsf.portal.repositories.customer.ListUploadRequestRepository;
import com.iconectiv.irsf.portal.service.AuditTrailService;
import com.iconectiv.irsf.portal.service.EventNotificationService;
import com.iconectiv.irsf.portal.service.FileHandlerService;
import com.iconectiv.irsf.portal.service.ListService;
import com.iconectiv.irsf.portal.service.ListUploadService;
import com.iconectiv.irsf.util.JsonHelper;
import com.iconectiv.irsf.util.ListHelper;

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
	private ListDefinitionRepository listDefRepo;
	@Autowired
	private ListDetailsRepository listDetailRepo;
	@Autowired
	private FileHandlerService fileService;
	@Autowired
	private ListUploadService lstUploadService;
	@Autowired
	private EventNotificationService eventService;
	@Autowired
	EntityManagerFactory customerEntityManagerFactory;
	@Autowired
	private AuditTrailService auditService;

	@Transactional
	@Override
	public void processListUploadRequest(ListUploadRequest uploadReq, Boolean isInitialLoading) {
		log.info("Start parsing black list file {}", uploadReq.getId());
		StringBuilder errorList = new StringBuilder();
		List<ListDetails> listEntries = new ArrayList<>();
		
		try {
			lstUploadService.parseBlackWhiteListData(uploadReq, listEntries, errorList);

			AuditTrail audit = new AuditTrail();
			audit.setAction("upload list");
			audit.setUserName(uploadReq.getLastUpdatedBy());
			audit.setCustomerName(uploadReq.getCustomerName());
			
			Map<String, String> auditDetail = new LinkedHashMap<>();
			auditDetail.put("file name", uploadReq.getFileName());
			auditDetail.put("list type", uploadReq.getListDefintion().getType());
			auditDetail.put("list name", uploadReq.getListDefintion().getListName());
			auditDetail.put("initial load", isInitialLoading.toString());
			
			if (errorList.length() > 0) {
				updateUploadRequestWithErrorMessage(uploadReq, errorList.toString());
				auditDetail.put("status", AppConstants.FAIL);
				auditService.saveAuditTrailLog(audit, auditDetail);
                uploadReq.setStatus(AppConstants.FAIL);
                listUploadRepo.save(uploadReq);
				return;
			}

			uploadReq.setStatus(AppConstants.COMPLETE);
			listUploadRepo.save(uploadReq);

            listDetailRepo.batchUpdate(listEntries);

			auditDetail.put("size", String.valueOf(listEntries.size()));
			auditDetail.put("status", AppConstants.COMPLETE);
			auditService.saveAuditTrailLog(audit, auditDetail);

			
			EventNotification event = new EventNotification();
			event.setCustomerName(uploadReq.getCustomerName());
			event.setEventType(EventTypeDefinition.List_Update.value());
			event.setReferenceId(uploadReq.getListRefId());
			event.setMessage("upload new " + uploadReq.getListDefintion().getType() + " list");
			event.setCreateTimestamp(new Date());
			event.setLastUpdatedBy(uploadReq.getLastUpdatedBy());
			event.setStatus("new");
			eventService.addEventNotification(event);

			log.info("Complete parsing list file {}", JsonHelper.toJson(uploadReq));
		} catch (Exception e) {
			log.error("Error to parse black list: \n", e);
		}

	}

	@Override
	public Integer createListDefinition(UserDefinition user, String listName, String listType) {
		ListDefintion listDefintion = new ListDefintion();
		listDefintion.setListName(listName);
		listDefintion.setType(listType);
		listDefintion.setActive(true);
		listDefintion.setCustomerName(user.getCustomerName());
		listDefintion.setCreateBy(user.getUserName());
		listDefintion.setLastUpdatedBy(user.getUserName());
		listDefintion.setCreateTimestamp(new Date());
		listDefintion.setLastUpdated(new Date());
		listDefintion = listDefRepo.save(listDefintion);

		return listDefintion.getId();
	}

	public ListUploadRequest createUploadRequest(ListDefintion listDef, String fileName, String delimiter) {
		if (delimiter == null) {
			delimiter = "|";
		}
		
		ListUploadRequest uploadReq = new ListUploadRequest();
		uploadReq.setFileName(fileName);
		uploadReq.setDelimiter(delimiter);
		uploadReq.setStatus(AppConstants.PROCESS);
		uploadReq.setListRefId(listDef.getId());
		uploadReq.setLastUpdated(new Date());
		uploadReq.setLastUpdatedBy(listDef.getLastUpdatedBy());

		uploadReq = listUploadRepo.save(uploadReq);
		return uploadReq;
	}

	@Override
	public ListDefintion getListDetails(String listName) {
		ListDefintion listDef = listDefRepo.findOneByListName(listName);

		if (listDef == null) {
			log.warn("list {} does not exist", listName);
			return null;
		}
		listDef.setListUploadRequests(listUploadRepo.findAllByListRefIdOrderByLastUpdatedDesc(listDef.getId()));

		listDef.getListUploadRequests().forEach(uploadReq -> {
			uploadReq.setListDetailsList(listDetailRepo.findAllByUpLoadRefId(uploadReq.getId()));
		});
		return listDef;
	}

	@Override
	public ListDefintion getListDetails(int listId) {
		ListDefintion listDef = listDefRepo.findOne(listId);

		if (listDef == null) {
			log.warn("list {} does not exist", listId);
			return null;
		}
		listDef.setListUploadRequests(listUploadRepo.findAllByListRefIdOrderByLastUpdatedDesc(listDef.getId()));

		listDef.getListUploadRequests().forEach(uploadReq -> {
			uploadReq.setListDetailsList(listDetailRepo.findAllByUpLoadRefId(uploadReq.getId()));
		});
		return listDef;
	}

	@Override
	@Transactional
	public void deleteListDefinition(String listName) {
		listDefRepo.deleteByListName(listName);
		return;
	}

	@Override
	@Transactional
	public void deleteListDefinition(int listId) {
		listDefRepo.delete(listId);
		return;
	}

	@Override
	@Transactional
	public ListUploadRequest saveUploadRequest(UserDefinition user, ListDefintion listDef, MultipartFile file, String delimiter) {
		ListUploadRequest uploadReq = createUploadRequest(listDef, file.getOriginalFilename(), delimiter);
		
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

		uploadReq.setCustomerName(user.getCustomerName());
		uploadReq.setData(fileService.getContentAsList(file));
		return uploadReq;
	}

	private void updateUploadRequestWithErrorMessage(ListUploadRequest uploadReq, String data) {
		uploadReq.setErrorData(data);
		uploadReq.setStatus(AppConstants.FAIL);
		uploadReq.setLastUpdated(new Date());
		listUploadRepo.save(uploadReq);
	}

	@Transactional
	@Override
	public void saveListEntry(UserDefinition loginUser, ListDetails listDetail) throws AppException {
		if (log.isDebugEnabled()) log.debug("Adding new list entry {}", JsonHelper.toJson(listDetail));
		
		try {
			listDetail.setActive(true);
			listDetail.setLastUpdatedBy(loginUser.getUserName());
			listDetail.setLastUpdated(new Date());
			
			listDetailRepo.save(listDetail);

			Map<String, String> auditDetail = new LinkedHashMap<>();
			auditDetail.put("dial pattern", listDetail.getDialPattern());
			auditDetail.put("list Id", listDetail.getListRefId().toString());
			auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Update_List_Entry, auditDetail);

			
			EventNotification event = new EventNotification();
			event.setCustomerName(loginUser.getCustomerName());
			event.setEventType(EventTypeDefinition.List_Update.value());
			event.setReferenceId(listDetail.getListRefId());
			event.setMessage("update list entry");
			event.setCreateTimestamp(new Date());
			event.setLastUpdatedBy(loginUser.getLastUpdatedBy());
			event.setStatus("new");
			eventService.addEventNotification(event);
		} catch (Exception e) {
			log.error("Error to add list entry: \n", e);
			throw new AppException(e.getMessage());
		}

	}

	@Override
	public List<ListDetails> getListDetailDataByListId(int listId) {
		List<ListDetails> dataList = new ArrayList<>();
		
		for (Object[] row : listDetailRepo.findAllDetailsByListRefId(listId)) {
			dataList.add( ListHelper.convertToListDetail(row) );
		}
		
		return dataList;
	}

	@Override
	public List<ListDetails> getListDetailDataByUploadId(int uploadId) {
		List<ListDetails> dataList = new ArrayList<>();
		
		for (Object[] row : listDetailRepo.findAllDetailsByUpLoadRefId(uploadId)) {
			dataList.add( ListHelper.convertToListDetail(row) );
		}
		
		return dataList;
	}

	@Override
	public List<ListDefintion> getTop3ListDefinition(String listType) {
		List<ListDefintion> listDefinitionData = listDefRepo.findTop3ByTypeAndActiveOrderByLastUpdatedDesc(listType, true);
		
		for (ListDefintion listDefinition : listDefinitionData) {
			listDefinition.setListUploadRequests(listUploadRepo.findAllByListRefIdOrderByLastUpdatedDesc(listDefinition.getId()));
		}
		return listDefinitionData;
	}
}
