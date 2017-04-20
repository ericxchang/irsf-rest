package com.iconectiv.irsf.jwt;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.util.JsonHelper;

public class JWTUtiltest {
	private static Logger log = LoggerFactory.getLogger(JWTUtiltest.class);

    @Rule
    public ExpectedException thrown= ExpectedException.none();
    
    @Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParseToken() {
		/*
		UserDefinition loginUser = new UserDefinition();
		loginUser.setUserName("guiuser01");
		loginUser.setCustomerId(1);
		loginUser.setRole(PermissionRole.CustAdmin.value());	
		loginUser.setSchemaName("cust01");
		
		String token = JWTUtil.createToken(loginUser);
		*/
		String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InVzZXIwMSIsInJvbGUiOiJ1c2VyIiwiY3VzdG9tZXJJZCI6MSwiZW5jcnlwdGlvbktleSI6Im1vYklkLTIwMTctMDQtMTkgMTY6NDA6NTUuMCIsInN1YiI6Imlyc2YgcG9ydGFsIGp3dCIsImF1ZCI6Imlyc2YtcmVzdCIsImlzcyI6Imljb25lY3RpdiIsImlhdCI6MTQ5MjcxMjk4N30.uPgLNuW3ItmUIde34qN7j90mKG-L60cQpiXZI68Lc2A";
		Object claims = JWTUtil.parseToken(token);
		log.info(JsonHelper.toPrettyJson(claims));
	}

}
