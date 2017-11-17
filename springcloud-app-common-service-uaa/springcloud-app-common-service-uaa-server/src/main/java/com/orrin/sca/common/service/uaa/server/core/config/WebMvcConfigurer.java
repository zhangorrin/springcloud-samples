package com.orrin.sca.common.service.uaa.server.core.config;

import com.orrin.sca.framework.core.springmvc.handle.JsonReturnHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.ForwardedHeaderFilter;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author orrin.zhang on 2017/7/28.
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebMvcConfigurer.class);


	@Bean
	FilterRegistrationBean forwardedHeaderFilter() {
		FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
		filterRegBean.setFilter(new ForwardedHeaderFilter());
		filterRegBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return filterRegBean;
	}

	@Bean
	HandlerMethodReturnValueHandler jsonReturnHandler() {
		JsonReturnHandler jsonReturnHandler = new JsonReturnHandler();
		return jsonReturnHandler;
	}

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
		returnValueHandlers.add(jsonReturnHandler());
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/oauth/confirm_access").setViewName("authorize");
		registry.addViewController("/index").setViewName("index");
	}
}
