package com.orrin.sca.component.redis.cache;

import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author orrin.zhang on 2017/8/3.
 * 缓存管理（注解用）
 * 使用时注解：@EnableCaching(mode = AdviceMode.ASPECTJ),@Configuration
 */
public class RedissonCacheConfig extends CachingConfigurerSupport {
	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private RedissonClient redissonClient;

	@Bean
	CacheManager cacheManager(@Qualifier("redissonClient") RedissonClient redissonClient) throws IOException {

		/**
		 * 过期时间（ttl）和最长空闲时间（maxIdleTime），如果这两个参数都未指定或值为0，那么实例管理的数据将永久保存
		 */
		Map<String, CacheConfig> config = new HashMap<String, CacheConfig>();
		// 创建一个名称为"testMap"的缓存，过期时间ttl为24秒钟，同时最长空闲时maxIdleTime为12秒钟。
		config.put("redissonCache", new CacheConfig(24*60*1000, 12*60*1000));

		return new RedissonSpringCacheManager(redissonClient,config);
	}
}
