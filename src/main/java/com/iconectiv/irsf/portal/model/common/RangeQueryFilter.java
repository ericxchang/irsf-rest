package com.iconectiv.irsf.portal.model.common;

import java.util.List;

public class RangeQueryFilter {

	private List<String> codeList;
	private List<String> iso2List;
	private List<TosAndTosDescType> tosDescList;
	private List<Provider> providerList;
	
	private String beforeLastObserved;
	private String afterLastObserved;
	
	private Integer pageNo;
	private Integer limit;
	
	private String dataSource;

	//primeMinus 2,3,4
	private String dialPatternType;
	
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
		
	public List<TosAndTosDescType> getTosDescList() {
		return tosDescList;
	}
	public void setTosDescList(List<TosAndTosDescType> tosDescList) {
		this.tosDescList = tosDescList;
	}
	public List<Provider> getProviderList() {
		return providerList;
	}
	public void setProviderList(List<Provider> providerList) {
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
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getDialPatternType() {
		return dialPatternType;
	}

	public void setDialPatternType(String dialPatternType) {
		this.dialPatternType = dialPatternType;
	}
}
