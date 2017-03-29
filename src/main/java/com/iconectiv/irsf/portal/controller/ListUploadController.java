package com.iconectiv.irsf.portal.controller;

import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.core.MessageDefinition;
import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.ListDefintion;
import com.iconectiv.irsf.portal.model.customer.ListUploadRequest;
import com.iconectiv.irsf.portal.repositories.customer.ListDefinitionRepository;
import com.iconectiv.irsf.portal.service.ListService;
import com.iconectiv.irsf.util.JsonHelper;
import io.jsonwebtoken.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Map;

@Controller
class ListUploadController extends BaseRestController {
	private Logger log = LoggerFactory.getLogger(ListUploadController.class);

	@Autowired
	private ListService listService;

	@Autowired
	private ListDefinitionRepository listDefRepo;


	@RequestMapping(value = "/uploadListFile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> multipleSave(@RequestHeader Map<String, String> header, @RequestParam("file") MultipartFile[] files,   
	        @RequestParam("listType") String listType, @RequestParam("listName") String listName,
	        @RequestParam("listId") Integer id, @RequestParam("delimiter") String delimiter) {
		ResponseEntity<String> rv;
		try {
            Assert.notNull(listType);
            Assert.notNull(delimiter);

			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value());

            Integer listId;
            final boolean isInitialLoading;
            
			if (listName != null && id == null) {
				CustomerContextHolder.setSchema(loginUser.getSchemaName());				
				listId = listService.createListDefinition(loginUser, listName, listType);
				isInitialLoading = true;
			} else if (id != null){
				listId = id;
				isInitialLoading = false;
			} else {
				throw new AppException("Invalid request parameter: must provide either listName or listId");
			}

            Arrays.asList(files).stream().forEach(file -> {
				saveSingleFile(loginUser, listId, listType, file, delimiter, isInitialLoading);
			});
			rv = makeSuccessResult(MessageDefinition.Process_List_Upload);
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

	@Async
	private void saveSingleFile(UserDefinition user, final Integer listId, String type, MultipartFile file, String delimiter, boolean isInitialLoading) {
		try {
			ListDefintion listDef = listDefRepo.findOne(listId);
			
			if (listDef != null) {
				ListUploadRequest uploadRequest = listService.saveUploadRequest(user, listDef, file, delimiter);
				uploadRequest.setListDefintion(listDef);
				listService.processListUploadRequest(uploadRequest, isInitialLoading);
			} else {
				throw new AppException("Invalid list definition id " + listId);
			}
			
		} catch (Exception e) {
			log.error("Error to save file {}", file.getOriginalFilename(), e);
		}
		return;
	}
}
