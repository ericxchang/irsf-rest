package com.iconectiv.irsf.portal.config;

public class CustomerContextHolder {
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
	
	CustomerContextHolder() {
		//default constructor
	}
	
	public static void setCustomer(String customer) {
		contextHolder.set(customer);
	}
	
	public static String getCustomer() {
		return contextHolder.get();
	}
	
	public static void cleanCustomer() {
		contextHolder.remove();
	}
}
