package com.orrin.sca.common.service.apigateway.server.config;

import com.orrin.sca.common.service.apigateway.server.service.impl.ZuulRouteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomeZuulConfig {
    @Autowired
    protected ZuulProperties zuulProperties;

    @Autowired
    protected ServerProperties server;

    @Autowired
    private DiscoveryClient discovery;

    @Autowired
    private ZuulRouteServiceImpl zuulRouteService;

    @Bean
    CustomeRouteLocator routeLocator() {
        CustomeRouteLocator routeLocator = new CustomeRouteLocator(server.getServletPath(), zuulProperties, discovery, zuulRouteService);
        return routeLocator;
    }

}
