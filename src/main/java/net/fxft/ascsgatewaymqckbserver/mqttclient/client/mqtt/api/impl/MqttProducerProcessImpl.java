package net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.api.impl;


import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.*;
import net.fxft.ascsgatewaymqckbserver.common.NettyLog;
import net.fxft.ascsgatewaymqckbserver.common.core.CacheList;
import net.fxft.ascsgatewaymqckbserver.common.core.CacheListLocalMemory;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.core.BaseClient;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.api.ClientProcess;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.api.MqttProducer;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.api.MqttProducerProcess;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.common.MessageData;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.common.MessageStatus;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.protocol.ClientProtocolUtil;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/
public class MqttProducerProcessImpl extends ClientProcess implements MqttProducerProcess, MqttProducer {
	@Setter
	@NonNull
	private CacheList<MessageData> msgListCache;

	public MqttProducerProcessImpl(BaseClient client) {
		this(client, new CacheListLocalMemory<MessageData>());
	}
	
	public MqttProducerProcessImpl(BaseClient client, CacheList<MessageData> msgListCache) {
		super(client);
		this.msgListCache = msgListCache;
	}

	private MessageData buildMqttMessage(String topic, byte[] message, int qosValue, boolean isReatain, boolean dup) {
		int msgId = 0;
		if (qosValue == MqttQoS.AT_LEAST_ONCE.value()) {
			msgId = messageId().getNextMessageId(getClientId());
		} else if (qosValue == MqttQoS.EXACTLY_ONCE.value()) {
			msgId = messageId().getNextMessageId(getClientId());
		}

		return MessageData.builder().messageId(msgId).topic(topic).dup(dup).retained(isReatain).qos(qosValue)
				.status(MessageStatus.PUB)
				.timestamp(System.currentTimeMillis()).payload(message)
				.build();
	}

	private void sendPubishMessage(MessageData sendMqttMessage) {
		if (sendMqttMessage == null) {
			return;
		}
		if (!channel().isActive()) {
			NettyLog.debug("channel is close");
			return;
		}
		saveMessage(sendMqttMessage);

		NettyLog.debug("sendPubishMessage: {}", sendMqttMessage.toString());
		channel().writeAndFlush(ClientProtocolUtil.publishMessage(sendMqttMessage));
	}

	@Override
	public void saveMessage(MessageData sendMqttMessage) {
		if (msgListCache == null) {
			return;
		}
		if ((sendMqttMessage != null) && (sendMqttMessage.getMessageId() > 0)) {
			NettyLog.debug("saveMessage: {}", sendMqttMessage.getMessageId());
			msgListCache.put(String.valueOf(sendMqttMessage.getMessageId()), sendMqttMessage);
		}
	}

	@Override
	public void delMessage(int messageId) {
		if (msgListCache == null) {
			return;
		}
		
		if (messageId > 0) {
			NettyLog.debug("delMessage: {}", messageId);
			msgListCache.remove(String.valueOf(messageId));
		}
	}
	
	public void changeMessageStatus(int messageId, MessageStatus status) {
		if (msgListCache == null) {
			return;
		}
		
		MessageData msgObj = msgListCache.get(String.valueOf(messageId));
		if (msgObj != null) {
			msgObj.setStatus(status);
			msgListCache.put(String.valueOf(messageId), msgObj);
		}
	}

	@Override
	public void sendPubishMessage(String topic, byte[] message, int qosValue, boolean isRetain) {
		sendPubishMessage(buildMqttMessage(topic, message, qosValue, isRetain, false));
	}

	@Override
	public void sendPubRel(int messageId) {
		NettyLog.debug("send Pub-Rel: {}", messageId);
		channel().writeAndFlush(ClientProtocolUtil.pubRelMessage(messageId, false));
	}

	@Override
	public void processPubRec(int messageId) {
		NettyLog.debug("process Pub-Rec: {}", messageId);
		changeMessageStatus(messageId, MessageStatus.PUBREC);
	}

	@Override
	public void processPubAck(int messageId) {
		NettyLog.debug("process Pub-Ack: {} ; list size: {}", messageId, msgListCache.size());
	}

	@Override
	public void processPubComp(int messageId) {
		NettyLog.debug("process Pub-Comp: {}", messageId);
	}

	@Override
	public void setCacheList(CacheList<MessageData> msgList) {
		if (msgList != null) {
			this.msgListCache = msgList;
		}
	}
}
