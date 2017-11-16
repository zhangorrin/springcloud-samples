package com.orrin.sca.component.rocketmq;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author orrin.zhang on 2017/8/11.
 */
@ConfigurationProperties(RocketmqProperties.PREFIX)
public class RocketmqProperties {
	public static final String PREFIX = "spring.extend.rocketmq";

	private String namesrvAddr;
	private ProducerConfig producer;
	private ConsumerConfig consumer;

	public String getNamesrvAddr() {
		return namesrvAddr;
	}

	public void setNamesrvAddr(String namesrvAddr) {
		this.namesrvAddr = namesrvAddr;
	}

	public ProducerConfig getProducer() {
		return producer;
	}

	public void setProducer(ProducerConfig producer) {
		this.producer = producer;
	}

	public ConsumerConfig getConsumer() {
		return consumer;
	}

	public void setConsumer(ConsumerConfig consumer) {
		this.consumer = consumer;
	}

	public static class ProducerConfig {
		private String instanceName;
		private String tranInstanceName;

		public String getInstanceName() {
			return instanceName;
		}

		public void setInstanceName(String instanceName) {
			this.instanceName = instanceName;
		}

		public String getTranInstanceName() {
			return tranInstanceName;
		}

		public void setTranInstanceName(String tranInstanceName) {
			this.tranInstanceName = tranInstanceName;
		}
	}

	public static class ConsumerConfig {
		private String instanceName;

		/**
		 * 订阅指定topic下tags分别等于TagA或TagC或TagD
		 * topic1:TagA || TagB || TagC
		 * topic2:*
		 */
		private List<String> subscribe;

		private String messageListenerClass;

		public String getInstanceName() {
			return instanceName;
		}

		public void setInstanceName(String instanceName) {
			this.instanceName = instanceName;
		}

		public List<String> getSubscribe() {
			return subscribe;
		}

		public void setSubscribe(List<String> subscribe) {
			this.subscribe = subscribe;
		}

		public String getMessageListenerClass() {
			return messageListenerClass;
		}

		public void setMessageListenerClass(String messageListenerClass) {
			this.messageListenerClass = messageListenerClass;
		}

	}
}
