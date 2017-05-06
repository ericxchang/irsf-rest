package com.iconectiv.irsf.portal.service;

import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.RuleDefinition;

public interface RuleService {
	void createRule(UserDefinition loginUser, RuleDefinition rule) throws AppException;
	void updateRule(UserDefinition loginUser, RuleDefinition rule) throws AppException;
}
