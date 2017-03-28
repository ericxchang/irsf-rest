package com.iconectiv.irsf.portal.controller;


import com.iconectiv.irsf.jwt.JWTUtil;
import com.iconectiv.irsf.portal.core.MessageDefinition;
import com.iconectiv.irsf.portal.exception.AuthException;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
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

@Controller
public class AuthServiceController extends BaseRestController {
	private static Logger log = LoggerFactory.getLogger(AuthServiceController.class);

	@Autowired
	UserService userService;
	
	@Autowired
	BCryptPasswordEncoder encoder;	

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public ResponseEntity<String> loginRequest(@RequestBody String value) {

		ResponseEntity<String> rv;

		if (log.isDebugEnabled())
			log.debug("Received login request: {}", value);

		try {
			UserDefinition user = JsonHelper.fromJson(value, UserDefinition.class, true);
			UserDefinition loginUser = userService.findUserByName(user.getUserName());
			
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
		try {
			UserDefinition user = JsonHelper.fromJson(userJson, UserDefinition.class);
			userService.createUser(user);
			rv = makeSuccessResult(MessageDefinition.Create_User_Success);
		} catch (Exception e) {
			rv = makeErrorResult(e);
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
		try {
			UserDefinition user = JsonHelper.fromJson(userJson, UserDefinition.class);
			userService.updateUser(user);
			rv = makeSuccessResult(MessageDefinition.Update_User_Success);
		} catch (Exception e) {
			rv = makeErrorResult(e);
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
			rv = makeSuccessResult(MessageDefinition.Change_Password_Success);
		} catch (Exception e) {
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled()) {
			log.debug(JsonHelper.toPrettyJson(rv));
		}
		return rv;
	}

}
