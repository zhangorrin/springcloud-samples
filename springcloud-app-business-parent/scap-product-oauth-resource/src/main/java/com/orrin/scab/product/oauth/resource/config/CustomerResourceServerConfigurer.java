package com.orrin.scab.product.oauth.resource.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
public class CustomerResourceServerConfigurer extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/**").authenticated();
                //.antMatchers("/api/**").access("#oauth2.hasScope('openid')");
                //.antMatchers(HttpMethod.GET, "/api/**").access("#oauth2.hasScope('read')")
                //.antMatchers(HttpMethod.POST, "/api/**").access("#oauth2.hasScope('write')");
        // 禁用缓存
        http.headers().cacheControl();
    }
}
