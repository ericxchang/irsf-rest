package com.iconectiv.irsf.util;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateTimeHelperTest {
	private static Logger log = LoggerFactory.getLogger(DateTimeHelperTest.class);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testNowInUTC() {
		log.info("current time in local: " + new Date());
		log.info("current time in UTC: " + DateTimeHelper.nowInUTC());
	}

	@Test
	public void testToUTC() {
		Date now = new Date();
		log.info("current time in UTC: " + DateTimeHelper.toUTC(now));
	}

}
