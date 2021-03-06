package com.iconectiv.irsf.portal.controller;

import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.core.AuditTrailActionDefinition;
import com.iconectiv.irsf.portal.core.ListType;
import com.iconectiv.irsf.portal.core.MessageDefinition;
import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.ListDefinition;
import com.iconectiv.irsf.portal.model.customer.ListDetails;
import com.iconectiv.irsf.portal.model.customer.ListUploadRequest;
import com.iconectiv.irsf.portal.repositories.customer.ListDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.customer.ListDetailsRepository;
import com.iconectiv.irsf.portal.repositories.customer.ListUploadRequestRepository;
import com.iconectiv.irsf.portal.service.AuditTrailService;
import com.iconectiv.irsf.portal.service.ListService;
import com.iconectiv.irsf.util.DateTimeHelper;
import com.iconectiv.irsf.util.JsonHelper;
import com.iconectiv.irsf.util.ListDetailConvert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
class ListServiceController extends BaseRestController {
	private Logger log = LoggerFactory.getLogger(ListServiceController.class);

	@Autowired
	private ListService listService;
	@Autowired
	private ListDetailsRepository listRepo;
	@Autowired
	private ListUploadRequestRepository listUploadRepo;
	@Autowired
	private ListDefinitionRepository listDefRepo;
	@Autowired
	private ListDetailsRepository listDetailRepo;
	@Autowired
	private AuditTrailService auditService;

	/*
	 * for this release, only support 3 Black lists and 3 white lists per customer
	 */
	@RequestMapping(value = "/lists/{listType}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getTopList(@RequestHeader Map<String, String> header, @PathVariable String listType) {
		ResponseEntity<String> rv;
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());
			List<ListDefinition> topLists;
			
