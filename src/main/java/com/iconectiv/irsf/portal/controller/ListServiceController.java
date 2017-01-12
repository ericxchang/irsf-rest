package com.iconectiv.irsf.portal.controller;

import com.iconectiv.irsf.portal.service.ListService;
import com.iconectiv.irsf.portal.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ListServiceController extends BaseRestController {
	private static Logger log = LoggerFactory.getLogger(ListServiceController.class);

	@Autowired
	private ListService listService;

	@RequestMapping(value = "/uploadBlackList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public ResponseEntity<String> uploadBlackList() {
		ResponseEntity<String> rv;
		try {
			Thread.sleep(5*1000);
			log.info("complete download file");
			listService.parseBlackList(1);
			rv = makeSuccessResult();
		} catch (Exception e) {
			rv = makeErrorResult(e);
		}

		log.info(JsonHelper.toJson(rv));
		return rv;
	}

}
