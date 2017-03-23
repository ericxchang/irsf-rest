package com.iconectiv.irsf.portal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.iconectiv.irsf.portal.core.AppConstants;
import com.iconectiv.irsf.util.JsonHelper;

/**
 * Base class containing utility methods for REST API controllers.
 */
public class BaseRestController {
	private static final String STATUS = "status";
	private static final String MESSAGE = "message";

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

	/**
	 * Create an error response (status = FAIL) with the message provided
	 * 
	 * @param message
	 *            message to be provided to the caller
	 * @param key1
	 *            key of object to add to response
	 * @param value1
	 *            value of object to add for key1
	 * @return response entity to return to spring
	 */
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

	/**
	 * Make a success result with the additional the data provided.
	 * 
	 * @param data
	 *            data to be returned to client
	 * @return response entity to return to spring
	 */
	protected ResponseEntity<String> makeSuccessResult(Map<String, Object> data) {
		data.put(STATUS, AppConstants.SUCCESS);
		String json = JsonHelper.toJson(data);
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


	protected ResponseEntity<String> makeSuccessResult(String key, Object value) {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put(key, value);
		result.put("data", dataMap);
		return makeSuccessResult(result);
	}

	/**
	 * Make a success result with the additional data item provided.
	 * 
	 * @param key
	 *            JSON key for the value to send to the client
	 * @param value
	 *            JSON value for the value to send to the client
	 * @return response entity to return to spring
	 */
	protected ResponseEntity<String> makeSuccessResult(Object value) {
		Map<String, Object> result = new HashMap<>();
		if (value != null) {
			result.put("data", value);
		}
		return makeSuccessResult(result);
	}

}
