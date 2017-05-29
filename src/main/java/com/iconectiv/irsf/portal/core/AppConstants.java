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
	public static int AFTER_LAST_OBSERVED = 32;
	public static int BEFORE_LAST_OBSERVED = 64;

	public static final String RANGE_NDC_TYPE = "Range NDC";
	public static final String PREMIUM_RANGE_TYPE = "IPRN";
	public static final String PRIME2 = "PRIME-2";
	public static final String PRIME3 = "PRIME-3";
	public static final String PRIME4 = "PRIME-4";
	public static final String CSV_COMMON_SEPERATOR = "|";
	public static final String IRSF_DATA_LOADER_CUSTOMER_NAME = "irsf";
	public static final String IRSF_DATA_LOADER_EVENT_TYPE = "RefreshData";



}
