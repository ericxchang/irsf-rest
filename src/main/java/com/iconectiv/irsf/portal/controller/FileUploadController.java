package com.iconectiv.irsf.portal.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.iconectiv.irsf.portal.config.SpringConfig;
import com.iconectiv.irsf.portal.model.common.ListUploadRequest;
import com.iconectiv.irsf.portal.service.ListService;

@Controller
class FileUploadController extends BaseRestController {
	private Logger log = LoggerFactory.getLogger(FileUploadController.class);

	@Autowired
	private ListService listService;
	
	@Autowired
	private Environment env;

	@RequestMapping(value = "/uploadBlackList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> multipleSave(@RequestParam("file") MultipartFile[] files, @RequestParam("customer") String customer) {
		ResponseEntity<String> rv;
		try {
			for (MultipartFile file : files) {
				saveSingleFile(customer, file);			
			}
			rv = makeSuccessResult();
		} catch (Exception e) {
			rv = makeErrorResult(e);
		}

		return rv;
	}

	@Async
	private void saveSingleFile(String customer, MultipartFile file) throws Exception {
		String fileName = file.getOriginalFilename();
		File fileDir =  new File(env.getProperty("uploadList.path") + "/" + customer);
		String fileLocation = env.getProperty("uploadList.path") + "/" + customer + "/" + fileName;
		log.info("Saving upload list file {}", fileName);
		
		if (!fileDir.exists()) {
			log.info("Create directory {]", fileDir.getAbsolutePath());
			fileDir.mkdirs();
		} 
		
		byte[] bytes = file.getBytes();
		BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(new File(fileLocation)));
		buffStream.write(bytes);
		buffStream.close();
		
		ListUploadRequest request = listService.saveUploadRequest(customer, fileName, fileLocation);
		listService.parseBlackList(request);
		return;
	}
}
