package com.orrin.sca.framework.core.thread;

import com.orrin.sca.component.utils.thread.TraceThreadPoolTaskExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;

/**
 * @author migu-orrin on 2017/12/22.
 */
@Configuration
@EnableAsync
public class TraceThreadPoolConfig {
	/**
	 * 线程池维护线程的最少数量
	 */
	private final static int corePoolSize = 10;

	/**
	 * 线程池维护线程的最大数量
	 */
	private final static int maxPoolSize = 30;

	/**
	 * 缓存队列
	 */
	private final static int queueCapacity = 8;

	/**
	 * 允许的空闲时间
	 */
	private final static int keepAlive = 60;

	/**
	 * 使用 @Async("localTaskAsyncPool") ，否则使用默认线程池
	 * @return
	 */

	@Bean
	public Executor localTaskAsyncPool() {
		TraceThreadPoolTaskExecutor executor = new TraceThreadPoolTaskExecutor(corePoolSize, maxPoolSize, keepAlive, queueCapacity);
		executor.initialize();
		return executor;
	}
}
