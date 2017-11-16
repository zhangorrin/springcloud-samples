package com.orrin.sca.component.rocketmq;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author orrin.zhang on 2017/8/11.
 */
public interface RocketmqMessageListener {
	public boolean onMessage(List<MessageExt> messageExtList, ConsumeConcurrentlyContext paramConsumeConcurrentlyContext);
}
