package com.iconectiv.irsf.portal.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.core.ListType;
import com.iconectiv.irsf.portal.core.MessageDefinition;
import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.ListDefintion;
import com.iconectiv.irsf.portal.model.customer.ListDetails;
import com.iconectiv.irsf.portal.model.customer.ListUploadRequest;
import com.iconectiv.irsf.portal.repositories.customer.ListDetailsRepository;
import com.iconectiv.irsf.portal.repositories.customer.ListUploadRequestRepository;
import com.iconectiv.irsf.portal.service.AuditTrailService;
import com.iconectiv.irsf.portal.service.ListService;
import com.iconectiv.irsf.portal.service.ListUploadService;
import com.iconectiv.irsf.util.JsonHelper;
import com.iconectiv.irsf.util.ListDetailConvert;

@Controller
class ListServiceController extends BaseRestController {
	private Logger log = LoggerFactory.getLogger(ListServiceController.class);

	@Autowired
	private ListService listService;
	@Autowired
	private ListDetailsRepository listRepo;
	@Autowired
	private ListUploadRequestRepository listUploadRepo;
	
	@Autowired
	private ListUploadService uploadService;
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
			List<ListDefintion> topLists;
			
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

		if (log.isDebugEnabled()) {
			log.debug(JsonHelper.toPrettyJson(rv));
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

	@Value("${jdbc.query_batch_size:10000}")
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
		if (log.isDebugEnabled()) {
			log.debug(JsonHelper.toPrettyJson(rv));
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
			
			ListDefintion listDef;
			
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

		if (log.isDebugEnabled()) {
			log.debug(JsonHelper.toPrettyJson(rv));
		}
		return rv;
	}


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
				listService.deleteListDefinition(listName);
			} else if (listId != null) {
				listService.deleteListDefinition(listId);
			} else {
				throw new AppException("Either listName or listId should be defined");
			}
	
			
			rv = makeSuccessResult(MessageDefinition.Delete_List_Success);
			auditService.saveAuditTrailLog(loginUser.getUserName(), loginUser.getCustomerName(), "delete list", "successfully remove list " + listId);
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

    @RequestMapping(value = "/list/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> saveRuleRequest(@RequestHeader Map<String, String> header, @RequestBody String value) {
        ResponseEntity<String> rv;
        try {
        	ListDetails listDetail = JsonHelper.fromJson(value, ListDetails.class);
            UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

            CustomerContextHolder.setSchema(loginUser.getSchemaName());
            uploadService.validateListEntry(listDetail);
            listService.saveListEntry(loginUser, listDetail);
            
            rv = makeSuccessResult(MessageDefinition.Save_Rule_Success, listDetail);
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
