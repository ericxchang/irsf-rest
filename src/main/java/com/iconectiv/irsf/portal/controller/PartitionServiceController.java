package com.iconectiv.irsf.portal.controller;

import com.iconectiv.irsf.core.EIResponse;
import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.core.AppConstants;
import com.iconectiv.irsf.portal.core.MessageDefinition;
import com.iconectiv.irsf.portal.core.PartitionStatus;
import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.exception.AuthException;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.PartitionDataDetails;
import com.iconectiv.irsf.portal.model.customer.PartitionDefinition;
import com.iconectiv.irsf.portal.model.customer.PartitionExportHistory;
import com.iconectiv.irsf.portal.repositories.common.UserDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.customer.PartitionDataDetailsRepository;
import com.iconectiv.irsf.portal.repositories.customer.PartitionDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.customer.PartitionExportHistoryRepository;
import com.iconectiv.irsf.portal.service.PartitionExportService;
import com.iconectiv.irsf.portal.service.PartitionService;
import com.iconectiv.irsf.util.DateTimeHelper;
import com.iconectiv.irsf.util.JsonHelper;
import com.iconectiv.irsf.util.SerializeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
class PartitionServiceController extends BaseRestController {
	private Logger log = LoggerFactory.getLogger(PartitionServiceController.class);

	@Autowired
	private PartitionService partitionServ;
	@Autowired
	private PartitionExportService exportService;
	@Autowired
	private PartitionDefinitionRepository partitionDefRepo;
	@Autowired
    private PartitionExportHistoryRepository exportRepo;
	@Autowired
	private PartitionDataDetailsRepository partitionDataRepo;
    @Autowired
    private UserDefinitionRepository userRepo;
    @Autowired
    BCryptPasswordEncoder encoder;
    
      

