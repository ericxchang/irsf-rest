package com.iconectiv.irsf.portal.service.impl;

import com.iconectiv.irsf.portal.config.CustomerContextHolder;
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
import com.iconectiv.irsf.util.DateTimeHelper;
import com.iconectiv.irsf.util.JsonHelper;
import com.iconectiv.irsf.util.ListHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    EntityManagerFactory customerEntityManagerFactory;
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
    private AuditTrailService auditService;
    @Autowired
    private MobileIdDataService midDataService;
    @Autowired
    private RangeNdcRepository rangeRepo;
    @Autowired
    @Lazy
    private PartitionService partitionService;
    @Value("${max_list_size:100000}")
    private int maxListSize;

    @Async
    @Override
    public void processListUploadRequest(UserDefinition user, ListDefinition listDef, ListUploadRequest uploadRequest, boolean isInitialLoading) {
        try {
            CustomerContextHolder.setSchema(user.getSchemaName());
            createListDefinition(user, listDef);

            uploadRequest.setListRefId(listDef.getId());
            uploadRequest.setStatus(AppConstants.PROCESS);
            uploadRequest.setLastUpdated(DateTimeHelper.nowInUTC());
            uploadRequest.setLastUpdatedBy(listDef.getLastUpdatedBy());

            listUploadRepo.save(uploadRequest);
            uploadRequest.setListDefintion(listDef);

            persistListUploadRequest(uploadRequest, isInitialLoading);
        } catch (Exception e) {
            log.error("Error to process list upload request: ", e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void persistListUploadRequest(ListUploadRequest uploadReq, Boolean isInitialLoading) {
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
            event.setCreateTimestamp(DateTimeHelper.nowInUTC());
            event.setLastUpdatedBy(uploadReq.getLastUpdatedBy());
            event.setStatus("new");
            eventService.addEventNotification(event);

        } catch (Exception e) {
            log.error("Error to parse black list: \n", e);
            uploadReq.setStatus(AppConstants.FAIL);
            listUploadRepo.save(uploadReq);
        }

    }

    private void updateUploadRequestWithErrorMessage(ListUploadRequest uploadReq, String data) {
        uploadReq.setErrorData(data);
        uploadReq.setStatus(AppConstants.FAIL);
        uploadReq.setLastUpdated(DateTimeHelper.nowInUTC());
        listUploadRepo.save(uploadReq);
    }

    @Override
    public void createListDefinition(UserDefinition user, ListDefinition listDef) {
        listDef.setActive(true);
        listDef.setCustomerName(user.getCustomerName());
        listDef.setLastUpdatedBy(user.getUserName());
        listDef.setLastUpdated(DateTimeHelper.nowInUTC());

        if (listDef.getId() == null) {
            listDef.setCreateBy(user.getUserName());
            listDef.setCreateTimestamp(DateTimeHelper.nowInUTC());
            listDefRepo.save(listDef);

            if (log.isDebugEnabled()) log.debug("created list definition recrod {}", JsonHelper.toJson(listDef));
            auditService.saveAuditTrailLog(user, AuditTrailActionDefinition.Create_List_Definition, "create new " +
                    listDef.getType() + " list " + listDef.getId());
        } else {
            updateListName(user, listDef);
        }
    }

    private void updateListName(UserDefinition loginUser, ListDefinition newListDef) {
        ListDefinition oldListDef = listDefRepo.findOne(newListDef.getId());
        if (newListDef.getListName().compareTo(oldListDef.getListName()) == 0 && newListDef.getDescription().compareTo(oldListDef.getDescription()) == 0) {
            return;
        }

        newListDef.setCreateBy(oldListDef.getCreateBy());
        newListDef.setCreateTimestamp(oldListDef.getCreateTimestamp());

        listDefRepo.save(newListDef);

        auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Update_List_Definition, "update list definition, id " + newListDef.getId());
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
    public void deleteListDefinition(UserDefinition loginUser, String listName) {
        listDefRepo.deleteByListName(listName);
        return;
    }

    @Override
    @Transactional
    public void deleteListDefinition(UserDefinition loginUser, int listId) {
        ListDefinition listDef = listDefRepo.findOne(listId);

        if (listDef != null) {
            listUploadRepo.deleteAllByListRefId(listDef.getId());
            listDetailRepo.deleteAllByListRefId(listDef.getId());

            auditService.saveAuditTrailLog(loginUser.getUserName(), loginUser.getCustomerName(), "delete list", "successfully remove list " + listId);

            partitionService.checkStale(loginUser, listDef, "list " + listDef.getListName() + " has been removed");
        }

        return;
    }

    @Override
    public List<ListDetails> getListDetailDataByListId(int listId) {
        List<ListDetails> dataList = new ArrayList<>();

        for (Object[] row : listDetailRepo.findAllDetailsByListRefId(listId)) {
            dataList.add(ListHelper.convertToListDetail(row));
        }

        return dataList;
    }

    @Override
    public List<ListDetails> getListDetailDataByUploadId(int uploadId) {
        List<ListDetails> dataList = new ArrayList<>();

        for (Object[] row : listDetailRepo.findAllDetailsByUpLoadRefId(uploadId)) {
            dataList.add(ListHelper.convertToListDetail(row));
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

    private void updateListDefinitionTime(UserDefinition loginUser, Integer listId) {
        updateListDefinitionTime(loginUser, listDefRepo.findOne(listId));
    }

    private void updateListDefinitionTime(UserDefinition loginUser, ListDefinition listDefinition) {
        listDefinition.setLastUpdated(DateTimeHelper.toUTC(new Date()));
        listDefinition.setLastUpdatedBy(loginUser.getUserName());
        listDefRepo.save(listDefinition);
    }

    @Override
    @Transactional
    public void updateListDetails(UserDefinition loginUser, ListDetails[] listDetails) throws AppException {
        if (listDetails.length < 1) {
            return;
        }
        Integer listId = listDetails[0].getListRefId();
        updateListDefinitionTime(loginUser, listId);

        Arrays.stream(listDetails).forEach(item -> {
            item.setLastUpdated(DateTimeHelper.toUTC(new Date()));
            item.setLastUpdatedBy(loginUser.getUserName());
        });


        listDetailRepo.save(Arrays.asList(listDetails));
        auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Update_List_Record, "updated " + listDetails.length + " new list records to list " + listDetails[0].getListRefId());
    }

    @Override
    @Transactional
    public void deleteListDetails(UserDefinition loginUser, ListDetails[] listDetails) throws AppException {
        if (listDetails.length < 1) {
            return;
        }
        Integer listId = listDetails[0].getListRefId();
        updateListDefinitionTime(loginUser, listId);

        listDetailRepo.delete(Arrays.asList(listDetails));
        auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Delete_List_Record, "deleted " + listDetails.length + " new list records to list " + listDetails[0].getListRefId());

        ListDefinition listDefinition = listDefRepo.findOne(listDetails[0].getListRefId());
        partitionService.checkStale(loginUser, listDefinition, "list " + listDefinition.getListName() + " has changed");
    }

    @Transactional
    @Override
    public Iterable<ListDetails> createListDetails(UserDefinition loginUser, ListDetails[] listDetails) throws AppException {
        if (log.isDebugEnabled()) log.debug("the max list size is " + maxListSize);
        if (listDetails.length < 1) {
            throw new AppException("received empty list");
        }

        int currentListSize = listDetailRepo.getListSizeByListId(listDetails[0].getListRefId());

        if (currentListSize + listDetails.length > maxListSize) {
            throw new AppException(MessageDefinition.ListSizeOverLimitError + maxListSize);
        }


        for (ListDetails listDetail : listDetails) {
            listDetail.setMatchCCNDC(midDataService.findMatchingCCNDC(listDetail.getDialPattern()));
            listDetail.setLastUpdatedBy(loginUser.getUserName());
            listDetail.setLastUpdated(DateTimeHelper.nowInUTC());
        }


        EventNotification event = new EventNotification();
        event.setCustomerName(loginUser.getCustomerName());
        event.setEventType(EventTypeDefinition.List_Update.value());
        event.setReferenceId(listDetails[0].getListRefId());
        event.setMessage("update list entry");
        event.setCreateTimestamp(DateTimeHelper.nowInUTC());
        event.setLastUpdatedBy(loginUser.getLastUpdatedBy());
        event.setStatus("new");
        eventService.addEventNotification(event);

        Iterable<ListDetails> result = listDetailRepo.save(Arrays.asList(listDetails));
        auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Add_List_Record, "added " + listDetails.length + " new list records to list " + listDetails[0].getListRefId());

        ListDefinition listDefinition = listDefRepo.findOne(listDetails[0].getListRefId());
        updateListDefinitionTime(loginUser, listDefinition);
        partitionService.checkStale(loginUser, listDefinition, "list " + listDefinition.getListName() + " has changed");
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
            listDetail.setNdc(rangeNDC.getNdc());
            listDetail.setSupplement(rangeNDC.getSupplement());
            listDetail.setTermCountry(rangeNDC.getTermCountry());
        }

        return;
    }


}
