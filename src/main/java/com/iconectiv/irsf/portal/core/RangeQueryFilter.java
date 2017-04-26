package com.iconectiv.irsf.portal.core;

import java.util.ArrayList;
import java.util.List;

public class RangeQueryFilter {

	private List<String> codeList;
	private List<String> iso2List;
	private List<String> tosList;
	private List<String> tosDescList;
	private List<String> providerList;
	
	private String beforeLastObserved;
	private String afterLastObserved;
	
	private Integer pageNo;
	private Integer limit;
	
	public List<String> getCodeList() {
		return codeList;
	}
	public void setCodeList(List<String> codeList) {
		this.codeList = codeList;
	}
	public List<String> getIso2List() {
		return iso2List;
	}
	public void setIso2List(List<String> iso2List) {
		this.iso2List = iso2List;
	}
	public List<String> getTosList() {
		return tosList;
	}
	public void setTosList(List<String> tosList) {
		this.tosList = tosList;
	}
	public List<String> getTosDescList() {
		return tosDescList;
	}
	public void setTosDescList(List<String> tosDescList) {
		this.tosDescList = tosDescList;
	}
	public List<String> getProviderList() {
		return providerList;
	}
	public void setProviderList(List<String> providerList) {
		this.providerList = providerList;
	}
	public String getBeforeLastObserved() {
		return beforeLastObserved;
	}
	public void setBeforeLastObserved(String beforeLastObserved) {
		this.beforeLastObserved = beforeLastObserved;
	}
	public String getAfterLastObserved() {
		return afterLastObserved;
	}
	public void setAfterLastObserved(String afterLastObserved) {
		this.afterLastObserved = afterLastObserved;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	
	
}
