package com.iconectiv.irsf.portal.controller;

import com.iconectiv.irsf.portal.model.common.ListUploadRequest;
import com.iconectiv.irsf.portal.service.FileHandlerService;
import com.iconectiv.irsf.portal.service.ListService;
import com.iconectiv.irsf.portal.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Controller
class FileUploadController extends BaseRestController {
	private Logger log = LoggerFactory.getLogger(FileUploadController.class);

	@Autowired
	private ListService listService;

	@Autowired
	private FileHandlerService fileService;

	@Autowired
	private Environment env;

	@RequestMapping(value = "/uploadBlackList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> multipleSave(@RequestParam("file") MultipartFile[] files,
	        @RequestParam("customer") String customer) {
		ResponseEntity<String> rv;
		try {
			Arrays.asList(files).stream().parallel().forEach(file ->
				saveSingleFile(customer, "blacklist", file)
			);
			rv = makeSuccessResult();
		} catch (Exception e) {
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled()) {
			log.debug(JsonHelper.toJson(rv));
		}
		return rv;
	}


	private void saveSingleFile(String customer, String type, MultipartFile file) {
		try {
			String fileLocation = env.getProperty("uploadList.path") + "/" + customer;

            List<String> contents = fileService.saveTextFile(fileLocation, file);
			if (!contents.isEmpty()) {
				ListUploadRequest request = listService.saveUploadRequest(customer, file.getOriginalFilename(), type, fileLocation);
				listService.parseBlackList(request, contents);
			}
		} catch (Exception e) {
			log.error("Error to save file {}", file.getOriginalFilename(), e);
		}
		return;
	}
}
