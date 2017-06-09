package com.iconectiv.irsf.portal.controller;


import com.iconectiv.irsf.jwt.JWTUtil;
import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.core.MessageDefinition;
import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.exception.AuthException;
import com.iconectiv.irsf.portal.model.common.CustomerDefinition;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.ListDefinition;
import com.iconectiv.irsf.portal.repositories.common.CustomerDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.common.UserDefinitionRepository;
import com.iconectiv.irsf.portal.repositories.customer.ListDefinitionRepository;
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
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class AuthServiceController extends BaseRestController {
	private static Logger log = LoggerFactory.getLogger(AuthServiceController.class);

	@Autowired
	UserService userService;
	@Autowired
    UserDefinitionRepository userRepo;
	@Autowired
	CustomerDefinitionRepository customerRepo;
    @Autowired
    ListDefinitionRepository listRepo;
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

			if (loginUser.getCustomerId() != null) {
				CustomerDefinition customer = customerRepo.findOne(loginUser.getCustomerId());
				
				if (!customer.isActive()) {
					throw new AuthException("Customer " + customer.getCustomerName() + " is NOT active");
				}
				loginUser.setCustomerName(customer.getCustomerName());
				loginUser.setSchemaName(customer.getSchemaName());

                CustomerContextHolder.setSchema(loginUser.getSchemaName());
                List<ListDefinition> listDefinitionData = listRepo.findTop3ByActive(true);
                loginUser.setHasList(listDefinitionData != null && listDefinitionData.size() > 0);
			}
			
            auditService.saveAuditTrailLog(loginUser.getUserName(), loginUser.getCustomerName(), "login", request.getRemoteAddr());
			
			String token = JWTUtil.createToken(loginUser);
			rv = makeSuccessResult("successful login", token);
		} catch (Exception e) {
			log.error("Failed to login: ", e);
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled())
			log.debug(rv.getBody());
		return rv;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public ResponseEntity<String> logoutRequest(@RequestHeader Map<String, String> header, @RequestBody String value) {

		ResponseEntity<String> rv;


		try {
			UserDefinition user = getLoginUser(header);
			
			
			if (user == null) {
				throw new AuthException("Invalid user Id");
			}

            auditService.saveAuditTrailLog(user.getUserName(), user.getCustomerName(), "logout", "successful log out");
			
			rv = makeSuccessResult("successful logout");
		} catch (Exception e) {
			log.error("Failed to login: ", e);
			rv = makeErrorResult(e);
		}

		return rv;
	}
	
	@RequestMapping(value = "/createUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> createUserRequest(@RequestHeader Map<String, String> header, @RequestBody String userJson) {
		if (log.isDebugEnabled()) log.info(JsonHelper.toJson(header));
		ResponseEntity<String> rv;
        UserDefinition user = null;
		try {
			validateAdminUser(header);
			
            user = JsonHelper.fromJson(userJson, UserDefinition.class);

            userService.createUser(user);
			rv = makeSuccessResult(MessageDefinition.Create_User_Success);
		} catch (Exception e) {
			rv = makeErrorResult(e);
            auditService.saveAuditTrailLog("system", "", "create user", "fail " + e.getMessage(), "system");
		}

		if (log.isTraceEnabled()) {
			log.trace(JsonHelper.toPrettyJson(rv));
		}
		return rv;
	}

	@RequestMapping(value = "/updateUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> updateUserRequest(@RequestHeader Map<String, String> header, @RequestBody String userJson) {
		ResponseEntity<String> rv;
        UserDefinition user = null;
        try {
			validateAdminUser(header);

			user = JsonHelper.fromJson(userJson, UserDefinition.class);
			userService.updateUser(user);
			rv = makeSuccessResult(MessageDefinition.Update_User_Success);
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
	public ResponseEntity<String> changePasswordRequest(@RequestHeader Map<String, String> header, @RequestBody String userJson) {
		ResponseEntity<String> rv;
		try {
			
			validateAdminUser(header);

			UserDefinition user = JsonHelper.fromJson(userJson, UserDefinition.class);
			userService.changePassword(user);
			rv = makeSuccessResult(MessageDefinition.Change_Password_Success, user);
		} catch (Exception e) {
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled()) {
			log.debug(JsonHelper.toPrettyJson(rv));
		}
		return rv;
	}

	/*
	 * Temporary solution since we don't account management GUI yet. Validate login user has admin privilege 
	 */
	private void validateAdminUser(Map<String, String> header) throws AuthException {
		String userId = header.get("userid");
		String password = header.get("password");
		
		Assert.notNull(userId, "Missing userId in header");
		Assert.notNull(password, "Missing password in header");
		
		UserDefinition loginUser = userRepo.findOneByUserName(userId);
		if (!encoder.matches(password, loginUser.getPassword())) {
			throw new AuthException("Password is NOT correct");
		}
		
		if (!loginUser.getRole().equals(PermissionRole.Admin.value())) {
			throw new AuthException("Not authorized User ID");
		}
	}

}
