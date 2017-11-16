package com.orrin.sca.common.service.file.config;

import com.orrin.sca.common.service.file.factory.ConnectionPoolFactory;
import com.orrin.sca.common.service.file.properties.FastDFSPoolProperties;
import com.orrin.sca.common.service.file.properties.FastDFSProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author orrin.zhang on 2017/8/14.
 */
@Configuration
@EnableConfigurationProperties({FastDFSProperties.class,FastDFSPoolProperties.class})
@EnableAutoConfiguration
public class FastDFSAutoConfiguration {

	private final static Logger logger = LoggerFactory.getLogger(FastDFSAutoConfiguration.class);

	@Autowired
	private FastDFSProperties properties;

	@Autowired
	private FastDFSPoolProperties poolProperties;

	@Bean
	public ConnectionPoolFactory connectionPoolFactory() {
		return new ConnectionPoolFactory(properties, poolProperties);
	}

}
