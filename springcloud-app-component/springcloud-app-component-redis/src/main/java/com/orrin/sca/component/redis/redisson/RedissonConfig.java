package com.orrin.sca.component.redis.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author orrin.zhang on 2017/8/3.
 */
public class RedissonConfig {

	private static final Logger logger = LoggerFactory.getLogger(RedissonConfig.class);

	@Autowired
	private RedisProperties redisProperties;

	@Bean(destroyMethod="shutdown")
	public RedissonClient redissonClient() {

		boolean clusterFlag = redisProperties.getCluster() != null;

		logger.info("is cluster ? = {}", clusterFlag);

		Config config = new Config();

		if (clusterFlag) {
			config.setUseLinuxNativeEpoll(true);
			List<String> nodes = redisProperties.getCluster().getNodes();
			String[] nodeStrs = new String[nodes.size()];
			for (int i = 0; i < nodes.size(); i++) {
				nodeStrs[i] = "redis://" + nodes.get(i);
				logger.info("redis node = {}", nodes.get(i));
			}
			config.useClusterServers().setScanInterval(2000).addNodeAddress(nodeStrs);
		} else {
			logger.info("redis host = {}", redisProperties.getHost());
			logger.info("redis port = {}", redisProperties.getPort());
			//可以用"rediss://"来启用SSL连接
			config.useSingleServer().setAddress("redis://" + redisProperties.getHost() + ":" + String.valueOf(redisProperties.getPort()));
		}

		RedissonClient redisson = Redisson.create(config);

		return redisson;
	}
}
