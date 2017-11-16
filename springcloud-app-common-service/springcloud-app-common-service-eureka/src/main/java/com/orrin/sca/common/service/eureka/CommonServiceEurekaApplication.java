package com.orrin.sca.common.service.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author orrin.zhang on 2017/7/28.
 */
@EnableEurekaServer
@SpringBootApplication
public class CommonServiceEurekaApplication {
	public static void main(String[] args) {
		SpringApplication.run(CommonServiceEurekaApplication.class, args);
	}
}
