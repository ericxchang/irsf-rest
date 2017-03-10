package com.iconectiv.irsf.portal.controller;

import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.customer.ListDefintion;
import com.iconectiv.irsf.portal.model.customer.ListUploadRequest;
import com.iconectiv.irsf.portal.repositories.customer.ListDefinitionRepository;
import com.iconectiv.irsf.portal.service.ListService;
import com.iconectiv.irsf.portal.util.JsonHelper;
import io.jsonwebtoken.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Controller
class ListUploadController extends BaseRestController {
	private Logger log = LoggerFactory.getLogger(ListUploadController.class);

	@Autowired
	private ListService listService;

	@Autowired
	private ListDefinitionRepository listDefRepo;
	
	@Autowired
	private Environment env;

	//TODO decode user from token
	private String guiUser = "test";

	@RequestMapping(value = "/uploadListFile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> multipleSave(@RequestParam("file") MultipartFile[] files,
	        @RequestParam("customer") String customer, @RequestParam("listType") String listType, @RequestParam("listName") String listName,
	        @RequestParam("listId") Integer id, @RequestParam("delimiter") String delimiter) {
		ResponseEntity<String> rv;
		try {
            Assert.notNull(customer);
            Assert.notNull(listType);
            Assert.notNull(delimiter);

            Integer listId;
			if (listName != null && id == null) {
				listId = listService.createListDefinition(customer, listName, listType, guiUser);
			} else if (!id.equals(null)){
				listId = id;
			} else {
				throw new AppException("Invalid request parameter: must provide either listName or listId");
			}

            Arrays.asList(files).stream().forEach(file -> {
				saveSingleFile(customer, listId, listType, file, delimiter);
			});
			rv = makeSuccessResult();
		} catch (Exception e) {
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled()) {
			log.debug(JsonHelper.toJson(rv));
		}
		return rv;
	}

	@Async
	private void saveSingleFile(String customer, final Integer listId, String type, MultipartFile file, String delimiter) {
		try {
			ListDefintion listDef = listDefRepo.findOne(listId);
			
			if (listDef != null) {
				ListUploadRequest uploadRequest = listService.saveUploadRequest(customer, listDef, file, delimiter, guiUser);
				listService.processListUploadRequest(uploadRequest);
			} else {
				throw new AppException("Invalid list definition id " + listId);
			}
			
		} catch (Exception e) {
			log.error("Error to save file {}", file.getOriginalFilename(), e);
		}
		return;
	}
}
