package com.iconectiv.irsf.portal.controller;

import com.google.common.base.Joiner;
import com.iconectiv.irsf.jwt.JWTUtil;
import com.iconectiv.irsf.portal.core.AppConstants;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.util.JsonHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base class containing utility methods for REST API controllers.
 */
public class BaseRestController {
	private static Logger log = LoggerFactory.getLogger(BaseRestController.class);
	private static final String STATUS = "status";
	private static final String MESSAGE = "message";

	protected void assertAuthorized(UserDefinition loginUser, String permission) {
		if (loginUser == null) {
			throw new SecurityException("Invalid user, please login first!");
		}

		if (permission != null) {
			if (!Arrays.asList(permission.split(",")).contains(loginUser.getRole())) {
				throw new SecurityException(
				        "User " + loginUser.getUserName() + " does NOT have permission of " + permission);
			}
		}
	}

	protected UserDefinition getLoginUser(Map<String, String> header) {
		try {
			log.debug(JsonHelper.toPrettyJson(header));
			String token = header.get("Authorization");
			if (log.isDebugEnabled()) log.debug("JWT in header: {}", token);
			
			Jws<Claims> claims = JWTUtil.parseToken(token.substring(7));

			if (log.isDebugEnabled()) log.debug("JWT body: {}", claims.getBody().toString());
			
			UserDefinition loginUser = JsonHelper.fromJson(claims.getBody().toString(), UserDefinition.class);
			return loginUser;
		} catch (Exception e) {
			log.error("Error to parse JWT:", e);
			return null;
		}
	}

	/**
	 * Create an error response (status = FAIL) based on the exception caught.
	 * 
	 * @param ve
	 *            validation exception that caused the request to fail.
	 * @return response entity to return to spring
	 */
	protected ResponseEntity<String> makeErrorResult(Exception ve) {
		Map<String, Object> result = new HashMap<>();
		result.put(STATUS, AppConstants.FAIL);
		result.put(MESSAGE, ve.getMessage());
		String json = JsonHelper.toJson(result);
		return new ResponseEntity<>(json, HttpStatus.OK);
	}

	protected ResponseEntity<String> makeErrorResult(Exception ve, HttpStatus httpStatus) {
		Map<String, Object> result = new HashMap<>();
		result.put(STATUS, AppConstants.FAIL);
		result.put(MESSAGE, ve.getMessage());
		String json = JsonHelper.toJson(result);
		return new ResponseEntity<>(json, httpStatus);
	}

	/**
	 * Create an error response (status = FAIL) with the message provided
	 * 
	 * @param message
	 *            message to be provided to the caller
	 * @return response entity to return to spring
	 */
	protected ResponseEntity<String> makeErrorResult(String message) {
		Map<String, Object> result = new HashMap<>();
		result.put(STATUS, AppConstants.FAIL);
		result.put(MESSAGE, message);
		String json = JsonHelper.toJson(result);
		return new ResponseEntity<>(json, HttpStatus.OK);
	}

	protected ResponseEntity<String> makeErrorResult(String message, String key, Object value) {
		Map<String, Object> result = new HashMap<>();
		result.put(STATUS, AppConstants.FAIL);
		result.put(MESSAGE, message);
		if (value != null) {
			result.put(key, value);
		}
		String json = JsonHelper.toJson(result);
		return new ResponseEntity<>(json, HttpStatus.OK);
	}

	/**
	 * Create an error response (status = FAIL) with the messages provided. Some
	 * API requests support multiple messages returned in an array
	 * 
	 * @param messages
	 *            messages to be provided to the caller
	 * @return response entity to return to spring
	 */
	protected ResponseEntity<String> makeErrorResult(List<String> messages) {
		Map<String, Object> result = new HashMap<>();
		result.put(STATUS, AppConstants.FAIL);
		result.put("messages", messages);
		String json = JsonHelper.toJson(result);
		return new ResponseEntity<>(json, HttpStatus.OK);
	}

	/**
	 * Make a result from the data provided. The result should include STATUS
	 * message.
	 * 
	 * @param data
	 *            data to be returned to client
	 * @return response entity to return to spring
	 */
	protected ResponseEntity<String> makeResult(Map<String, Object> data) {
		String json = JsonHelper.toJson(data);
		return new ResponseEntity<>(json, HttpStatus.OK);
	}


	protected  <T> ResponseEntity<String> makeSuccessResult(Page<T> pageData) {
		Map<String, Object> result = new HashMap<>();
		result.put(STATUS, AppConstants.SUCCESS);
		result.put("messages", "");
		result.put("data", pageData.getContent());
		result.put("first", pageData.isFirst());
		result.put("last", pageData.isLast());
		result.put("totalCount", pageData.getTotalElements());
		result.put("totalPage", pageData.getTotalPages());
		result.put("pageNumber", pageData.getNumber());
		result.put("count", pageData.getNumberOfElements());
		String json = JsonHelper.toJson(result);
		return new ResponseEntity<>(json, HttpStatus.OK);
	}

	
	/**
	 * Make a success result with the additional the data provided.
	 * 
	 * @param data
	 *            data to be returned to client
	 * @return response entity to return to spring
	 */
	private ResponseEntity<String> makeSuccessResult(Map<String, Object> data) {
		data.put(STATUS, AppConstants.SUCCESS);
		String json = JsonHelper.toJson(data);
		if (log.isDebugEnabled()) log.debug(JsonHelper.toPrettyJson(json));
		return new ResponseEntity<>(json, HttpStatus.OK);
	}

	/**
	 * Make a success result with no additional the data provided.
	 * 
	 * @return response entity to return to spring
	 */
	protected ResponseEntity<String> makeSuccessResult() {
		Map<String, Object> result = new HashMap<>();
		return makeSuccessResult(result);
	}

	protected ResponseEntity<String> makeSuccessResult(String message) {
		Map<String, Object> result = new HashMap<>();
		result.put(MESSAGE, message);
		return makeSuccessResult(result);
	}

	protected ResponseEntity<String> makeSuccessResult(String message, String key, Object value) {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put(key, value);
		result.put("data", dataMap);
		result.put(MESSAGE, message);
		return makeSuccessResult(result);
	}


	protected ResponseEntity<String> makeSuccessResult(String message, Object value) {
		Map<String, Object> result = new HashMap<>();
		if (value != null) {
			result.put("data", value);
		}
		result.put(MESSAGE, message);
		return makeSuccessResult(result);
	}

}
