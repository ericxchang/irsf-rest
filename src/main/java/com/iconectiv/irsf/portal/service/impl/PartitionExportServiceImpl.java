package com.iconectiv.irsf.portal.service.impl;

import com.iconectiv.irsf.core.ByteFile;
import com.iconectiv.irsf.core.EIResponse;
import com.iconectiv.irsf.json.vaidation.JsonValidationException;
import com.iconectiv.irsf.portal.core.AppConstants;
import com.iconectiv.irsf.portal.core.AuditTrailActionDefinition;
import com.iconectiv.irsf.portal.core.EventTypeDefinition;
import com.iconectiv.irsf.portal.core.PartitionExportStatus;
import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.common.CustomerDefinition;
import com.iconectiv.irsf.portal.model.common.HttpResponseMessage;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.PartitionExportHistory;
import com.iconectiv.irsf.portal.repositories.common.CustomerDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.customer.PartitionExportHistoryRepository;
import com.iconectiv.irsf.portal.service.AuditTrailService;
import com.iconectiv.irsf.portal.service.EventNotificationService;
import com.iconectiv.irsf.portal.service.FileHandlerService;
import com.iconectiv.irsf.portal.service.PartitionExportService;
import com.iconectiv.irsf.util.DateTimeHelper;
import com.iconectiv.irsf.util.JsonHelper;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class PartitionExportServiceImpl implements PartitionExportService {
	private static Logger log = LoggerFactory.getLogger(PartitionExportServiceImpl.class);

	@Value("${export.file.path:/apps/irsf/data/export/}")
	private String exportFilePath;

	@Autowired
	private EventNotificationService eventService;
	@Autowired
	private PartitionExportHistoryRepository exportRepo;
	@Autowired
	private AuditTrailService auditService;
	@Autowired
	private FileHandlerService fileService;
    @Autowired
    private CustomerDefinitionRepository customerRepo;

	@Override
    @Transactional
	public void resendPartition(UserDefinition loginUser, Integer exportPartitionId) {
		CustomerDefinition customer = customerRepo.findByCustomerName(loginUser.getCustomerName());
		log.info("exportPartition: exportPartitionId: {}", exportPartitionId);
		PartitionExportHistory partHist = exportRepo.findOne(exportPartitionId);

		if (customer.getExportTarget() != null) {
			log.info("exportPartition: calling sendExportFile2EI(): partitionId: {}", partHist.getPartitionId());
			sendExportFile2EI(loginUser, partHist, customer.getExportTarget());
		}

	}



	@Transactional
	@Override
	public void sendExportFile2EI(UserDefinition loginUser, PartitionExportHistory partHist, String url) {
		if (loginUser == null || partHist == null || url == null || "".equals(url))
			return;
		
		try {
            String fileName = loginUser.getCustomerId() + "_" + partHist.getPartitionId() + "_" + DateTimeHelper.formatDate(new Date(), "MMddyy_HHmmss");
			byte[] data = createExportFiles(loginUser, partHist, fileName);

            // if no file was added to the list, return error
            if (data == null || data.length < 1) {
                log.warn("sendExportFile2EI: no data to sent to blocking system");
                return;
            }

            // if everything is OK, zip the file and send it to EI server
            String zipFileName = fileName + ".zip";
           

            url = url + "?customer=" + loginUser.getSchemaName() + "&partition=" + partHist.getId();
            
            log.info("send file to EI: {}, file name: {}", url, zipFileName) ;
            
            EIResponse response = uploadFiles(zipFileName, data, url, loginUser.getCustomerName(), partHist.getId());
            partHist.setReferenceId(response.getId());
            partHist.setReason(response.getMessage());

            if (response.getStatus().equals(AppConstants.SUCCESS)) {
                partHist.setStatus(PartitionExportStatus.Exported.value());
            } else {
                partHist.setStatus(PartitionExportStatus.Failed.value());
            }

            exportRepo.save(partHist);

            auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Send_Partition_Data_To_EI, response.getMessage());

			eventService.sendPartitionEvent(loginUser, partHist.getPartitionId(), EventTypeDefinition.Partition_PushToEI.value(), response.getMessage());
		} catch (Exception e) {
			log.error("Error to send to EI", e);
			partHist.setStatus(PartitionExportStatus.Failed.value());
			exportRepo.save(partHist);
			
			auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Send_Partition_Data_To_EI, e.getMessage());
		}
		return;
	}

	@Override
    public byte[] createExportFiles(UserDefinition loginUser, PartitionExportHistory partHist, String fileName) {
	    List<ByteFile> files = new ArrayList<>();
        // create file 1 from ExportFileShort
	    String exportfileName = "";
        if (partHist.getExportFileShort() != null && partHist.getExportFileShort().length > 0) {
        	exportfileName = fileName + "_screeninglist.csv";
        	log.info("create export short file:  {}", exportfileName) ;
            files.add(new ByteFile(exportfileName, partHist.getExportFileShort()));
        }

        // create file 2 from ExportWhitelist
        if (partHist.getExportWhitelist() != null && partHist.getExportWhitelist().length > 0) {
        	exportfileName = fileName + "_exceptionlist.csv";
        	log.info("create export white list file:  {}", exportfileName) ;
            files.add(new ByteFile(exportfileName, partHist.getExportWhitelist()));
        }

        return fileService.zipFile(files);
    }

    @Override
	public EIResponse uploadFiles(String uploadFleName, byte[] data, String url, String customer, Integer exportPartitionId) {
        EIResponse response = new EIResponse();
		//RestTemplate restTemplate = new RestTemplate();
        CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);

        RestTemplate restTemplate = new RestTemplate(requestFactory); 
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

        /* TODO add userId and password header
       MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
headers.add("Authorization", "Basic " + base64Creds);
headers.add("Content-Type", "application/json");

RestTemplate restTemplate = new RestTemplate();
restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

HttpEntity<ObjectToPass> request = new HttpEntity<ObjectToPass>(ObjectToPass, headers);
         */
        ByteArrayResource contentsAsResource = new ByteArrayResource(data){
            @Override
            public String getFilename(){
                return uploadFleName;
            }
        };
        map.add("file", contentsAsResource);
        //map.add("customer", customer);
        //map.add("partition", exportPartitionId);

        try {
            response = restTemplate.postForObject(url, map, EIResponse.class);
            log.info("received reponse from EI: {}", JsonHelper.toJson(response));
        } catch (Exception e) {
            log.error("Error to send file to EI:", e.getMessage());
            response.setStatus(AppConstants.FAIL);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public void updateStatus(UserDefinition loginUser, EIResponse eiStatus) throws AppException {
        PartitionExportHistory exportHistory = exportRepo.findOne(eiStatus.getPartition());

        if (exportHistory == null) {
            throw  new AppException("invalid export partition id " + eiStatus.getPartition());
        }

        exportHistory.setReason(eiStatus.getMessage());

        if (eiStatus.getStatus().equals(AppConstants.SUCCESS)) {
			exportHistory.setStatus(PartitionExportStatus.Exported.value());
		} else {
			exportHistory.setStatus(PartitionExportStatus.Failed.value());
		}

        exportRepo.save(exportHistory);

        return;
    }

	@Override
	public void cleanExportHistory(CustomerDefinition customer) {
		List<Integer> origPartitionIdList = exportRepo.findAllOrigPartitionId();
		origPartitionIdList.forEach(origId -> {
			List<Integer> exportIds = exportRepo.findAllIdByOrigPartitionId(origId);
			log.info("Export IDs for partition {}: {}", origId, exportIds);
			if (exportIds.size() >  AppConstants.MAX_NO_OF_EXPORT_HOSTORY) {
				exportIds.remove(0);
				exportIds.remove(0);
				log.info("Will remove export IDs for partition {}: {}", origId, exportIds);
				exportRepo.deleteByIdIn(exportIds);
				auditService.saveAuditTrailLog("system", customer.getCustomerName(), AuditTrailActionDefinition.Delete_Export_History, "deleted export history record " + exportIds);
			}
		});
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


}
