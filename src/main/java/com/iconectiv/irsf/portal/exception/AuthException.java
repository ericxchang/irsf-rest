package com.iconectiv.irsf.portal.exception;

public class AuthException extends Exception {
	private static final long serialVersionUID = 1L;

	public AuthException() {
	}

	public AuthException(String message) {
		super(message);
	}

	public AuthException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthException(Throwable cause) {
		super(cause);
	}

	public AuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
