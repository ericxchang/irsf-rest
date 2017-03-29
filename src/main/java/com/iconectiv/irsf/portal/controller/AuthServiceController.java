package com.iconectiv.irsf.portal.controller;


import com.iconectiv.irsf.jwt.JWTUtil;
import com.iconectiv.irsf.portal.core.MessageDefinition;
import com.iconectiv.irsf.portal.exception.AuthException;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.repositories.common.UserDefinitionRepository;
import com.iconectiv.irsf.portal.service.AuditTrailService;
import com.iconectiv.irsf.portal.service.UserService;
import com.iconectiv.irsf.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthServiceController extends BaseRestController {
	private static Logger log = LoggerFactory.getLogger(AuthServiceController.class);

	@Autowired
	UserService userService;
	@Autowired
    UserDefinitionRepository userRepo;
	@Autowired
    AuditTrailService auditService;
	@Autowired
	BCryptPasswordEncoder encoder;	

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public ResponseEntity<String> loginRequest(HttpServletRequest request, @RequestBody String value) {

		ResponseEntity<String> rv;

		if (log.isDebugEnabled())
			log.debug("Received login request: {}", value);

		try {
			UserDefinition user = JsonHelper.fromJson(value, UserDefinition.class, true);
			UserDefinition loginUser = userRepo.findOneByUserName(user.getUserName());
			
			if (loginUser == null) {
				throw new AuthException("Invalid user Id");
			}

			if (loginUser.isDisabled()) {
				throw new AuthException("The User Id has been disabled");
			}

			if (loginUser.isLocked()) {
				throw new AuthException("The User Id has been locked");
			}

			if (!encoder.matches(user.getPassword(), loginUser.getPassword())) {
				throw new AuthException("Password is NOT correct");
			}

            auditService.saveAuditTrailLog(user.getUserName(), user.getCustomerName(), "login", request.getRemoteAddr());
			
			String token = JWTUtil.createToken(loginUser);
			rv = makeSuccessResult(token);
		} catch (Exception e) {
			log.error("Failed to login: ", e);
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled())
			log.debug(rv.getBody());
		return rv;
	}

	@RequestMapping(value = "/createUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> createUserRequest(@RequestBody String userJson) {
		ResponseEntity<String> rv;
        UserDefinition user = null;
		try {
            user = JsonHelper.fromJson(userJson, UserDefinition.class);

            userService.createUser(user);
			rv = makeSuccessResult(MessageDefinition.Create_User_Success);
            auditService.saveAuditTrailLog(user.getUserName(), user.getCustomerName(), "create user", "success created user " + user.getUserName(), "system");
		} catch (Exception e) {
			rv = makeErrorResult(e);
            auditService.saveAuditTrailLog("system", "", "create user", "fail " + e.getMessage(), "system");
		}

		if (log.isDebugEnabled()) {
			log.debug(JsonHelper.toPrettyJson(rv));
		}
		return rv;
	}

	@RequestMapping(value = "/updateUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> updateUserRequest(@RequestBody String userJson) {
		ResponseEntity<String> rv;
        UserDefinition user = null;
        try {
            user = JsonHelper.fromJson(userJson, UserDefinition.class);
			userService.updateUser(user);
			rv = makeSuccessResult(MessageDefinition.Update_User_Success);
            auditService.saveAuditTrailLog(user.getUserName(), user.getCustomerName(), "update user", "success updated user " + user.getUserName(), "system");
		} catch (Exception e) {
			rv = makeErrorResult(e);
            auditService.saveAuditTrailLog("system", "", "update user", "fail " + e.getMessage(), "system");
		}

		if (log.isDebugEnabled()) {
			log.debug(JsonHelper.toPrettyJson(rv));
		}
		return rv;
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> changePasswordRequest(@RequestBody String userJson) {
		ResponseEntity<String> rv;
		try {
			UserDefinition user = JsonHelper.fromJson(userJson, UserDefinition.class);
			userService.changePassword(user);
			rv = makeSuccessResult(MessageDefinition.Change_Password_Success, user);
            auditService.saveAuditTrailLog(user.getUserName(), user.getCustomerName(), "change password", "success");
		} catch (Exception e) {
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled()) {
			log.debug(JsonHelper.toPrettyJson(rv));
		}
		return rv;
	}

}
