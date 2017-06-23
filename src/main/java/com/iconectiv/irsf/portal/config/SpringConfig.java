package com.iconectiv.irsf.portal.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Created by echang on 1/11/2017.
 */

@Configuration
@EnableAsync
@EnableScheduling
@EnableWebSocketMessageBroker
@PropertySource(value="classpath:spring-config.properties", ignoreResourceNotFound = true)
@PropertySource(value="file:/apps/irsf/conf/spring-config.properties", ignoreResourceNotFound = true)
public class SpringConfig extends AbstractWebSocketMessageBrokerConfigurer {
	private static Logger log = LoggerFactory.getLogger(SpringConfig.class);

	@Autowired
	private static Environment env;
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
		configurer.setIgnoreUnresolvablePlaceholders(true);
		configurer.setIgnoreResourceNotFound(true);

		return configurer;
	}


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/queue/", "/topic/");
        config.setApplicationDestinationPrefixes("/app");
        config.setPathMatcher(new AntPathMatcher("."));
    }

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/events").setAllowedOrigins("*").withSockJS().setSupressCors(true);
	}

}
