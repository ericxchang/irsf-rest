package com.iconectiv.irsf.portal.controller;

import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.core.ListType;
import com.iconectiv.irsf.portal.core.MessageDefinition;
import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.ListDefintion;
import com.iconectiv.irsf.portal.model.customer.ListDetails;
import com.iconectiv.irsf.portal.model.customer.RuleDefinition;
import com.iconectiv.irsf.portal.repositories.customer.ListDefinitionRepository;
import com.iconectiv.irsf.portal.service.AuditTrailService;
import com.iconectiv.irsf.portal.service.ListService;
import com.iconectiv.irsf.portal.service.ListUploadService;
import com.iconectiv.irsf.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
class ListServiceController extends BaseRestController {
	private Logger log = LoggerFactory.getLogger(ListServiceController.class);

	@Autowired
	private ListService listService;
	@Autowired
	private ListDefinitionRepository listRepo;
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
				topLists = listRepo.findTop3ByTypeAndActiveOrderByLastUpdatedDesc(ListType.Black.value(), true);
			} else {
				topLists = listRepo.findTop3ByTypeAndActiveOrderByLastUpdatedDesc(ListType.White.value(), true);
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


	@RequestMapping(value = "/list/{listName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getListDetails(@RequestHeader Map<String, String> header, @PathVariable String listName) {
		ResponseEntity<String> rv;
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());
			ListDefintion listDef = listService.getListDetails(listName);
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


	@RequestMapping(value = "/list/{listName}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> deleteListByNameRequest(@RequestHeader Map<String, String> header, @PathVariable String listName) {
		ResponseEntity<String> rv;
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());
			listService.deleteListDefinition(listName);
			rv = makeSuccessResult(MessageDefinition.Delete_List_Success);
			auditService.saveAuditTrailLog(loginUser.getUserName(), loginUser.getCustomerName(), "delete list", listName);
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
