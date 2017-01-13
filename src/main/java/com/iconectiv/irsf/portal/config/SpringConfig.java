package com.iconectiv.irsf.portal.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Created by echang on 1/11/2017.
 */

@Configuration
@EnableAsync
@PropertySource("file:/conf/irsf/spring-config.properties")
public class SpringConfig {
	private static Logger log = LoggerFactory.getLogger(SpringConfig.class);

	@Autowired
	private Environment env;
}
