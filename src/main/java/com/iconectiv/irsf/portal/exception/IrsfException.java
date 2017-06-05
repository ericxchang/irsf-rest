package com.iconectiv.irsf.portal.exception;

public class IrsfException extends Exception {

	private static final long serialVersionUID = 1L;
	private String code;
	private String desc;
	private String addInfo;
	    
	public IrsfException() {
		super();
	}

	public IrsfException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IrsfException(String message, Throwable cause) {
		super(message, cause);
	}

	public IrsfException(String message) {
		super(message);
	}

	public IrsfException(Throwable cause) {
		super(cause);
	}

	public IrsfException(String code, String desc)
    {
        super(desc);
        this.code = code;
        this.desc = desc;
    }

    public IrsfException(String code, String desc, String addInfo)
    {
        super(desc);
        this.code = code;
        this.desc = desc;
        this.addInfo = addInfo;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAddInfo() {
		return addInfo;
	}

	public void setAddInfo(String addInfo) {
		this.addInfo = addInfo;
	}
	

}
