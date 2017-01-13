package com.iconectiv.irsf.portal.controller;

import com.iconectiv.irsf.portal.model.common.ListUploadRequest;
import com.iconectiv.irsf.portal.service.ListService;
import com.iconectiv.irsf.portal.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ListServiceController extends BaseRestController {
	private static Logger log = LoggerFactory.getLogger(ListServiceController.class);

	@Autowired
	private ListService listService;

	@RequestMapping(value = "/uploadBlackList/{customer}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public ResponseEntity<String> uploadBlackList(@PathVariable String customer) {
		ResponseEntity<String> rv;
		try {
			log.info("complete download file");
			ListUploadRequest request = listService.saveUploadRequest(customer, "blacklist", "/tmp/blackList.txt");
			listService.parseBlackList(request);
			rv = makeSuccessResult(request);
		} catch (Exception e) {
			rv = makeErrorResult(e);
		}

		log.info(JsonHelper.toJson(rv));
		return rv;
	}

}