    @RequestMapping(value = "/partition/{partitionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getPartitionDetails(@RequestHeader Map<String, String> header,
	        @PathVariable Integer partitionId) {
		ResponseEntity<String> rv;
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());
			PartitionDefinition partition = partitionServ.getPartitionDetails(partitionId);
			rv = makeSuccessResult(MessageDefinition.Query_Success, partition);
		} catch (SecurityException e) {
			rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			log.error("Error: ", e);
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled()) {
			log.debug(JsonHelper.toPrettyJson(rv));
		}
		return rv;
	}

	@CrossOrigin
	@RequestMapping(value = "/partition/{partitionId}/{ruleId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> removeRuleRequest(@RequestHeader Map<String, String> header,
	        @PathVariable Integer partitionId, @PathVariable Integer ruleId) {
		ResponseEntity<String> rv;
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());

			PartitionDefinition partition = partitionServ.removeRule(loginUser, partitionId, ruleId);
			partition = partitionServ.getPartitionDetails(partitionId);
			rv = makeSuccessResult(MessageDefinition.Remove_Rule, partition);
		} catch (SecurityException e) {
			rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			log.error("Error: ", e);
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled()) {
			log.debug(JsonHelper.toPrettyJson(rv));
		}
		return rv;
	}

	@CrossOrigin
	@RequestMapping(value = "/partition/{partitionId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> deletePartitionRequest(@RequestHeader Map<String, String> header,
													@PathVariable Integer partitionId) {
		ResponseEntity<String> rv;
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());

			partitionServ.deleteParitition(loginUser, partitionId);

			rv = makeSuccessResult(MessageDefinition.DELETE_PARTITION);
		} catch (SecurityException e) {
			rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			log.error("Error: ", e);
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled()) {
			log.debug(JsonHelper.toPrettyJson(rv));
		}
		return rv;
	}

	@RequestMapping(value = "/partitions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getPartitionList(@RequestHeader Map<String, String> header) {
		ResponseEntity<String> rv;

		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());

			List<PartitionDefinition> partitions = partitionServ.getAllActivePartitions();
			//log.info(JsonHelper.toPrettyJson(partitions));
			rv = makeSuccessResult(MessageDefinition.Query_Success, partitions);
		} catch (SecurityException e) {
			rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			log.error("Error: ", e);
			rv = makeErrorResult(e);
		}

		return rv;
	}

	@RequestMapping(value = "/partition/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> savePartitionRequest(@RequestHeader Map<String, String> header,
	        @RequestBody String value) {
		ResponseEntity<String> rv;
		try {
			PartitionDefinition partition = JsonHelper.fromJson(value, PartitionDefinition.class);
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());
            if (partition.getId() != null) {
                partitionServ.validateParitionStatus(partition);
            }
			partitionServ.savePartition(loginUser, partition);
			rv = makeSuccessResult(MessageDefinition.Save_Partition_Success, partition);
		} catch (SecurityException e) {
			rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
		} catch (AppException e) {
			log.error("Error:", e);
			rv = makeErrorResult(e);
		} catch (Exception e) {
			// TODO throw trap
			log.error("Error:", e);
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled()) {
			log.debug(JsonHelper.toJson(rv));
		}
		return rv;
	}

	@RequestMapping(value = "/partition/updateBlackList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> updatePartitionBlackListRequest(@RequestHeader Map<String, String> header,
	        @RequestBody String value) {
		ResponseEntity<String> rv;
		try {
			PartitionDefinition partition = JsonHelper.fromJson(value, PartitionDefinition.class);
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());

            partitionServ.validateParitionStatus(partition);
            partitionServ.savePartition(loginUser, partition);

			partitionServ.checkStale(loginUser, partition, " has different black list");

			rv = makeSuccessResult(MessageDefinition.Save_Partition_Success);
		} catch (SecurityException e) {
			rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
		} catch (AppException e) {
			log.error("Error:", e);
			rv = makeErrorResult(e);
		} catch (Exception e) {
			// TODO throw trap
			log.error("Error:", e);
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled()) {
			log.debug(JsonHelper.toJson(rv));
		}
		return rv;
	}

	@RequestMapping(value = "/partition/updateWhiteList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> updatePartitionWhiteListRequest(@RequestHeader Map<String, String> header,
	        @RequestBody String value) {
		ResponseEntity<String> rv;
		try {
			PartitionDefinition partition = JsonHelper.fromJson(value, PartitionDefinition.class);
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());

			partitionServ.validateParitionStatus(partition);
			partitionServ.savePartition(loginUser, partition);

			partitionServ.checkStale(loginUser, partition, "has different white list");

			rv = makeSuccessResult(MessageDefinition.Save_Partition_Success);
		} catch (SecurityException e) {
			rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
		} catch (AppException e) {
			log.error("Error:", e);
			rv = makeErrorResult(e);
		} catch (Exception e) {
			// TODO throw trap
			log.error("Error:", e);
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled()) {
			log.debug(JsonHelper.toJson(rv));
		}
		return rv;
	}

	@RequestMapping(value = "/partition/export/{partitionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> exportPartitionRequest(@RequestHeader Map<String, String> header,
	        @PathVariable Integer partitionId) {
		ResponseEntity<String> rv;
		if (log.isDebugEnabled()) log.debug("exportPartitionRequest(): partitionId: {}", partitionId);
		
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());

			PartitionDefinition partition = partitionDefRepo.findOne(partitionId);

			if (partition.getStatus().equals(PartitionStatus.InProgress.value())) {
				rv = makeErrorResult(MessageDefinition.Generating_Partition_Dataset_Success);
			} else {
				rv = makeSuccessResult(MessageDefinition.Exporting_Partition_Dataset_Success);
				partitionServ.exportPartition(loginUser, partitionId);
			}
		} catch (SecurityException e) {
			rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			log.error("Error:", e);
			log.debug("Total Memory: {}, Free Memory: {} ", (double) Runtime.getRuntime().totalMemory()/1024,  (double) Runtime.getRuntime().freeMemory()/ 1024);
			rv = makeErrorResult(e);
		}

		if (log.isTraceEnabled()) {
			log.trace(JsonHelper.toJson(rv));
		}
		return rv;
	}

	@RequestMapping(value = "/partition/resend/{exportPartitionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> resendPartitionRequest(@RequestHeader Map<String, String> header,
	        @PathVariable Integer exportPartitionId) {
		ResponseEntity<String> rv;
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());
	
			exportService.resendPartition(loginUser, exportPartitionId);
			rv = makeSuccessResult(MessageDefinition.Exporting_Partition_Dataset_Success);
		} catch (SecurityException e) {
			rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			log.error("Error:", e);
			log.debug("Total Memory: {}, Free Memory: {} ", (double) Runtime.getRuntime().totalMemory()/1024,  (double) Runtime.getRuntime().freeMemory()/ 1024);
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled()) {
			log.debug(JsonHelper.toJson(rv));
		}
		return rv;
	}

	@RequestMapping(value = "/exportData/{exportPartitionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getPartitionDataFullSetRequest(@RequestHeader Map<String, String> header, @PathVariable Integer exportPartitionId) {
		ResponseEntity<String> rv;
		try {
		    if (log.isDebugEnabled()) log.debug("receiving download export data request {}", exportPartitionId);

			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());

			byte[] partitionData = exportRepo.findPartitonExportFullSet(exportPartitionId);
			List<PartitionDataDetails> dataSet = (List<PartitionDataDetails>) SerializeHelper.deserialize(partitionData);
            if (partitionData.length > 0) {
                rv = makeSuccessResult(MessageDefinition.Query_Success, dataSet);
            } else {
                throw new AppException("No data found");
            }
		} catch (SecurityException e) {
			rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			log.error("Error:", e);
			log.debug("Total Memory: {}, Free Memory: {} ", (double) Runtime.getRuntime().totalMemory()/1024,  (double) Runtime.getRuntime().freeMemory()/ 1024);
			rv = makeErrorResult(e);
		}

		return rv;
	}

	@RequestMapping(value = "/blockingfile/{exportPartitionId}", method = RequestMethod.GET, produces = "application/octet-stream")
    public @ResponseBody HttpEntity<byte[]> downloadPartitionExportData(@RequestHeader Map<String, String> header, @PathVariable Integer exportPartitionId) throws Exception{
        if (log.isDebugEnabled()) log.debug("receiing download blocking file request {}", exportPartitionId);
        try {
            UserDefinition loginUser = getLoginUser(header);
            assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

            CustomerContextHolder.setSchema(loginUser.getSchemaName());

            PartitionExportHistory exportHistory = exportRepo.findOne(exportPartitionId);

            if (exportHistory == null) {
                throw new AppException("invalid export id " + exportPartitionId);
            }

            String outputFileName  = loginUser.getCustomerId() + "_" + exportHistory.getPartitionId() + "_" + DateTimeHelper.formatDate(new Date(), "yyyyMMdd_HHmmss");


            byte[] documentBody = exportService.createExportFiles(loginUser, exportHistory, outputFileName);

            HttpHeaders respHeader = new HttpHeaders();

            respHeader.set("Content-Disposition", "attachment; filename=" + outputFileName);
            respHeader.setContentLength(documentBody.length);

            return new HttpEntity<byte[]>(documentBody, respHeader);

        } catch(Exception e) {
            log.error("Error to download partition export data: ", e);
        }

        return null;
    }


    @RequestMapping(value = "/partition/refresh/{partitionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> refreshPartitionRequest(@RequestHeader Map<String, String> header,
	        @PathVariable Integer partitionId) {
		ResponseEntity<String> rv;
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());

			PartitionDefinition partition = partitionDefRepo.findOne(partitionId);

			if (partition.getStatus().equals(PartitionStatus.InProgress.value())) {
				rv = makeErrorResult(MessageDefinition.Generating_Partition_Dataset_Success);
			} else {
				partitionServ.refreshPartition(loginUser, partitionId);
				rv = makeSuccessResult(MessageDefinition.Generating_Partition_Dataset_Success);
			}
			
		} catch (SecurityException e) {
			rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			log.error("Error:", e);
			log.debug("Total Memory: {}, Free Memory: {} ", (double) Runtime.getRuntime().totalMemory()/1024,  (double) Runtime.getRuntime().freeMemory()/ 1024);
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled()) {
			log.debug("return from partition refresh call: " + JsonHelper.toJson(rv));
		}
		return rv;
	}

    @Value("${jdbc.gui_query_batch_size:10000}")
	private int batchSize;

	@RequestMapping(value = "/draftdata", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getDraftDataRequest(@RequestHeader Map<String, String> header,
	        @RequestParam(value = "pageNo", required = false) Integer pageNo,
	        @RequestParam(value = "limit", required = false) Integer limit,
	        @RequestParam(value = "id", required = true) Integer partitionId) {
		ResponseEntity<String> rv;
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());

			PartitionDefinition partition = partitionDefRepo.findOne(partitionId);

			if (partition.getStatus().equals(PartitionStatus.InProgress.value())) {
				rv = makeSuccessResult(MessageDefinition.Generating_Partition_Dataset_Success);
			}
			else if (partition.getStatus().equals(PartitionStatus.Stale.value())
			        || partition.getStatus().equals(PartitionStatus.Fresh.value())) {
				rv = makeSuccessResult(MessageDefinition.Generating_Partition_Dataset_Success);
				partitionServ.refreshPartition(loginUser, partitionId);
			} else {
				if (pageNo == null) {
					pageNo = 0;
				}

				if (limit == null) {
					limit = batchSize;
				}

				PageRequest page = new PageRequest(pageNo, limit);

				Page<PartitionDataDetails> result = partitionDataRepo.findAllByPartitionId(partitionId, page);
				rv = makeSuccessResult(result);
			}
		} catch (SecurityException e) {
			rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			log.error("Error:", e);
			log.debug("Total Memory: {}, Free Memory: {} ", (double) Runtime.getRuntime().totalMemory()/1024,  (double) Runtime.getRuntime().freeMemory()/ 1024);
			rv = makeErrorResult(e);
		}

		return rv;
	}

    @RequestMapping(value = "/loadstatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> updateExportStatusRequest(@RequestHeader Map<String, String> header, @RequestBody String value) {
        ResponseEntity<String> rv;
        EIResponse eiStatus = new EIResponse();
        try {
            eiStatus = JsonHelper.fromJson(value, EIResponse.class);
            String userId = header.get("userId");
            String password = header.get("password");
            UserDefinition loginUser = userRepo.findOneByUserName(userId);

            if (loginUser == null) {
                throw new AuthException("Invalid user Id");
            }

            if (loginUser.isDisabled()) {
                throw new AuthException("The User Id has been disabled");
            }

            if (loginUser.isLocked()) {
                throw new AuthException("The User Id has been locked");
            }

            if (!encoder.matches(password, loginUser.getPassword())) {
                throw new AuthException("Password is NOT correct");
            }

            assertAuthorized(loginUser, PermissionRole.API.value());
            loginUser.setSchemaName(eiStatus.getCustomer());

            CustomerContextHolder.setSchema(loginUser.getSchemaName());

            exportService.updateStatus(loginUser, eiStatus);

            rv = new ResponseEntity<>(JsonHelper.toJson(eiStatus), HttpStatus.OK);
        } catch (SecurityException e) {
            eiStatus.setStatus(AppConstants.FAIL);
            eiStatus.setMessage(e.getMessage());
            rv = new ResponseEntity<>(JsonHelper.toJson(eiStatus), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            eiStatus.setStatus(AppConstants.FAIL);
            eiStatus.setMessage(e.getMessage());
            rv = new ResponseEntity<>(JsonHelper.toJson(eiStatus), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (log.isDebugEnabled()) {
            log.debug(JsonHelper.toJson(rv));
        }
        return rv;
    }

}
