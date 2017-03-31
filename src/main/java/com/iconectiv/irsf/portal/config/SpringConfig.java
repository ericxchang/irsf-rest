package com.iconectiv.irsf.portal.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Created by echang on 1/11/2017.
 */

@Configuration
@EnableAsync
@PropertySource(value="classpath:spring-config.properties", ignoreResourceNotFound = true)
@PropertySource(value="file:/conf/irsf/spring-config.properties", ignoreResourceNotFound = true)
public class SpringConfig {
	private static Logger log = LoggerFactory.getLogger(SpringConfig.class);

	@Autowired
	private Environment env;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
		configurer.setIgnoreUnresolvablePlaceholders(true);
		configurer.setIgnoreResourceNotFound(true);
		return configurer;
	}

}
