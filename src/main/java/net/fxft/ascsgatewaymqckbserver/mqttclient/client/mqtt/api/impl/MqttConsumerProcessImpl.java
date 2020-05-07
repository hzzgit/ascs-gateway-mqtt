package net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.api.impl;


import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import net.fxft.ascsgatewaymqckbserver.common.core.CacheList;
import net.fxft.ascsgatewaymqckbserver.common.core.CacheListLocalMemory;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.core.BaseClient;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.api.ClientProcess;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.api.MqttConsumer;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.api.MqttConsumerListener;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.api.MqttConsumerProcess;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.common.MessageData;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.common.MessageStatus;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.common.SubscribeMessage;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.protocol.ClientProtocolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/
@Slf4j
public class MqttConsumerProcessImpl extends ClientProcess implements MqttConsumerProcess, MqttConsumer {
	private MqttConsumerListener listener;
	@Setter
	@NonNull
	private CacheList<MessageData> msgListCache;
	
	public MqttConsumerProcessImpl(BaseClient client) {
		this(client, new CacheListLocalMemory<MessageData>());
	}
	
	public MqttConsumerProcessImpl(BaseClient client, CacheList<MessageData> msgListCache) {
		super(client);
		this.msgListCache = msgListCache;
	}

	@Override
	public void subscribe(String topic, int qosValue) {
		SubscribeMessage msgObj = SubscribeMessage.builder().topic(topic).qos(qosValue).build();
		subscribe(msgObj);
	}

	public void subscribe(SubscribeMessage info) {
		log.debug("subscribe： {} ", info);
		subscribe(new SubscribeMessage[] { info });
	}

	private void subscribe(SubscribeMessage... info) {
		if (info != null) {
			int messageId = messageId().getNextMessageId(this.getClientId());
			if (!channel().isActive()) {
				log.debug("channel is close");
				return;
			}
			
			channel().writeAndFlush(ClientProtocolUtil.subscribeMessage(messageId, info));
		}
	}

	public void unSubscribe(String topic) {
		List<String> topics = new ArrayList<String>();
		topics.add(topic);
		unSubscribe(topics);
	}

	public void unSubscribe(List<String> topics) {
		if (topics != null) {
			int messageId = messageId().getNextMessageId(this.getClientId());
			channel().writeAndFlush(ClientProtocolUtil.unSubscribeMessage(topics, messageId));
		}
	}

	@Override
	public void saveMesage(MessageData recviceMessage) {
		//if (msgListCache == null) {return;}
		if ((recviceMessage != null) && (recviceMessage.getMessageId() > 0)) {
			log.debug("saveMessage: {}", recviceMessage.getMessageId());
			msgListCache.put(String.valueOf(recviceMessage.getMessageId()), recviceMessage);
		}
	}

	@Override
	public void delMesage(int messageId) {
		if (msgListCache == null) {return;}
		if (messageId > 0) {
			log.debug("delMesage: {}", messageId);
			msgListCache.remove(String.valueOf(messageId));
		}
	}

	public MessageData changeMessageStatus(int messageId, MessageStatus status) {
		if (msgListCache == null) {return null;}
		log.debug("changeMessageStatus: {}", status.name());

		MessageData msgObj = msgListCache.get(String.valueOf(messageId));
		if (msgObj != null) {
			msgObj.setStatus(status);
			msgListCache.put(String.valueOf(messageId), msgObj);
		}
		return msgObj;
	}

	@Override
	public void processPubRel(int messageId) {
		MessageData msgObj = changeMessageStatus(messageId, MessageStatus.PUBREL);

		if ((msgObj != null) && (listener != null)) {
			if (msgObj.getQos() == MqttQoS.EXACTLY_ONCE.value()) {
				listener.receiveMessage(msgObj.getMessageId(), msgObj.getTopic(), decode(msgObj.getPayload()));
			}
		}
		log.debug("process Pub-rel: messageId - {} ", messageId);

	}

	@Override
	public void processSubAck(int messageId) {
		log.debug("process Sub-ack: messageId - {} ", messageId);
	}

	@Override
	public void processUnSubBack(int messageId) {
		log.debug("process Un-sub-back: messageId - {} ", messageId);
	}

	@Override
	public void processPublish(MessageData msg) {
		log.debug("process Publish: {} ", msg);

		if (listener != null) {
			listener.receiveMessageByAny(msg.getMessageId(), msg.getTopic(), decode(msg.getPayload()));

			if (msg.getQos() == MqttQoS.AT_MOST_ONCE.value() || msg.getQos() == MqttQoS.EXACTLY_ONCE.value()) {
				listener.receiveMessage(msg.getMessageId(), msg.getTopic(), decode(msg.getPayload()));
			}
		}
	}

	@Override
	public void sendPubRecMessage(int messageId) {
		log.debug("send Pub-rec: messageId - {} ", messageId);
		channel().writeAndFlush(ClientProtocolUtil.pubRecMessage(messageId));
	}

	@Override
	public void sendPubAckMessage(int messageId) {
		log.debug("send Pub-ack: messageId - {} ", messageId);
		channel().writeAndFlush(ClientProtocolUtil.pubAckMessage(messageId));
	}

	@Override
	public void sendPubCompMessage(int messageId) {
		log.debug("send Pub-comp: messageId - {} ", messageId);
		channel().writeAndFlush(ClientProtocolUtil.pubCompMessage(messageId));
	}

	@Override
	public void setConsumerListener(MqttConsumerListener listener) {
		this.listener = listener;
	}

	@Override
	public void setCacheList(CacheList<MessageData> msgList) {
		if (msgList != null) {
			this.msgListCache = msgList;
		}
	}
}
