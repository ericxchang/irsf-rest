package com.iconectiv.irsf.json.vaidation;

public class JsonValidationException extends Exception {
	private static final long serialVersionUID = 1L;

	public JsonValidationException() {
	}

	public JsonValidationException(String message) {
		super(message);
	}

	public JsonValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public JsonValidationException(Throwable cause) {
		super(cause);
	}

	public JsonValidationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
