package com.iconectiv.irsf.portal.controller;

import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.model.customer.ListDefintion;
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
class ListServiceController extends BaseRestController {
	private Logger log = LoggerFactory.getLogger(ListServiceController.class);

	@Autowired
	private ListService listService;

	@RequestMapping(value = "/list/{schema}/{listName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getListDetails(@PathVariable String schema, @PathVariable String listName) {
		ResponseEntity<String> rv;
		try {
			CustomerContextHolder.setSchema(schema);
			ListDefintion listDef = listService.getListDetails(listName);
            rv = makeSuccessResult(listDef);
		} catch (Exception e) {
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled()) {
			log.debug(JsonHelper.toPrettyJson(rv));
		}
		return rv;
	}

	//TO support junit testing
    @RequestMapping(value = "/list/{schema}/{listName}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> deleteListByNameRequest(@PathVariable String schema, @PathVariable String listName) {
        ResponseEntity<String> rv;
        try {
			CustomerContextHolder.setSchema(schema);
            listService.deleteListDefinition(listName);
            rv = makeSuccessResult();
        } catch (Exception e) {
            rv = makeErrorResult(e);
        }

        if (log.isDebugEnabled()) {
            log.debug(JsonHelper.toJson(rv));
        }
        return rv;
    }

}
