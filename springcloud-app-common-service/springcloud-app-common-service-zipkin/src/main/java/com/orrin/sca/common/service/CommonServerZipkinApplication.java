package com.orrin.sca.common.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import zipkin.server.EnableZipkinServer;

/**
 * @author orrin.zhang on 2017/8/2.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableZipkinServer
public class CommonServerZipkinApplication {
	public static void main(String[] args) {
		SpringApplication.run(CommonServerZipkinApplication.class, args);
	}
}
