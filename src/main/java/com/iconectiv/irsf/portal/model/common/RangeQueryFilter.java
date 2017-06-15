package com.iconectiv.irsf.portal.model.common;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RangeQueryFilter implements Cloneable, java.io.Serializable {

	private List<String> codeList;
	private List<String> iso2List;
	private List<TosTosDesc> tosDescList;
	private List<Provider> providerList;
	
	private String beforeLastObserved;
	private String afterLastObserved;
	
	private Integer numOfMonthsSinceLastObserved;
	
	private Integer pageNo;
	private Integer limit;
	
	public RangeQueryFilter() {
		super();
	}
	
	public RangeQueryFilter(List<String> codeList, List<String> iso2List, List<TosTosDesc> tosDescList,
			List<Provider> providerList, String beforeLastObserved, String afterLastObserved,
			Integer numOfMonthsSinceLastObserved, Integer pageNo, Integer limit) {
		super();
		this.codeList = codeList;
		this.iso2List = iso2List;
		this.tosDescList = tosDescList;
		this.providerList = providerList;
		this.beforeLastObserved = beforeLastObserved;
		this.afterLastObserved = afterLastObserved;
		this.numOfMonthsSinceLastObserved = numOfMonthsSinceLastObserved;
		this.pageNo = pageNo;
		this.limit = limit;
	}

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

	public List<TosTosDesc> getTosDescList() {
		return tosDescList;
	}
	public void setTosDescList(List<TosTosDesc> tosDescList) {
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
	public Integer getNumOfMonthsSinceLastObserved() {
		return numOfMonthsSinceLastObserved;
	}
	public void setNumOfMonthsSinceLastObserved(Integer numOfMonthsSinceLastObserved) {
		this.numOfMonthsSinceLastObserved = numOfMonthsSinceLastObserved;
	}
	
	 //----------------------------------------------------------------
    // Overwrite object.clone() method
    //----------------------------------------------------------------
    public Object clone() {
        Object obj = null;
        try {
            obj = super.clone();
        } catch (CloneNotSupportedException ce) { }
        return obj;
    }
}
