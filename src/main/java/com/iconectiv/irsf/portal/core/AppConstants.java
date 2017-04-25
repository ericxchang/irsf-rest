package com.iconectiv.irsf.portal.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppConstants {
	private AppConstants() {
	}

	public static final String SecretKey = "iconectiv.irsf.jwt";
	public static final String Subject = "irsf portal jwt";
	public static final String Audience = "irsf-rest";

	public static final String DATEFORMATPATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";
	public static final String PROCESS = "process";
	public static final String COMPLETE = "complete";
	public static final String FAIL = "fail";
	public static final String SUCCESS = "success";
	public static final String NORECORDFOUND = "No record found";
	public static final String BLACKLIST = "BL";
	public static final String WHITELIST = "WL";
	
	public static List<String> UploadFileType = new ArrayList<>(Arrays.asList("application/vnd.ms-excel", "text/plain"));
	
	public static int CODE = 1;
	public static int ISO2 = 2;
	public static int TOS = 4;
	public static int TOSDESC = 8;
	public static int PROVIDER=16;
	
	

}
