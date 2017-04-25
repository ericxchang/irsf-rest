package com.iconectiv.irsf.portal.core;

import java.util.ArrayList;
import java.util.List;

public class RangeNdcQueryFilter {

	private List<String> codeList;
	private List<String> iso2List;
	private List<String> tosList;
	private List<TosAndTosDescType> tosDescLis;
	private List<String> providerList;
	Integer pageNo;
	Integer limit;
	
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
	
	public List<TosAndTosDescType> getTosDescLis() {
		return tosDescLis;
	}
	public void setTosDescLis(List<TosAndTosDescType> tosDescLis) {
		this.tosDescLis = tosDescLis;
	}
	public List<String> getProviderList() {
		return providerList;
	}
	public void setProviderList(List<String> providerList) {
		this.providerList = providerList;
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
