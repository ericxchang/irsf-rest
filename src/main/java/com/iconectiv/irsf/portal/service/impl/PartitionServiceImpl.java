package com.iconectiv.irsf.portal.service.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.iconectiv.irsf.json.vaidation.JsonValidationException;
import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.core.AuditTrailActionDefinition;
import com.iconectiv.irsf.portal.core.EventTypeDefinition;
import com.iconectiv.irsf.portal.core.PartitionDataType;
import com.iconectiv.irsf.portal.core.PartitionExportStatus;
import com.iconectiv.irsf.portal.core.PartitionStatus;
import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.common.CustomerDefinition;
import com.iconectiv.irsf.portal.model.common.EventNotification;
import com.iconectiv.irsf.portal.model.common.HttpResponseMessage;
import com.iconectiv.irsf.portal.model.common.Premium;
import com.iconectiv.irsf.portal.model.common.RangeNdc;
import com.iconectiv.irsf.portal.model.common.RangeQueryFilter;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.ListDefinition;
import com.iconectiv.irsf.portal.model.customer.ListDetails;
import com.iconectiv.irsf.portal.model.customer.PartitionDataDetails;
import com.iconectiv.irsf.portal.model.customer.PartitionDefinition;
import com.iconectiv.irsf.portal.model.customer.PartitionExportHistory;
import com.iconectiv.irsf.portal.model.customer.RuleDefinition;
import com.iconectiv.irsf.portal.repositories.common.CustomerDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.common.EventNotificationRepository;
import com.iconectiv.irsf.portal.repositories.customer.ListDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.customer.ListDetailsRepository;
import com.iconectiv.irsf.portal.repositories.customer.PartitionDataDetailsRepository;
import com.iconectiv.irsf.portal.repositories.customer.PartitionDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.customer.PartitionExportHistoryRepository;
import com.iconectiv.irsf.portal.repositories.customer.RuleDefinitionRepository;
import com.iconectiv.irsf.portal.service.AuditTrailService;
import com.iconectiv.irsf.portal.service.EventNotificationService;
import com.iconectiv.irsf.portal.service.ListService;
import com.iconectiv.irsf.portal.service.MobileIdDataService;
import com.iconectiv.irsf.portal.service.PartitionService;
import com.iconectiv.irsf.util.DateTimeHelper;
import com.iconectiv.irsf.util.JsonHelper;
import com.iconectiv.irsf.util.MultipleFileZip;

import io.jsonwebtoken.lang.Assert;

@Service
public class PartitionServiceImpl implements PartitionService {
	private static Logger log = LoggerFactory.getLogger(PartitionServiceImpl.class);

	final String RANGE_NDC_TYPE = "Range NDC";
	final String PREMIUM_RANGE_TYPE = "IPRN";
	final String PRIME2 = "PRIME-2";
	final String PRIME3 = "PRIME-3";
	final String PRIME4 = "PRIME-4";
	final String CSV_COMMON_SEPERATOR = "|";
	final String IRSF_DATA_LOADER_CUSTOMER_NAME = "irsf";
	final String IRSF_DATA_LOADER_EVENT_TYPE = "RefreshData";

	@Value("${export.file.path:/apps/irsf/data/export/}")
	private String exportFilePath;
	
	@Autowired
	private PartitionDefinitionRepository partitionDefRepo;
	@Autowired
	private PartitionExportHistoryRepository exportRepo;
	@Autowired
	private PartitionDataDetailsRepository partitionDataRepo;
	@Autowired
	private RuleDefinitionRepository ruleRepo;
	@Autowired
	private AuditTrailService auditService;
	@Autowired
	private MobileIdDataService mobileIdService;
	@Autowired
	private ListDetailsRepository listDetailsRepo;
	@Autowired
	private ListDefinitionRepository listDefinitionRepo;
	@Autowired
	private EventNotificationService eventService;
	@Autowired
	private EventNotificationRepository eventRepo;
	@Autowired
	private CustomerDefinitionRepository customerRepo;
	@Autowired
	private	ListService listService;

	@Async
	@Override
	public void refreshPartition(UserDefinition loginUser, Integer partitionId) {
		syncRefreshPartition(loginUser, partitionId);
	}
	public PartitionDefinition syncRefreshPartition(UserDefinition loginUser, Integer partitionId) {
		CustomerContextHolder.setSchema(loginUser.getSchemaName());
		String prevStatus = null;
		PartitionDefinition partition = null;
		try {
			partition = partitionDefRepo.findOne(partitionId);

			if (partition == null) {
				throw new AppException("Invalid partition Id");
			}
			validateParitionStatus(partition);

			prevStatus = partition.getStatus();
			partition.setStatus(PartitionStatus.InProgress.value());
			partitionDefRepo.save(partition);
			refreshParitionData(loginUser, partition);
		} catch (AppException e) {
			log.error("Error to refresh partition:", e);
			if (partition != null) {
				partition.setStatus(prevStatus);
				partitionDefRepo.save(partition);
			}
		}
		return partition;
	}

