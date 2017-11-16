package com.orrin.scab.product.config;

import com.orrin.sca.framework.core.springmvc.handle.JsonReturnHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author orrin.zhang on 2017/8/16.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
		returnValueHandlers.add(new JsonReturnHandler());
	}
}
