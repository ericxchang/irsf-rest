package com.iconectiv.irsf.portal.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.core.MessageDefinition;
import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.RuleDefinition;
import com.iconectiv.irsf.portal.repositories.customer.RuleDefinitionRepository;
import com.iconectiv.irsf.portal.service.RuleService;
import com.iconectiv.irsf.util.JsonHelper;


@Controller
class RuleServiceController extends BaseRestController {
	private Logger log = LoggerFactory.getLogger(RuleServiceController.class);

	@Autowired
	private RuleService ruleService;
	@Autowired
	private RuleDefinitionRepository ruleRepo;

	@RequestMapping(value = "/rule/{ruleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getRuleDetails(@RequestHeader Map<String, String> header, @PathVariable Integer ruleId) {
		ResponseEntity<String> rv;
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());
			RuleDefinition rule = ruleRepo.findOne(ruleId);
            rv = makeSuccessResult(MessageDefinition.Query_Success, rule);
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


	@RequestMapping(value = "/rules", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getActiveRuleList(@RequestHeader Map<String, String> header) {
		ResponseEntity<String> rv;
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());

			List<RuleDefinition> rules = ruleRepo.findAllByActive(true);
			rv = makeSuccessResult(MessageDefinition.Query_Success, rules);
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

    @RequestMapping(value = "/rule/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> createRuleRequest(@RequestHeader Map<String, String> header, @RequestBody String value) {
        ResponseEntity<String> rv;
        try {
        	RuleDefinition rule = JsonHelper.fromJson(value, RuleDefinition.class);
            UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

            CustomerContextHolder.setSchema(loginUser.getSchemaName());
            ruleService.createRule(loginUser, rule);
            rv = makeSuccessResult(MessageDefinition.Save_Rule_Success, rule);
        } catch (SecurityException e) {
            rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
        } catch (AppException e) {
        	log.error("Error:", e);
            rv = makeErrorResult(e);
        } catch (Exception e) {
        	//TODO throw trap
        	log.error("Error:", e);
            rv = makeErrorResult(e);
        }

        if (log.isDebugEnabled()) {
            log.debug(JsonHelper.toJson(rv));
        }
        return rv;
    }

    @RequestMapping(value = "/rule/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> saveRuleRequest(@RequestHeader Map<String, String> header, @RequestBody String value) {
        ResponseEntity<String> rv;
        try {
        	RuleDefinition rule = JsonHelper.fromJson(value, RuleDefinition.class);
            UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

            CustomerContextHolder.setSchema(loginUser.getSchemaName());
            ruleService.updateRule(loginUser, rule);
            rv = makeSuccessResult(MessageDefinition.Save_Rule_Success, rule);
        } catch (SecurityException e) {
            rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
        } catch (AppException e) {
        	log.error("Error:", e);
            rv = makeErrorResult(e);
        } catch (Exception e) {
        	//TODO throw trap
        	log.error("Error:", e);
            rv = makeErrorResult(e);
        }

        if (log.isDebugEnabled()) {
            log.debug(JsonHelper.toJson(rv));
        }
        return rv;
    }

}
