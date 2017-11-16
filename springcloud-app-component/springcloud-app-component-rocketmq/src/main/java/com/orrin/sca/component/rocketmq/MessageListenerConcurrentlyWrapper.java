package com.orrin.sca.component.rocketmq;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author orrin.zhang on 2017/8/11.
 */
public class MessageListenerConcurrentlyWrapper implements MessageListenerConcurrently {

	private final static Logger logger = LoggerFactory.getLogger(MessageListenerConcurrentlyWrapper.class);

	private RocketmqMessageListener rocketmqMessageListener;

	public MessageListenerConcurrentlyWrapper(RocketmqMessageListener rocketmqMessageListener) {
		this.rocketmqMessageListener = rocketmqMessageListener;
	}

	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {

		logger.info(Thread.currentThread().getName() + " Receive New Messages = {}", list.size());

		boolean dealResult = rocketmqMessageListener.onMessage(list, consumeConcurrentlyContext);

		logger.info(Thread.currentThread().getName() + " dealResult = {} ", dealResult);

		if (dealResult) {
			//如果没有return success，consumer会重复消费此信息，直到success。
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		}

		//TODO 进行日志记录
		return ConsumeConcurrentlyStatus.RECONSUME_LATER;

	}
}
