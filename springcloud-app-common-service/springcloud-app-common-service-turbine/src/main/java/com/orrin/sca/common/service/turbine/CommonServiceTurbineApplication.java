package com.orrin.sca.common.service.turbine;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

/**
 * @author orrin.zhang on 2017/8/2.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableTurbine
public class CommonServiceTurbineApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(CommonServiceTurbineApplication.class).web(true).run(args);
	}
}
