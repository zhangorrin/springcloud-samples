package com.orrin.sca.component.redis.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.DefaultRedisCachePrefix;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCachePrefix;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 缓存管理（注解用）
 * @author orrin.zhang on 2017/8/3.
 * 使用时注解：@EnableCaching(mode = AdviceMode.ASPECTJ),@Configuration
 */
public class RedisCacheConfig extends CachingConfigurerSupport implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(RedisCacheConfig.class);

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	@Bean
	public CacheManager cacheManager() {
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
		redisCacheManager.setDefaultExpiration(30000);
		RedisCachePrefix redisCachePrefix = new DefaultRedisCachePrefix(":reidscache:");
		redisCacheManager.setUsePrefix(true);
		redisCacheManager.setCachePrefix(redisCachePrefix);
		return redisCacheManager;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("afterPropertiesSet");
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());

	}
}
