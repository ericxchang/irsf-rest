package com.iconectiv.irsf.portal.controller;

import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.core.MessageDefinition;
import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.ListDefinition;
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
public class ListUploadController extends BaseRestController {
	private Logger log = LoggerFactory.getLogger(ListUploadController.class);

	@Autowired
	private ListService listService;

	@Autowired
	private ListDefinitionRepository listDefRepo;


	@RequestMapping(value = "/uploadListFile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> multipleSave(@RequestHeader Map<String, String> header, @RequestParam("file") MultipartFile[] files,   
	        @RequestParam("listType") String listType, @RequestParam("listName") String listName, @RequestParam("description") String description,
	        @RequestParam("listId") Integer id, @RequestParam("delimiter") String delimiter) {
		ResponseEntity<String> rv;
		try {
            if (log.isDebugEnabled()) log.debug("Received list upload request {}, {}, {}, {}, {}", id, listType, listName, description, delimiter);

            Assert.notNull(listName);
            Assert.notNull(delimiter);
            Assert.notNull(listType);

			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

            Integer listId;
            final boolean isInitialLoading;
			CustomerContextHolder.setSchema(loginUser.getSchemaName());				
            
			if (listName != null && id == null) {
				listId = listService.createListDefinition(loginUser, listName, description, listType);
				isInitialLoading = true;
			} else if (id != null){
				listId = id;
				listService.updateListName(loginUser, listId, listName, description);
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
		    log.error("error to process lit upload request:", e);
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled()) {
			log.debug("rest return: {} ", JsonHelper.toJson(rv));
		}
		return rv;
	}

	@Async
	public void saveSingleFile(UserDefinition user, final Integer listId, String type, MultipartFile file, String delimiter, boolean isInitialLoading) {
        if (log.isDebugEnabled()) log.debug("Processing list upload file {}, size: {}", file.getOriginalFilename(), file.getSize());

        try {
			ListDefinition listDef = listDefRepo.findOne(listId);
			
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
