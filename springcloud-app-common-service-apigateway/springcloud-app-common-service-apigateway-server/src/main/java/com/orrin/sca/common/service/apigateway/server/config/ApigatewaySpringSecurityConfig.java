package com.orrin.sca.common.service.apigateway.server.config;

import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author orrin.zhang on 2017/7/28.
 */
//@Order(ManagementServerProperties.ACCESS_OVERRIDE_ORDER)
//@Configuration
@EnableWebSecurity
public class ApigatewaySpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(WebSecurity webSecurity) throws Exception {
		webSecurity.ignoring().antMatchers( "/zuul/**","/mgmt/health","/mgmt/health/");
	}

	/*@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/zuul/**","/mgmt/health","/mgmt/health/").permitAll()
				.and().authorizeRequests().anyRequest().authenticated();

		http.csrf().disable();
	}*/

}