	@Transactional
	private void refreshParitionData(UserDefinition loginUser, PartitionDefinition partition) throws AppException {
		try {

			generateDraftData(partition);

			partition.setStatus(PartitionStatus.Draft.value());
			partition.setDraftDate(DateTimeHelper.nowInUTC());
			partition.setLastUpdatedBy(loginUser.getUserName());
			partitionDefRepo.save(partition);

			log.info("generating event log for refreshing parttion");

			EventNotification event = new EventNotification();
			event.setCreateTimestamp(DateTimeHelper.nowInUTC());
			event.setEventType(EventTypeDefinition.Partition_Draft.value());
			event.setReferenceId(partition.getId());
			event.setCustomerName(loginUser.getCustomerName());
			event.setStatus("new");
			event.setMessage("Genrated draft data for partition " + partition.getName());
			eventRepo.save(event);
			eventService.broadcastPartitionEvent(loginUser.getCustomerId(), event);

			auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Refresh_Partition_Data,
			        "generated partition draft data set:" + partition.getId());
		} catch (Exception e) {
			log.error("Error on export partition data", e);
			throw new AppException(e);
		}
	}

	private List<PartitionDataDetails> convertNdcToPartitionDataDetails(PartitionDefinition partition,
	        RuleDefinition rule, List<RangeNdc> list) {
		List<PartitionDataDetails> pdList = new ArrayList<PartitionDataDetails>(list.size());
		for (RangeNdc obj : list) {
			PartitionDataDetails p = new PartitionDataDetails();
			p.setPartitionId(partition.getId());
			p.setBillingId(obj.getBillingId());
			p.setCc(obj.getCode());
			p.setCustomerDate(obj.getEffectiveDate());
			p.setDialPattern(obj.getCcNdc());
			p.setIso2(obj.getIso2());
			p.setNdc(obj.getNdc());
			p.setNotes(null);
			p.setProvider(obj.getProvider());
			p.setReason(null);
			p.setReference(rule.getId().toString());
			p.setTos(obj.getTos());
			p.setTosdesc(obj.getTosdesc());
			p.setDataType("R");
			pdList.add(p);
		}

		return pdList;

	}

	private List<PartitionDataDetails> convertIprnToPartitionDataDetails(PartitionDefinition partition,
	        RuleDefinition rule, List<Premium> list) {
		List<PartitionDataDetails> pdList = new ArrayList<PartitionDataDetails>(list.size());
		for (Premium obj : list) {
			PartitionDataDetails p = new PartitionDataDetails();
			p.setPartitionId(partition.getId());
			p.setBillingId(obj.getBillingId());
			p.setCc(obj.getCode());
			p.setCustomerDate(obj.getLastUpdate());

			if (PRIME2.equals(rule.getDialPatternType())) {
				p.setDialPattern(obj.getPrimeMinus2());
			} else if (PRIME3.equals(rule.getDialPatternType())) {
				p.setDialPattern(obj.getPrimeMinus3());
				if (p.getDialPattern() == null)
					p.setDialPattern(obj.getPrimeMinus2());
			} else if (PRIME4.equals(rule.getDialPatternType())) {
				p.setDialPattern(obj.getPrimeMinus3());
				if (p.getDialPattern() == null)
					p.setDialPattern(obj.getPrimeMinus3());
				if (p.getDialPattern() == null)
					p.setDialPattern(obj.getPrimeMinus2());
			}

			p.setIso2(obj.getIso2());
			p.setNdc(obj.getNdc());
			p.setNotes(null);
			p.setProvider(obj.getProvider());
			p.setReason(null);
			p.setReference(rule.getId().toString());
			p.setTos(obj.getTos());
			p.setTosdesc(obj.getTosdesc());
			p.setDataType(PartitionDataType.Rule.value());
			pdList.add(p);
		}

		return pdList;

	}

	private List<PartitionDataDetails> convertListDetailsToPartitionDataDetails(PartitionDefinition partition,
	        ListDefinition listDef, List<ListDetails> list, String listType) {
		List<PartitionDataDetails> pdList = new ArrayList<PartitionDataDetails>(list.size());
		for (ListDetails obj : list) {
			PartitionDataDetails p = new PartitionDataDetails();
			p.setPartitionId(partition.getId());
			p.setBillingId(obj.getBillingId());
			p.setCc(obj.getCode());
			p.setCustomerDate(obj.getCustomerDate());
			p.setDialPattern(obj.getDialPattern());
			p.setIso2(obj.getIso2());
			p.setNdc(obj.getNdc());
			p.setNotes(obj.getNotes());
			p.setProvider(obj.getProvider());
			p.setReason(obj.getReason());
			p.setReference(listDef.getListName());
			p.setTos(obj.getTos());
			p.setTosdesc(obj.getTosdesc());
			p.setDataType(listType);
			pdList.add(p);
		}
		return pdList;

	}

	/*
	 * To export partition: 1. copy data to parition_export_history data and
	 * move the status of current partition to "Locked" 2. clear data from
	 * partition_data_details table 3. clone a new partition with status of
	 * "fresh" and underlying rule(s) 4. create event to send export data to EI
	 * system
	 */
	@Override
	@Async
	public void exportPartition(UserDefinition loginUser, Integer partitionId) {
		CustomerContextHolder.setSchema(loginUser.getSchemaName());
		HttpResponseMessage httpResponseMessage;
		PartitionDefinition partition = null;
		String prevStatus = null;
		try {
			partition = partitionDefRepo.findOne(partitionId);

			if (partition == null) {
				throw new AppException("Invalid partition Id");
			}
			log.info("exportPartition: partitionId: {}, status: {}", partitionId, partition.getStatus());

			boolean refresh = validateParitionExportStatus(partition);
			if (refresh) {
				partition = syncRefreshPartition(loginUser, partitionId); 
			}
			prevStatus = partition.getStatus();
			PartitionExportHistory partHist = exportPartitionData(loginUser, partition);
			 
			CustomerDefinition customer = customerRepo.findByCustomerName(loginUser.getCustomerName());
			if (customer.getExportTarget() != null) {
				log.info("exportPartition: calling sendExportFile2EI(): partitionId: {}, status: {}", partitionId, partition.getStatus());
				httpResponseMessage = sendExportFile2EI(loginUser, partHist, customer.getExportTarget());
				log.info("Http response: status: {}, message: {}, httpStatus: {}", httpResponseMessage.getStatus(), httpResponseMessage.getMessage(), httpResponseMessage.getHttpStatus());
				if (!"success".equals(httpResponseMessage.getStatus())){
					throw new AppException(httpResponseMessage.getMessage());
				}
				
			}
			
		} catch (AppException e) {
			log.error("Error to export partition:", e);
			if (partition != null) {
				partition.setStatus(prevStatus);
				partitionDefRepo.save(partition);
			}
		}

		return;
	}
	@Override
	@Async
	public void resendPartition(UserDefinition loginUser, Integer partitionId) {
		CustomerContextHolder.setSchema(loginUser.getSchemaName());
		HttpResponseMessage httpResponseMessage;
		String prevStatus = null;
		PartitionDefinition partition = null;
		try {
			partition = partitionDefRepo.findOne(partitionId);

			if (partition == null) {
				log.error("Invalid partitionId: {}", partitionId);
				throw new AppException("Invalid partition Id: " + partitionId);
			}
			log.info("exportPartition: partitionId: {}, status: {}", partitionId, partition.getStatus());
			List<PartitionExportHistory> partHistList = exportRepo.findAllByPartitionId(partitionId);
			if (partHistList == null || partHistList.isEmpty()) {
				log.error("No partition history data found for partition: {}", partitionId);
				throw new AppException("No partition history data found for partition: " + partitionId);
			}
			prevStatus = partition.getStatus();
			PartitionExportHistory partHist = 	partHistList.get(0);
			 
			CustomerDefinition customer = customerRepo.findByCustomerName(loginUser.getCustomerName());
			if (customer.getExportTarget() != null) {
				log.info("exportPartition: calling sendExportFile2EI(): partitionId: {}, status: {}", partitionId, partition.getStatus());
				httpResponseMessage = sendExportFile2EI(loginUser, partHist, customer.getExportTarget());
				if (!"success".equals(httpResponseMessage.getStatus())){
					log.error("Http response: status: {}, message: {}, httpStatus: {}", httpResponseMessage.getStatus(), httpResponseMessage.getMessage(), httpResponseMessage.getHttpStatus());
					throw new AppException(httpResponseMessage.getMessage());
				}
			}
			
		} catch (AppException e) {
			log.error("Error to export partition:", e);
			if (partition != null) {
				partition.setStatus(prevStatus);
				partitionDefRepo.save(partition);
			}
		}

		return;
	}


	private void validateParitionStatus(PartitionDefinition partition) throws AppException {
		partition = partitionDefRepo.findOne(partition.getId());
		Assert.notNull(partition);

		if (partition.getStatus().equals(PartitionStatus.InProgress.value())) {
			throw new AppException("System is generating partition data set");
		}

		if (partition.getStatus().equals(PartitionStatus.Exported.value())) {
			throw new AppException("Partition has been exported");
		}

		return;
	}

	private boolean validateParitionExportStatus(PartitionDefinition partition) throws AppException {
		boolean refreshFlag = false;
		partition = partitionDefRepo.findOne(partition.getId());
		Assert.notNull(partition);

		if (partition.getStatus().equals(PartitionStatus.InProgress.value())) {
			throw new AppException("System is exporting partition data set");
		}
		if (partition.getStatus().equals(PartitionStatus.Exported.value())) {
			throw new AppException("Partition has been exported");
		}
		if (partition.getStatus().equals(PartitionStatus.Stale.value())) {
			log.info("partition data status is stale, refresh it");
			refreshFlag = true;
		}
		if (partition.getStatus().equals(PartitionStatus.Fresh.value())) {
			log.info("partition data status is fresh, refresh it");
			refreshFlag = true;
		}
		
		//if (!partition.getStatus().equals(PartitionStatus.Draft.value())) {
		//	throw new AppException("System has not generated partition data set");
		//}

		return refreshFlag;
	}

	@Transactional
	private PartitionExportHistory exportPartitionData(UserDefinition loginUser, PartitionDefinition partition) throws AppException {
		HttpResponseMessage httpResponseMessage = null;
		List<String> WL_DataType = new ArrayList<String>();
		List<String> NON_WL_DataType = new ArrayList<String>();
		WL_DataType.add(PartitionDataType.WhiteList.value());
		NON_WL_DataType.add(PartitionDataType.BlackList.value());
		NON_WL_DataType.add(PartitionDataType.Rule.value());

		PartitionExportHistory partHist = new PartitionExportHistory();
		StringBuffer sb = new StringBuffer();

		try {
			log.debug("exportPartitionData(): partitionId: {}; status: {}", partition.getId(), partition.getStatus());

			partition.setStatus(PartitionStatus.InProgress.value());
			log.debug("exportPartitionData(): partitionId: {}; set status to:  {}", partition.getId(), partition.getStatus());
			partitionDefRepo.save(partition);
			EventNotification event = eventRepo.findTop1ByCustomerNameAndEventTypeOrderByCreateTimestampDesc(
			        IRSF_DATA_LOADER_CUSTOMER_NAME, IRSF_DATA_LOADER_EVENT_TYPE);

			List<PartitionDataDetails> partitionDataListLong = partitionDataRepo
			        .findAllByPartitionId(partition.getId());
			List<String> partitionDataListShort = partitionDataRepo
			        .findDistinctDialPatternByPrtitionId(partition.getId(), NON_WL_DataType);
			List<String> whiteList = partitionDataRepo.findDistinctDialPatternByPrtitionId(partition.getId(),
			        WL_DataType);

			partHist.setExportFileLong(buildPartitionDataLong(partitionDataListLong));
			partHist.setExportFileShort(buildByteArrayFromList(partitionDataListShort));
			partHist.setExportWhitelist(buildByteArrayFromList(whiteList));
			
			partHist.setExportFileLongSize(partHist.getExportFileLong().length);
			partHist.setExportFileShortSize(partHist.getExportFileShort().length);
			partHist.setExportWhitelistSize(partHist.getExportWhitelist().length);

			log.info(
			        "exportPartitionData(): partitionId: {}, size of exportFileLong: {}, size of exportFileShort: {}, size of exportWhitelist: {}",
			        partition.getId(), partHist.getExportFileLongSize(), partHist.getExportFileShortSize(),
			        partHist.getExportWhitelistSize());

			partHist.setExportDate(DateTimeHelper.nowInUTC());
			partHist.setOrigPartitionId(partition.getOrigPartitionId());
			partHist.setPartitionId(partition.getId());
			partHist.setStatus(PartitionExportStatus.Success.value());
			partHist.setReason(AuditTrailActionDefinition.Export_Partition_Data);
			partHist.setMidDataLoadTime(event.getCreateTimestamp());

			log.debug("exportPartitionData(): save PartitionExportHistory");
			partHist = exportRepo.save(partHist);
			
			partition.setStatus(PartitionStatus.Exported.value());
			partition.setLastExportDate(DateTimeHelper.nowInUTC());
			partition.setLastUpdatedBy(loginUser.getUserName());
			
			log.debug("exportPartitionData(): update  PartitionDefinition");
			partition = partitionDefRepo.save(partition);
			
			
			log.debug("exportPartitionData(): clone partitionDefinition");
			PartitionDefinition newPartition = clonePartition(loginUser, partition);
			
			log.debug("exportPartitionData():: move PartitionDataDetails: old: {}; new: {} ", partition.getId(),
			        newPartition.getId());
			partitionDataRepo.movePartition(partition.getId(), newPartition.getId());
	
			auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Export_Partition_Data,
			        "export partition data set " + partition.getId());
			
			
		} catch (Exception e) {
			log.error("Error on export partition data", e);
			throw new AppException(e);
		}
		
		return partHist;
	}
	
	@Transactional
	private HttpResponseMessage sendExportFile2EI(UserDefinition loginUser, PartitionExportHistory partHist, String url) throws AppException {

		HttpResponseMessage httpResponseMessage = null;
		
		if (url == null)  
			  return httpResponseMessage;
		
		String fileName = makeFileName();
		// create file 1 from ExportFileShort
		String file1 = null;
		if (partHist.getExportFileShort() != null && partHist.getExportFileShort().length > 0) {
			file1 = exportFilePath + loginUser.getUserName() + "_" + fileName + ".csv";
			Path path1 = Paths.get(file1);
	        log.info("write path: " + path1.getFileName());
	        try {
	        	
				Files.write(path1, partHist.getExportFileShort());
			} catch (IOException e) {
				log.error("sendExportFile2EI: write file: {}, Exception: {} ", file1, e.getMessage());
				httpResponseMessage = new HttpResponseMessage(null, "Failed to write " + file1, "failed", null);
				e.printStackTrace();
				
				
			}
		}
		
		// create file 2 from ExportWhitelist
		String file2 = null;
		List<String> files = new ArrayList<String>();
		if (partHist.getExportWhitelist() != null && partHist.getExportWhitelist().length > 0) {
			file2 = exportFilePath + loginUser.getUserName() + "_WL_" + fileName + ".csv";
			Path path2 = Paths.get(file2);
			if (httpResponseMessage == null) {
				log.info("write path: " + path2.getFileName());
				try {
					Files.write(path2, partHist.getExportWhitelist());
				} catch (IOException e) {
					log.error("sendExportFile2EI: write file: {}, Exception: {} ", file2, e.getMessage());
					httpResponseMessage = new HttpResponseMessage(null, "Failed to write " + file2, "failed", null);
					e.printStackTrace();

				}

			}
		}
		// if no error, add files to a list
		if (httpResponseMessage == null) {
			if (file1 != null)
				files.add(file1);

			if (file2 != null)
				files.add(file2);
		}
		// if no file was added to the list, return error
		if (files.isEmpty()) {
			log.error("sendExportFile2EI: no file is avaiaable to be resent");
			httpResponseMessage = new HttpResponseMessage(null, "no file is avaiaable to be resent", "failed", null);
		}
		
		// if everything is OK, zip the file and send it to EI server
		if (httpResponseMessage == null) {
			String zipFile = exportFilePath + loginUser.getUserName() + fileName + "_export.zip";
			makeZipFile(zipFile, files);
			url = url + "?customer=" + loginUser.getCustomerName() + "&partition=" + partHist.getId();
			httpResponseMessage = uploadFiles(zipFile, url);

			partHist.setStatus(httpResponseMessage.getStatus());
			partHist.setReferenceId(httpResponseMessage.getId());
			log.debug(
					"sendExportFile2EI(): update PartitionExportHistory with status: {}, partition export history id: {}, EI reference ID: ",
					httpResponseMessage.getStatus(), partHist.getId(), httpResponseMessage.getId());
			partHist = exportRepo.save(partHist);

		}
         
        log.debug("sendExportFile2EI: message: {}, status: {}, id: {}", httpResponseMessage.getMessage(), httpResponseMessage.getStatus(), httpResponseMessage.getId());
        auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Send_Partition_Data_To_EI, httpResponseMessage.getMessage());
        
        return httpResponseMessage;

	}

	private HttpResponseMessage uploadFiles(String uploadFleName, String url) {
        String status = "success";
        HttpResponseMessage  httpMsg = null;
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> httpEntity;
		ResponseEntity<String> statusResponse;
        
		// set headers here:
		//headers.set("requester", "test");
		//headers.set("Authorization", "Token KqY+VEP3A/Cj");
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		httpEntity = new HttpEntity(headers);

		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("file", new FileSystemResource(uploadFleName));
		try {
			statusResponse = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<MultiValueMap<String, Object>>(map, headers), String.class);
			HttpStatus httpStatus =  statusResponse.getStatusCode() ;
            log.info("HttpStatus: " + httpStatus);

            if (statusResponse.hasBody()) {
            	log.info(statusResponse.getBody().toString());
            	String jsonString = statusResponse.getBody();

            	Map<String, String> messages = JsonHelper.fromJson(jsonString, Map.class);
            	Set<String> keys = messages.keySet();
            	Iterator<String> it = keys.iterator();
            	String rtnMessage = null;
            	String rtnStatus = null;
            	String rtnId = null;
            	
            	while (it.hasNext()) {
            		String key = it.next();
            		if ("message".equals(key)) {
            			rtnMessage = messages.get(key);
            		}
            		else if ("status".equals(key)) {
            			rtnStatus = messages.get(key);
            		}
            		else if ("id".equals(key)) {
            			rtnId = messages.get(key);
            		}
            	}
            	
               	httpMsg = new HttpResponseMessage(httpStatus, rtnMessage, rtnStatus, rtnId ); 
            	log.info("message: {}, status: {}, id: {}", rtnMessage, rtnStatus, rtnId);
            	
            	
            }

		} catch(JsonValidationException e) {
			log.error(e.getMessage());
			httpMsg = new HttpResponseMessage(null, e.getMessage(),"failed", null ); 
			
		} catch(HttpClientErrorException e) {
			String msg = "Document not found! Status code " + e.getStatusCode();
			log.error(msg);
			httpMsg = new HttpResponseMessage(null, msg,"failed", null ); 
			
		}
		
		return httpMsg;
	}
    private String makeFileName() {
      	SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String timeString = DateTimeHelper.formatDate(new Date(), format);
    	return timeString;
    }
    private boolean makeZipFile(String fileName, List<String> files) {
    	boolean cc = true;
    	 MultipleFileZip mfe = new MultipleFileZip();
    	 String zipfile  = "";
          try {
 			zipfile = mfe.zipFiles(fileName, files);
 			log.info("makeZipFile(): filename: {}, number of files inside the zip file: ", zipfile, files.size());
 		} catch (Exception e) {
 			log.error(e.getMessage());
 			cc = false;
 			e.printStackTrace();
 		}
        return cc;
    }
	private byte[] buildPartitionDataLong(List<PartitionDataDetails> list) {
		log.info("buildPartitionDataLong(): number of rows: {}", list.size());
		StringBuffer sb = new StringBuffer();
		for (PartitionDataDetails p : list) {
			sb.append(p.toCSVheader(CSV_COMMON_SEPERATOR));
			sb.append("\n");
		}
		list.clear();
		list = null;
		return sb.toString().getBytes();

	}

	private byte[] buildByteArrayFromList(List<String> list) {
		log.info("buildByteArrayFromList(): number of rows: {}", list.size());
		StringBuffer sb = new StringBuffer();
		for (String p : list) {
			sb.append(p);
			sb.append("\n");
		}
		list.clear();
		list = null;
		return sb.toString().getBytes();

	}

	private void generateDraftData(final PartitionDefinition partition) {

		List<PartitionDataDetails> partitionDataList = null;

		List<RuleDefinition> rules = partition.getRuleDefinitions();
		if (rules == null || rules.isEmpty()) {
			rules = ruleRepo.findAllByPartitionId(partition.getId());
		}
		log.debug("generateDraftData: delete all partition data for partitionId: {}", partition.getId());
		partitionDataRepo.deleteByPartitionId(partition.getId());
        int rangeNdcCount = 0;
        int premiumCount = 0;
        int WLcount = 0;
        int BLCount = 0;
		for (RuleDefinition rule : rules) {
			if (!rule.isActive()) {
				log.info("skip inactive rule, rule_id: {}", rule.getId());
				continue;
			}
			partitionDataList = null;
			RangeQueryFilter filter = null;

			try {
				filter = rule.getRangeQueryFilter();
			} catch (JsonValidationException e) {
				log.error("can't parse RangeQueryFilter - {}, skip ruleId: {}, details: {}", e.toString(), rule.getId(),
				        rule.getDetails());
				continue;
			}
			log.debug("generateDraftData: retrieve all partition data for ruleId: {}", rule.getId());
			if (RANGE_NDC_TYPE.equals(rule.getDataSource())) {
				List<RangeNdc> list = mobileIdService.findAllRangeNdcByFilters(filter);
				if (list != null && !list.isEmpty()) {
					rangeNdcCount = list.size();
					log.debug("generateDraftData: found {} rows of ndc_range for ruleId: {}", rangeNdcCount, rule.getId());
					partitionDataList = convertNdcToPartitionDataDetails(partition, rule, list);
					log.debug("generateDraftData: build {} partition records", partitionDataList.size());
				}
				else {
					log.info("generateDraftData: no range_ndc data for ruleId: {}", rule.getId());
				}
			} else if (PREMIUM_RANGE_TYPE.equals(rule.getDataSource())) {
				List<Premium> list = mobileIdService.findAllPremiumRangeByFilters(filter);
				if (list != null && !list.isEmpty()) {
					premiumCount = list.size();
					log.debug("generateDraftData: found {} rows of premium for ruleId: {}", premiumCount, rule.getId());
					partitionDataList = convertIprnToPartitionDataDetails(partition, rule, list);
					log.debug("generateDraftData: build {} partition records", partitionDataList.size());
				}
				else {
					log.info("generateDraftData: no premium data for ruleId: {}", rule.getId());
				}
			} else {
				log.error("Unknown data source: {}, rule_id: {}", rule.getDataSource(), rule.getId());
			}
			if (partitionDataList != null && !partitionDataList.isEmpty()) {
				log.debug("generateDraftData: save partition data for ruleId: {}, rowCount: {}", rule.getId(), partitionDataList.size());
				partitionDataRepo.batchUpdate(partitionDataList);
				log.debug("generateDraftData: successfully saved partition data for ruleId: {}, rowCount: {}", rule.getId(), partitionDataList.size());
			}
		}
		log.debug("generateDraftData: retrieve white list data for partitionId: {}, WLId: {}", partition.getId(), partition.getWlId());
		partitionDataList = null;
		if (partition.getWlId() != null) {
			ListDefinition listDef = listDefinitionRepo.findOne(partition.getWlId());
			//List<ListDetails> wlList = listDetailsRepo.findAllByListRefId(partition.getWlId());
			List<ListDetails> wlList = listService.getListDetailDataByListId(partition.getWlId());
			if (wlList != null && !wlList.isEmpty()) {
				WLcount = wlList.size();
				log.debug("generateDraftData: found {} rows in the white list: {}", WLcount, partition.getWlId());
				partitionDataList = convertListDetailsToPartitionDataDetails(partition, listDef, wlList, PartitionDataType.WhiteList.value());
				partitionDataRepo.batchUpdate(partitionDataList);
			}
		}
		log.debug("generateDraftData: retrieve black list data for partitionId: {}, WLId: {}", partition.getId(), partition.getBlId());
		partitionDataList = null;
		if (partition.getBlId() != null) {
			//List<ListDetails> blList = listDetailsRepo.findAllByListRefId(partition.getBlId());
			List<ListDetails> blList = listService.getListDetailDataByListId(partition.getBlId());
			if (blList != null && !blList.isEmpty()) {
				BLCount = blList.size();
				log.debug("generateDraftData: found {} rows in the black list: {}", BLCount, partition.getBlId());
				ListDefinition listDef = listDefinitionRepo.findOne(partition.getBlId());
				partitionDataList = convertListDetailsToPartitionDataDetails(partition, listDef, blList, PartitionDataType.BlackList.value());
				partitionDataRepo.batchUpdate(partitionDataList);
			}
		}
		if (rangeNdcCount + premiumCount + WLcount + BLCount > 0)
			log.info("Completed generating partition data, NDC_RANGE count: {}, IPRN count: {}, WL count : {}, BL count: {}", rangeNdcCount, premiumCount, WLcount, BLCount);
		else {
			log.info("No rows found for partition: {}", partition.getId());
		}
		
		return;
	}

	private PartitionDefinition clonePartition(UserDefinition loginUser, PartitionDefinition partition) {
		if (partition.getOrigPartitionId() == null) {
			partition.setOrigPartitionId(partition.getId());
		}
		partition.setId(null);
		partition.setStatus(PartitionStatus.Fresh.value());
		partition.setLastUpdated(DateTimeHelper.nowInUTC());
		partition.setLastExportDate(null);
		partition.setDraftDate(null);
		partition.setLastUpdatedBy("cloned");
		partition = partitionDefRepo.save(partition);

		List<String> ruleIds = cloneRules(loginUser, partition);

		partition.setRuleIds(String.join(",", ruleIds));
		partitionDefRepo.save(partition);
		auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Clone_Partition,
		        "clone new partition " + partition.getId());

		return partition;
	}

	private List<String> cloneRules(UserDefinition loginUser, final PartitionDefinition partition) {
		List<String> ruleIds = new ArrayList<>();
		for (String ruleId : partition.getRuleIds().split(",")) {
			RuleDefinition rule = ruleRepo.findOne(Integer.valueOf(ruleId));
			Assert.notNull(rule);

			// mark the old one inActive
			rule.setActive(false);
			ruleRepo.save(rule);

			// create a new rule
			rule.setId(null);
			rule.setLastUpdated(DateTimeHelper.nowInUTC());
			rule.setCreatedBy("cloned");
			rule.setPartitionId(partition.getId());
			rule.setActive(true);
			ruleRepo.save(rule);

			ruleIds.add(rule.getId().toString());
			auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Clone_Rule,
			        "clone new rule " + rule.getId());

			if (log.isDebugEnabled())
				log.debug("Clone new rule {}", rule.getId());
		}
		return ruleIds;
	}

	@Transactional
	@Override
	public void addRule(UserDefinition loginUser, PartitionDefinition partition, RuleDefinition rule)
	        throws AppException {
		try {
			Assert.notNull(partition);
			Assert.notNull(rule);

			if (rule.getPartitionId() != null && !rule.getPartitionId().equals(partition.getId())) {
				throw new AppException("rule " + rule.getId() + " has assigned to different partition.");
			}

			Set<String> ruleIds = new HashSet<>();

			if (partition.getRuleIds() != null) {
				Collections.addAll(ruleIds, partition.getRuleIds().split(","));
			}
			ruleIds.add(rule.getId().toString());

			updateRuleId(partition, ruleIds, loginUser.getUserName());
			auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Add_Rule_To_Partition,
			        "append rule " + rule.getId() + " to partition " + partition.getId());

			checkStale(partition);
		} catch (Exception e) {
			log.error("Fail to add rule to parition: ", e);
			throw new AppException(e);
		}
	}

	@Transactional
	@Override
	public void removeRule(UserDefinition loginUser, PartitionDefinition partition, Integer ruleId)
	        throws AppException {
		try {
			Assert.notNull(partition);
			Assert.notNull(ruleId);

			Set<String> ruleIds = new HashSet<>();
			Collections.addAll(ruleIds, partition.getRuleIds().split(","));

			if (ruleIds.contains(ruleId.toString())) {
				ruleIds.remove(ruleId.toString());
			}

			updateRuleId(partition, ruleIds, loginUser.getUserName());

			// mark rule as inactive
			RuleDefinition rule = ruleRepo.findOne(ruleId);
			if (rule != null) {
				rule.setActive(false);
				rule.setPartitionId(null);
				ruleRepo.save(rule);
				auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Update_Rule,
				        "deactive rule " + ruleId);
			}

			auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Remove_Rule_From_Partition,
			        "remove rule " + ruleId + " from partition " + partition.getId());
			checkStale(partition);
		} catch (Exception e) {
			log.error("Fail to remove rule to parition: ", e);
			throw new AppException(e);
		}

	}

	@Override
	public PartitionDefinition removeRule(UserDefinition loginUser, Integer partitionId, Integer ruleId)
	        throws AppException {
		try {
			Assert.notNull(ruleId);

			PartitionDefinition partition = partitionDefRepo.findOne(partitionId);
			removeRule(loginUser, partition, ruleId);
			return partition;
		} catch (Exception e) {
			log.error("Fail to remove rule to parition: ", e);
			throw new AppException(e);
		}
	}

	@Override
	public PartitionDefinition getPartitionDetails(Integer partitionId) throws AppException {
		PartitionDefinition partition = partitionDefRepo.findOne(partitionId);
		if (partition == null) {
			throw new AppException("Invalid partition id " + partitionId);
		}

		partition.setPartitionExportHistories(exportRepo.findAllByOrigPartitionId(partition.getOrigPartitionId()));

		if (partition.getRuleIds() == null) {
			return partition;
		}
		for (String ruleId : partition.getRuleIds().split(",")) {
			partition.addRule(ruleRepo.findOne(Integer.valueOf(ruleId)));
		}

		return partition;
	}

	@Override
	public List<PartitionDefinition> getAllActivePartitions() {
		List<PartitionDefinition> partitions = partitionDefRepo.findAllActivePartitions();
		for (PartitionDefinition partition : partitions) {
			Integer origId = partition.getOrigPartitionId();
			if (origId == null) {
				origId = partition.getId();
			}
			partition.setPartitionExportHistories(exportRepo.findAllByOrigPartitionId(origId));

			String ruleIds = partition.getRuleIds();

			if (ruleIds != null) {
				for (String ruleId : ruleIds.split(",")) {
					partition.addRule(ruleRepo.findOne(Integer.valueOf(ruleId)));
				}
			}
		}

		return partitions;
	}

	@Transactional
	@Override
	public void savePartition(UserDefinition loginUser, PartitionDefinition partition) throws AppException {
		String action = AuditTrailActionDefinition.Create_Partition;

		if (partition.getId() != null) {
			action = AuditTrailActionDefinition.Update_Partition;
		} else {
			partition.setStatus(PartitionStatus.Fresh.value());
		}

		partition.setCustomerName(loginUser.getCustomerName());
		partition.setLastUpdated(DateTimeHelper.nowInUTC());
		partition.setLastUpdatedBy(loginUser.getUserName());

		partition = partitionDefRepo.save(partition);

		if (partition.getOrigPartitionId() == null) {
			partition.setOrigPartitionId(partition.getId());
			partitionDefRepo.save(partition);
		}

		auditService.saveAuditTrailLog(loginUser, action, "partition id: " + partition.getId());
	}

	private void updateRuleId(PartitionDefinition partition, Set<String> ruleIds, String userName) {
		partition.setRuleIds(StringUtils.collectionToCommaDelimitedString(ruleIds));
		partition.setLastUpdated(DateTimeHelper.nowInUTC());
		partition.setLastUpdatedBy(userName);
		partitionDefRepo.save(partition);
	}

	@Override
	public void checkStale(PartitionDefinition partition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkStale(ListDefinition listDefinition) {
		// TODO Auto-generated method stub

	}

	/*
	 * TODO: The following condition can cause staled draft data set: 1. BL/WL
	 * list change 2. Rule(s) change 3. New MobileID data set
	 */
	// no need
	private boolean checkPartitionStale(PartitionDefinition partition) {
		boolean isStale = false;

		Date draftTime = partition.getDraftDate();
		if (isStale) {
			partition.setStatus(PartitionStatus.Stale.value());
			partitionDefRepo.save(partition);
		}

		return isStale;
	}

	private byte[] readBytesFromFile(String filePath) throws IOException {
		File inputFile;
		FileInputStream inputStream = null;
		BufferedInputStream fstream = null;
		byte[] fileBytes = null;

		try {
			inputFile = new File(filePath);
			inputStream = new FileInputStream(inputFile);
			fstream = new BufferedInputStream(inputStream);
			fileBytes = new byte[(int) inputFile.length()];
			fstream.read(fileBytes);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			// releases any system resources associated with the stream
			if (inputStream != null)
				inputStream.close();
			if (fstream != null)
				fstream.close();
		}

		return fileBytes;
	}

	private static void saveBytesToFile(String filePath, byte[] fileBytes) throws IOException {
		FileOutputStream outputStream = new FileOutputStream(filePath);
		outputStream.write(fileBytes);
		outputStream.close();
	}

}
