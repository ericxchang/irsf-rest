package com.iconectiv.irsf.portal.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by echang on 1/12/2017.
 */
public class CustomerDatasourceRouter extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
    	return CustomerContextHolder.getSchema();
    }

}