			if (listType.equals(ListType.Black.value())) {
				topLists = listService.getTop3ListDefinition(ListType.Black.value());
			} else {
				topLists = listService.getTop3ListDefinition(ListType.White.value());
			}
			rv = makeSuccessResult(MessageDefinition.Query_Success, topLists);
		} catch (SecurityException e) {
			rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			rv = makeErrorResult(e);
		}

		if (log.isTraceEnabled()) {
			log.trace(JsonHelper.toPrettyJson(rv));
		}
		return rv;
	}


	@RequestMapping(value = "/listUploadRequest/{listId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getListUploadRequestByListId(@RequestHeader Map<String, String> header, @PathVariable int listId) {
		ResponseEntity<String> rv;
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());
			List<ListUploadRequest> listUploadRequests = listUploadRepo.findAllByListRefIdOrderByLastUpdatedDesc(listId);
			rv = makeSuccessResult(MessageDefinition.Query_Success, listUploadRequests);
		} catch (SecurityException e) {
			rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			rv = makeErrorResult(e);
		}

		return rv;
	}

	@RequestMapping(value = "/latestUploadRequest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getLatestListUploadRequestByListId(@RequestHeader Map<String, String> header, 
			 @RequestParam(value = "listId", required = false) Integer listId,
			 @RequestParam(value = "listName", required = false) String listName) {
		ResponseEntity<String> rv;
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());
			
			if (listId == null) {
				ListDefinition listDef = listDefRepo.findOneByListName(listName);
				listId = listDef.getId();
			}
			
			ListUploadRequest listUploadRequest = listUploadRepo.findTop1ByListRefIdOrderByLastUpdatedDesc(listId);
			rv = makeSuccessResult(MessageDefinition.Query_Success, listUploadRequest);
		} catch (SecurityException e) {
			rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			rv = makeErrorResult(e);
		}

		return rv;
	}

	@RequestMapping(value = "/hasListDetails/{listId}/{dialPattern}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> hasListDetail(@RequestHeader Map<String, String> header, @PathVariable int listId, String dialPattern) {
		ResponseEntity<String> rv;
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());
			
			ListDetails listDetail = listDetailRepo.findOneByListRefIdAndDialPattern(listId, dialPattern);
			
			if (log.isDebugEnabled()) log.debug("Found existing list record: " + JsonHelper.toJson(listDetail));
			rv = makeSuccessResult(MessageDefinition.Query_Success, listDetail);
		} catch (SecurityException e) {
			rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			rv = makeErrorResult(e);
		}

		return rv;
	}


	@RequestMapping(value = "/dialPattern", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getListDetailByDialPattern(@RequestHeader Map<String, String> header, @RequestBody String value) {
		ResponseEntity<String> rv;
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());
			ListDetails listDetailData = JsonHelper.fromJson(value, ListDetails.class);
			
			listService.getListDetailDataByDialPattern(listDetailData);
			rv = makeSuccessResult(MessageDefinition.Query_Success, listDetailData);
		} catch (SecurityException e) {
			rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			rv = makeErrorResult(e);
		}

		return rv;
	}

	
	@RequestMapping(value = "/listDetail/{listId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getListDataByListId(@RequestHeader Map<String, String> header, @PathVariable int listId) {
		ResponseEntity<String> rv;
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());
			List<ListDetails> listDetailData = listService.getListDetailDataByListId(listId);
			rv = makeSuccessResult(MessageDefinition.Query_Success, listDetailData);
		} catch (SecurityException e) {
			rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			rv = makeErrorResult(e);
		}

		return rv;
	}

	@Value("${jdbc.gui_query_batch_size:10000}")
	private int batchSize;

	@RequestMapping(value = "/listDetail", method = RequestMethod.GET)
	public ResponseEntity<String> getListDetailsByPage(@RequestHeader Map<String, String> header, @RequestParam(value = "pageNo", required = false) Integer pageNo,
	        @RequestParam(value = "limit", required = false) Integer limit, @RequestParam(value = "id", required = true) Integer listId) {
		ResponseEntity<String> rv;
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());
			CustomerContextHolder.setSchema(loginUser.getSchemaName());

			if (pageNo == null) {
				pageNo = 0;
			}

			if (limit == null) {
				limit = batchSize;
			}

			PageRequest page = new PageRequest(pageNo, limit);

			Page<Object[]> listData = listRepo.findAllDetailsByListRefId(listId, page);
	    	
	    	final Page<ListDetails> results = listData.map(new ListDetailConvert());

			rv = makeSuccessResult(results);
		} catch (NullPointerException e1) {
			rv = makeErrorResult(e1);
		} catch (Exception e) {
			rv = makeErrorResult(e);
		}
		if (log.isTraceEnabled()) {
			log.trace(JsonHelper.toPrettyJson(rv));
		}
		return rv;
	}
	

	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getListDefintiontDetails(@RequestHeader Map<String, String> header, 
			@RequestParam(value = "listId", required = false) Integer listId, 
			@RequestParam(value = "listName", required = false) String listName) {
		ResponseEntity<String> rv;
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());
			
			ListDefinition listDef;
			
			if (listName != null) {
				listDef = listService.getListDetails(listName);				
			} else if (listId != null) {
				listDef = listService.getListDetails(listId);				
			} else {
				throw new AppException("Either listName or listId should be defined");
			}
			rv = makeSuccessResult(MessageDefinition.Query_Success, listDef);
		} catch (SecurityException e) {
			rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			rv = makeErrorResult(e);
		}

		if (log.isTraceEnabled()) {
			log.trace(JsonHelper.toPrettyJson(rv));
		}
		return rv;
	}


    @RequestMapping(value = "/list/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> updateListDefinitionRequest(@RequestHeader Map<String, String> header, @RequestBody String value) {
        ResponseEntity<String> rv;
        try {
            UserDefinition loginUser = getLoginUser(header);
            assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

            CustomerContextHolder.setSchema(loginUser.getSchemaName());
        	ListDefinition listDef = JsonHelper.fromJson(value, ListDefinition.class);
        	boolean isNewList = false;

        	if (listDef.getId() == null) {
        		isNewList = true;
        		listDef.setActive(true);
        		listDef.setCreateBy(loginUser.getUserName());
        		listDef.setCreateTimestamp(DateTimeHelper.nowInUTC());
        		listDef.setCustomerName(loginUser.getCustomerName());
        	}

            listDef.setLastUpdated(DateTimeHelper.nowInUTC());
            listDef.setLastUpdatedBy(loginUser.getUserName());
			listDefRepo.save(listDef);

			if (isNewList) {
                auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Create_List_Definition, "created new list id " + listDef.getId());
            } else {
                auditService.saveAuditTrailLog(loginUser, AuditTrailActionDefinition.Update_List_Definition, "updated list id " + listDef.getId());
            }
            
            rv = makeSuccessResult(MessageDefinition.Rename_List_Success, listDef);
        } catch (SecurityException e) {
            rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            rv = makeErrorResult(e);
        }

        return rv;
    }

    @CrossOrigin
	@RequestMapping(value = "/list", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> deleteListByNameRequest(@RequestHeader Map<String, String> header,
			@RequestParam(value = "listId", required = false) Integer listId, 
			@RequestParam(value = "listName", required = false) String listName) {

		ResponseEntity<String> rv;
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());
			
			if (listName != null) {
				listService.deleteListDefinition(loginUser, listName);
			} else if (listId != null) {
				listService.deleteListDefinition(loginUser, listId);
			} else {
				throw new AppException("Either listName or listId should be defined");
			}
	
			
			rv = makeSuccessResult(MessageDefinition.Delete_List_Success);
		} catch (SecurityException e) {
			rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled()) {
			log.debug(JsonHelper.toJson(rv));
		}
		return rv;
	}

    @RequestMapping(value = "/listDetails/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> createListDetailRecordsRequest(@RequestHeader Map<String, String> header, @RequestBody String value) {
        ResponseEntity<String> rv;
        try {
        	if (log.isDebugEnabled()) log.debug("Receive data: " + value);
        	ListDetails[] listDetails = JsonHelper.fromJson(value, ListDetails[].class);
        	
        	if (listDetails.length < 1) {
        		throw new AppException("received blank list");
        	}
            UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

            CustomerContextHolder.setSchema(loginUser.getSchemaName());
            
			Iterable<ListDetails> result = listService.createListDetails(loginUser, listDetails);

            rv = makeSuccessResult(MessageDefinition.Update_ListDetails_Success, result);
        } catch (SecurityException e) {
            rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            rv = makeErrorResult(e);
        }

        if (log.isDebugEnabled()) {
            log.debug(JsonHelper.toJson(rv));
        }
        return rv;
    }

    @RequestMapping(value = "/listDetails/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> updateListDetailRecordsRequest(@RequestHeader Map<String, String> header, @RequestBody String value) {
        ResponseEntity<String> rv;
        try {
        	ListDetails[] listDetails = JsonHelper.fromJson(value, ListDetails[].class);
            UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

            CustomerContextHolder.setSchema(loginUser.getSchemaName());
            
            listService.updateListDetails(loginUser, listDetails);
            
            rv = makeSuccessResult(MessageDefinition.Update_ListDetails_Success, listDetails);
        } catch (SecurityException e) {
            rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            rv = makeErrorResult(e);
        }

        if (log.isDebugEnabled()) {
            log.debug(JsonHelper.toJson(rv));
        }
        return rv;
    }

    @CrossOrigin
    @RequestMapping(value = "/listDetails/delete", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> deleteListDetailRecordsRequest(@RequestHeader Map<String, String> header, @RequestBody String value) {
        ResponseEntity<String> rv;
        try {
        	ListDetails[] listDetails = JsonHelper.fromJson(value, ListDetails[].class);
            UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

            CustomerContextHolder.setSchema(loginUser.getSchemaName());
            
            listService.deleteListDetails(loginUser, listDetails);
            
            rv = makeSuccessResult(MessageDefinition.Delete_ListDetails_Success);
        } catch (SecurityException e) {
            rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            rv = makeErrorResult(e);
        }

        if (log.isDebugEnabled()) {
            log.debug(JsonHelper.toJson(rv));
        }
        return rv;
    }
}
