package com.orrin.sca.component.rocketmq;

import com.orrin.sca.component.utils.spring.SpringUtil;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author orrin.zhang on 2017/8/11.
 */
@Configuration
@EnableConfigurationProperties(RocketmqProperties.class)
@ConditionalOnProperty(prefix = RocketmqProperties.PREFIX, value = "namesrvAddr")
public class RocketmqAutoConfiguration {

	private final static Logger logger = LoggerFactory.getLogger(RocketmqAutoConfiguration.class);

	@Autowired
	private RocketmqProperties properties;

	@Value("${spring.application.name}")
	private String producerGroupName;

	@Value("${spring.application.name}")
	private String consumerGroupName;


	/**
	 * 初始化向rocketmq发送普通消息的生产者
	 */
	@Bean
	@ConditionalOnProperty(prefix = RocketmqProperties.PREFIX, value = "producer.instanceName")
	public DefaultMQProducer defaultMQProducer() throws MQClientException {
		logger.info(" start create defaultMQProducer ...");
		/**
		 * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例<br>
		 * 注意：ProducerGroupName需要由应用来保证唯一<br>
		 * ProducerGroup这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键，
		 * 因为服务器会回查这个Group下的任意一个Producer
		 */
		DefaultMQProducer producer = new DefaultMQProducer(producerGroupName);
		producer.setNamesrvAddr(properties.getNamesrvAddr());
		producer.setInstanceName(properties.getProducer().getInstanceName());
		producer.setVipChannelEnabled(false);

		logger.info(" start create defaultMQProducer namesrvAddr = {}", properties.getNamesrvAddr());
		logger.info(" start create defaultMQProducer instanceName = {}", properties.getProducer().getInstanceName());

		/**
		 * Producer对象在使用之前必须要调用start初始化，初始化一次即可<br>
		 * 注意：切记不可以在每次发送消息时，都调用start方法
		 */
		producer.start();
		logger.info("defaultMQProducer started");
		return producer;
	}

	/**
	 * 初始化向rocketmq发送事务消息的生产者
	 */
	@Bean
	@ConditionalOnProperty(prefix = RocketmqProperties.PREFIX, value = "producer.tranInstanceName")
	public TransactionMQProducer transactionMQProducer() throws MQClientException {
		logger.info(" start create transactionMQProducer ...");
		/**
		 * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例<br>
		 * 注意：ProducerGroupName需要由应用来保证唯一<br>
		 * ProducerGroup这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键，
		 * 因为服务器会回查这个Group下的任意一个Producer
		 */
		TransactionMQProducer producer = new TransactionMQProducer("transaction-"+producerGroupName);
		producer.setNamesrvAddr(properties.getNamesrvAddr());
		producer.setInstanceName(properties.getProducer().getTranInstanceName());


		logger.info(" start create transactionMQProducer namesrvAddr = {}", properties.getNamesrvAddr());
		logger.info(" start create transactionMQProducer instanceName = {}", properties.getProducer().getTranInstanceName());

		// 事务回查最小并发数
		producer.setCheckThreadPoolMinSize(2);
		// 事务回查最大并发数
		producer.setCheckThreadPoolMaxSize(5);
		// 队列数
		producer.setCheckRequestHoldMax(2000);

		/**
		 * Producer对象在使用之前必须要调用start初始化，初始化一次即可<br>
		 * 注意：切记不可以在每次发送消息时，都调用start方法
		 */
		producer.start();

		logger.info("transactionMQProducer started");
		return producer;
	}

	/**
	 * 初始化rocketmq消息监听方式的消费者
	 */
	@Bean
	@ConditionalOnProperty(prefix = RocketmqProperties.PREFIX, value = {"consumer.instanceName","consumer.messageListenerClass"})
	public DefaultMQPushConsumer pushConsumer() throws MQClientException, ClassNotFoundException {
		/**
		 * 一个应用创建一个Consumer，由应用来维护此对象，可以设置为全局对象或者单例<br>
		 * 注意：ConsumerGroupName需要由应用来保证唯一
		 */
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroupName);
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		consumer.setNamesrvAddr(properties.getNamesrvAddr());
		consumer.setInstanceName(properties.getConsumer().getInstanceName());
		consumer.setConsumeMessageBatchMaxSize(1);//设置批量消费，以提升消费吞吐量，默认是1

		/**
		 * 订阅指定topic下tags
		 */
		List<String> subscribeList = properties.getConsumer().getSubscribe();
		for (String sunscribe : subscribeList) {
			consumer.subscribe(sunscribe.split(":")[0], sunscribe.split(":")[1]);
		}

		RocketmqMessageListener rocketmqMessageListener;
		Object listenerObj = SpringUtil.getBean(Class.forName(properties.getConsumer().getMessageListenerClass()));
		if(listenerObj instanceof RocketmqMessageListener){
			rocketmqMessageListener = (RocketmqMessageListener)listenerObj;
		}else {
			throw new MQClientException(-1,"配置文件中spring.extend.rocketmq.consumer.messageListenerClass必须实现RocketmqMessageListener接口");
		}

		MessageListenerConcurrentlyWrapper messageListenerConcurrentlyWrapper = new MessageListenerConcurrentlyWrapper(rocketmqMessageListener);

		consumer.registerMessageListener(messageListenerConcurrentlyWrapper);

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(5000);//延迟5秒再启动，主要是等待spring事件监听相关程序初始化完成，否则，可能会出现对RocketMQ的消息进行消费后立即发布消息到达的事件，然而此事件的监听程序还未初始化，从而造成消息的丢失
					/**
					 * Consumer对象在使用之前必须要调用start初始化，初始化一次即可<br>
					 */
					try {
						consumer.start();
					} catch (Exception e) {
						logger.info("RocketMq pushConsumer Start failure!!!.");
						e.printStackTrace();
					}

					logger.info("RocketMq pushConsumer Started.");

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}).start();

		return consumer;

	}
}
