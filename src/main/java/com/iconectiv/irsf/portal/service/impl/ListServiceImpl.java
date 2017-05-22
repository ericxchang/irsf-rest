package com.iconectiv.irsf.portal.service.impl;

import com.iconectiv.irsf.portal.core.AppConstants;
import com.iconectiv.irsf.portal.core.AuditTrailActionDefinition;
import com.iconectiv.irsf.portal.core.EventTypeDefinition;
import com.iconectiv.irsf.portal.core.MessageDefinition;
import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.common.AuditTrail;
import com.iconectiv.irsf.portal.model.common.EventNotification;
import com.iconectiv.irsf.portal.model.common.RangeNdc;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.ListDefinition;
import com.iconectiv.irsf.portal.model.customer.ListDetails;
import com.iconectiv.irsf.portal.model.customer.ListUploadRequest;
import com.iconectiv.irsf.portal.repositories.common.RangeNdcRepository;
import com.iconectiv.irsf.portal.repositories.customer.ListDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.customer.ListDetailsRepository;
import com.iconectiv.irsf.portal.repositories.customer.ListUploadRequestRepository;
import com.iconectiv.irsf.portal.service.*;
import com.iconectiv.irsf.util.JsonHelper;
import com.iconectiv.irsf.util.ListHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManagerFactory;
import java.util.*;

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
	@Autowired
	private MobileIdDataService midDataService;
	@Autowired
	private RangeNdcRepository rangeRepo;
	
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
		ListDefinition listDefintion = new ListDefinition();
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

	public ListUploadRequest createUploadRequest(ListDefinition listDef, String fileName, String delimiter) {
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
	public void updateListName(UserDefinition loginUser, Integer listId, String listName) {
		ListDefinition listDef = listDefRepo.findOne(listId);
		
		if (listDef != null && !listDef.getListName().equals(listName)) {
			listDef.setListName(listName);
			listDef.setLastUpdated(new Date());
			listDef.setLastUpdatedBy(loginUser.getUserName());
			listDefRepo.save(listDef);
			
			auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Update_List_Definition, "rename list " + listId + " to " + listName);
		}
		
	}
	
	@Override
	public ListDefinition getListDetails(String listName) {
		ListDefinition listDef = listDefRepo.findOneByListName(listName);

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
	public ListDefinition getListDetails(int listId) {
		ListDefinition listDef = listDefRepo.findOne(listId);

		if (listDef == null) {
			log.warn("list {} does not exist", listId);
			return null;
		}
		listDef.setListUploadRequests(listUploadRepo.findAllByListRefIdOrderByLastUpdatedDesc(listDef.getId()));

		listDef.getListUploadRequests().forEach(uploadReq -> {
			uploadReq.setListDetailsList(listDetailRepo.findAllByUpLoadRefId(uploadReq.getId()));
		});
		
		listDef.setListSize(listDetailRepo.getListSizeByListId(listDef.getId()));
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
		ListDefinition listDef = listDefRepo.findOne(listId);
		
		if (listDef != null) {
			listUploadRepo.deleteAllByListRefId(listDef.getId());
			listDetailRepo.deleteAllByListRefId(listDef.getId());
		}
		return;
	}


	@Override
	@Transactional
	public ListUploadRequest saveUploadRequest(UserDefinition user, ListDefinition listDef, MultipartFile file, String delimiter) {
		ListUploadRequest uploadReq = createUploadRequest(listDef, file.getOriginalFilename(), delimiter);
		
		log.debug(file.getContentType());

		if (fileService.getFileSize(file) == 0) {
			String errorMessage = file.getOriginalFilename() + " is empty";
			log.error(errorMessage);
			updateUploadRequestWithErrorMessage(uploadReq, errorMessage);
			return null;
		}

		if (!AppConstants.UploadFileType.contains( file.getContentType() )) {
			String errorMessage = file.getOriginalFilename() + " is NOT ascii file " + file.getContentType();
			log.error(errorMessage);
			updateUploadRequestWithErrorMessage(uploadReq, errorMessage);
			return null;
		}

		uploadReq = listUploadRepo.save(uploadReq);

		uploadReq.setCustomerName(user.getCustomerName());
		uploadReq.setData(fileService.getContentAsList(file));
		
		//check list size
		int currentListSize = 0;
		if (listDef.getId() != null) {
			currentListSize = listDetailRepo.getListSizeByListId(listDef.getId());
		}
		if (currentListSize + uploadReq.getData().size() > maxListSize) {
			updateUploadRequestWithErrorMessage(uploadReq, MessageDefinition.ListSizeOverLimitError + maxListSize);
			return null;
		}
		
		
		
		return uploadReq;
	}

	private void updateUploadRequestWithErrorMessage(ListUploadRequest uploadReq, String data) {
		uploadReq.setErrorData(data);
		uploadReq.setStatus(AppConstants.FAIL);
		uploadReq.setLastUpdated(new Date());
		listUploadRepo.save(uploadReq);
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
	public List<ListDefinition> getTop3ListDefinition(String listType) {
		List<ListDefinition> listDefinitionData = listDefRepo.findTop3ByTypeAndActiveOrderByLastUpdatedDesc(listType, true);
		
		for (ListDefinition listDefinition : listDefinitionData) {
			listDefinition.setListSize(listDetailRepo.getListSizeByListId(listDefinition.getId()));
			listDefinition.setListUploadRequests(listUploadRepo.findAllByListRefIdOrderByLastUpdatedDesc(listDefinition.getId()));
		}
		return listDefinitionData;
	}

	@Override
	@Transactional
	public void updateListDetails(UserDefinition loginUser, ListDetails[] listDetails) throws AppException {
		listDetailRepo.save(Arrays.asList(listDetails));
		auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Update_List_Record, "updated " + listDetails.length + " new list records to list " + listDetails[0].getListRefId());
	}

	@Override
	@Transactional
	public void deleteListDetails(UserDefinition loginUser, ListDetails[] listDetails) throws AppException {
		listDetailRepo.delete(Arrays.asList(listDetails));
		auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Delete_List_Record, "deleted " + listDetails.length + " new list records to list " + listDetails[0].getListRefId());
	}

	@Value("${max_list_size:100000}")
	private int maxListSize;
	@Transactional
	@Override
	public Iterable<ListDetails>  createListDetails(UserDefinition loginUser, ListDetails[] listDetails) throws AppException {
		if (log.isDebugEnabled()) log.debug("the max list size is " + maxListSize);
		if (listDetails.length < 1) {
			throw new AppException("received empty list");
		}
		
		int currentListSize = listDetailRepo.getListSizeByListId(listDetails[0].getListRefId());
		
		if (currentListSize + listDetails.length > maxListSize) {
			throw new AppException(MessageDefinition.ListSizeOverLimitError + maxListSize);
		}
		
		
		for (ListDetails listDetail : listDetails) {
			listDetail.setMatchCCNDC( midDataService.findMatchingCCNDC(listDetail.getDialPattern()) );
			listDetail.setLastUpdatedBy(loginUser.getUserName());
			listDetail.setLastUpdated(new Date());
		}
		
		EventNotification event = new EventNotification();
		event.setCustomerName(loginUser.getCustomerName());
		event.setEventType(EventTypeDefinition.List_Update.value());
		event.setReferenceId(listDetails[0].getListRefId());
		event.setMessage("update list entry");
		event.setCreateTimestamp(new Date());
		event.setLastUpdatedBy(loginUser.getLastUpdatedBy());
		event.setStatus("new");
		eventService.addEventNotification(event);
		
		Iterable<ListDetails> result = listDetailRepo.save(Arrays.asList(listDetails));
		auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Add_List_Record, "added " + listDetails.length + " new list records to list " + listDetails[0].getListRefId());
        return result;
	}

	@Override
	public void getListDetailDataByDialPattern(ListDetails listDetail) {
		String ccNdc = midDataService.findMatchingCCNDC(listDetail.getDialPattern());
		
		RangeNdc rangeNDC = rangeRepo.findTop1ByCcNdc(ccNdc);
		
		if (rangeNDC != null) {
			listDetail.setCcNdc(ccNdc);
			listDetail.setIso2(rangeNDC.getIso2());
			listDetail.setCode(rangeNDC.getCode());
			listDetail.setTos(rangeNDC.getTos());
			listDetail.setProvider(rangeNDC.getProvider());
			listDetail.setTosdesc(rangeNDC.getTosdesc());
			listDetail.setBillingId(rangeNDC.getBillingId());
			listDetail.setLocality(rangeNDC.getLocality());
			listDetail.setNdc(rangeNDC.getNdc());
			listDetail.setSupplement(rangeNDC.getSupplement());
			listDetail.setTermCountry(rangeNDC.getTermCountry());
		}
		
		return;
	}


}
