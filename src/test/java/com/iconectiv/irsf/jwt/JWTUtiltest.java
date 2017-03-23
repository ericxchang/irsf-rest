package com.iconectiv.irsf.jwt;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		String token = JWTUtil.createToken("irsf portal jwt", "irsf-rest");

		Object claims = JWTUtil.parseToken(token);
		log.info(JsonHelper.toPrettyJson(claims));
	}

}
