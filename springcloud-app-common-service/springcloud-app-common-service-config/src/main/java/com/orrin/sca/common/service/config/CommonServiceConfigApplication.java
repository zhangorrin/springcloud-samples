package com.orrin.sca.common.service.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@EnableEurekaClient
@EnableConfigServer
public class CommonServiceConfigApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommonServiceConfigApplication.class, args);
    }
}
