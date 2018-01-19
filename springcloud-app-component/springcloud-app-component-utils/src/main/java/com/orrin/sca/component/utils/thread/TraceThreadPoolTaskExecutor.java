package com.orrin.sca.component.utils.thread;

import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Future;

/**
 * @author orrin.zhang on 2017/8/16.
 */
public class TraceThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

	public TraceThreadPoolTaskExecutor(int corePoolSize, int maxPoolSize, int keepAliveTime, int queueCapacity) {
		super();
		super.setCorePoolSize(corePoolSize);
		super.setMaxPoolSize(maxPoolSize);
		super.setKeepAliveSeconds(keepAliveTime);
		super.setQueueCapacity(queueCapacity);
		super.setThreadNamePrefix("TraceThreadPoolTaskExecutor-");
		//对拒绝task的处理策略
		super.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
	}

	@Override
	public void execute(Runnable task) {
		super.execute(wrap(task, clientTrace(), Thread.currentThread().getName()));
	}

	@Override
	public Future<?> submit(Runnable task) {
		return super.submit(wrap(task, clientTrace(), Thread.currentThread().getName()));
	}

	private Exception clientTrace() {
		return new Exception("Client stack trace");
	}

	private Runnable wrap(final Runnable task, final Exception clientStack, String clientThreadName) {
		return new Runnable() {
			@Override
			public void run() {
				try {
					task.run();
				} catch (Exception e) {
					clientStack.printStackTrace();
					e.printStackTrace();
					throw e;
				}
			}
		};
	}


}
