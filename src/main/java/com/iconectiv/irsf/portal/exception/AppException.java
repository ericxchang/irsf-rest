package com.iconectiv.irsf.portal.exception;

public class AppException extends IrsfException {
	private static final long serialVersionUID = 1L;

	public AppException() {
		super();
	}

	public AppException(String message) {
		super(message);
	}

	public AppException(String message, Throwable cause) {
		super(message, cause);
	}

	public AppException(Throwable cause) {
		super(cause);
	}

	public AppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AppException(String code, String desc)
    {
        super(code, desc);
 
    }

    public AppException(String code, String desc, String addInfo)
    {
        super(code, desc, addInfo);
      
    }

}
