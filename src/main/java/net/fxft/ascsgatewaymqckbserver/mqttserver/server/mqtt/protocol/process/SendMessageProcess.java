package net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.protocol.process;


import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import lombok.extern.slf4j.Slf4j;
import net.fxft.ascsgatewaymqckbserver.common.NettyUtil;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.common.ConsumerMessage;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.protocol.ProtocolUtil;

import java.util.List;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/
@Slf4j
public class SendMessageProcess {

	/**
	 *  B - S (Qos1, Qos2)
	 * @param channel
	 * @param info
	 */
	public void sendPublishRetryMessage(Channel channel, ConsumerMessage info) {
		log.debug("sendPublishRetryMessage: {}", info);
		if (info != null) {
			this.sendPublishMessage(channel, info.getTopic(), info.isDup(), info.getMqttQoS(), info.isRetain(),
					info.getMessageId(), info.getMessageBytes());
		}
	}

	/**
	 *  B - S (Qos0 Qos1 Qos2)
	 * @param channel
	 * @param topicName
	 * @param isDup
	 * @param iQoS
	 * @param isRetain
	 * @param messageId
	 * @param bytes
	 */
	public void sendPublishMessage(Channel channel, String topicName, boolean isDup, int iQoS, boolean isRetain,
			int messageId, byte[] bytes) {
		log.debug("sendPublishMessage: msgId- {}", messageId);
		channel.writeAndFlush(ProtocolUtil.publishMessage(topicName, isDup, iQoS, isRetain, messageId, bytes));
	}

	/**
	 * sendPublishMessage obj
	 * @param channel
	 * @param publishMessage
	 */
	public void sendPublishMessage(Channel channel, MqttPublishMessage publishMessage) {
		log.debug("sendPublishMessage");
		channel.writeAndFlush(publishMessage);
	}


	/**
	 *  B - P (Qos2)
	 * @param channel
	 * @param msgId
	 */
	public void sendPubCompMessage(Channel channel, int msgId) {
		log.debug("sendPubCompMessage");
		channel.writeAndFlush(ProtocolUtil.pubCompMessage(msgId));
	}


	/**
	 *  B - P (Qos1)
	 * @param channel
	 * @param messageId
	 */
	public void sendPubAckMessage(Channel channel, int messageId) {
		log.debug("sendPubAckMessage");
		channel.writeAndFlush(ProtocolUtil.pubAckMessage(messageId));
	}


	/**
	 *  B - P (Qos2)
	 * @param channel
	 * @param messageId
	 */
	public void sendPubRecMessage(Channel channel, int messageId) {
		log.debug("sendPubRecMessage");
		channel.writeAndFlush(ProtocolUtil.pubRecMessage(messageId));
	}

	/**
	 *  B - P, B - S
	 * @param channel
	 * @param code
	 * @param bSessionPresent
	 */

	public void sendBackConnect(Channel channel, MqttConnectReturnCode code, boolean bSessionPresent) {
		log.debug("sendBackConnect");
		channel.writeAndFlush(ProtocolUtil.connAckMessage(code, bSessionPresent));
	}
	
	/**
	 *  B - S, B-P
	 * @param channel
	 */
	public void sendPingRespMessage(Channel channel) {
		log.debug("sendPingRespMessage - clientId: {}", NettyUtil.getClientId(channel));
		channel.writeAndFlush(ProtocolUtil.pingRespMessage());
	}

	/**
	 *  B - S
	 * @param channel
	 * @param messageId
	 * @param mqttQoSList
	 */
	public void sendSubAckMessage(Channel channel, int messageId, List<Integer> mqttQoSList) {
		log.debug("sendSubAckMessage - clientId: {}, msgID: {}", NettyUtil.getClientId(channel), messageId);
		channel.writeAndFlush(ProtocolUtil.subAckMessage(messageId, mqttQoSList));
	}
	
	/**
	 *  B - S
	 * @param channel
	 * @param messageId
	 * @param bDup
	 */
	public void sendPubRelMessage(Channel channel, int messageId, boolean bDup) {
		log.debug("sendPubRelMessage - clientId: {}, msgID: {}", NettyUtil.getClientId(channel), messageId);
		channel.writeAndFlush(ProtocolUtil.pubRelMessage(messageId, bDup));
	}
	
	/**
	 *  B - S
	 * @param channel
	 * @param messageId
	 */
	public void sendUnSubAckMessage(Channel channel, int messageId) {
		log.debug("sendUnSubAckMessage - clientId: {}, msgID: {}", NettyUtil.getClientId(channel), messageId);
		channel.writeAndFlush(ProtocolUtil.unsubAckMessage(messageId));
	}
}
