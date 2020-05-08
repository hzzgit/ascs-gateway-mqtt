package net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.protocol.process;


import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.fxft.ascsgatewaymqckbserver.common.NettyUtil;
import net.fxft.ascsgatewaymqckbserver.common.api.GlobalUniqueId;
import net.fxft.ascsgatewaymqckbserver.common.api.GlobalUniqueIdImpl;
import net.fxft.ascsgatewaymqckbserver.common.core.CacheList;
import net.fxft.ascsgatewaymqckbserver.common.core.UniqueIdInteger;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.MqttSession;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.api.ConsumerDataService;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.api.InternalSend;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.api.MqttSessionService;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.common.*;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.protocol.ProtocolUtil;

import java.util.List;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/
@Slf4j
public class ConsumerProcess {
	private SendMessageProcess sendProcess;
	private ConsumerDataService consumerData;
	private MqttSessionService sessionService;
	
	private GlobalUniqueId messageIdService;

	public SendMessageProcess getSendProcess() {
		return sendProcess;
	}

	@Setter
    @Getter
	private InternalSend internalSend;

	public ConsumerProcess(SendMessageProcess sendProcess, ConsumerDataService consumerData, MqttSessionService sessionService) {
		messageIdService = new GlobalUniqueIdImpl();
		this.sendProcess = sendProcess;
		this.consumerData = consumerData;
		this.sessionService = sessionService;
	}
	
	public void setGlobalUniqueIdCacheList(CacheList<UniqueIdInteger> cacheList) {
		messageIdService.setCacheList(cacheList);
	}

	public void processHistoryPub(Channel channel) {
		String clientId = NettyUtil.getClientId(channel);

		List<ConsumerMessage> dupPublishMessageStoreList = consumerData.getPublishMessage(clientId);
		dupPublishMessageStoreList.forEach(info -> {
			this.sendProcess.sendPublishRetryMessage(channel, info);
		});
	}

	public void removeByCleanSession(String clientId) {
		consumerData.removeByClient(clientId);
	}

	public void processPubRec(Channel channel, int messageId) {
		String clientId = NettyUtil.getClientId(channel);
		log.debug("PUBREC - clientId: {}, messageId: {}", clientId, messageId);
	}

	public void processPubComp(Channel channel, int messageId) {
		String clientId = NettyUtil.getClientId(channel);
		log.error("PUBCOMP - clientId: {}, messageId: {}", clientId, messageId);
		consumerData.removePublishMessage(clientId, messageId);
	}

	public void processPubAck(Channel channel, int messageId) {
		String clientId = NettyUtil.getClientId(channel);
		log.debug("PUBACK - clientId: {}, messageId: {}", clientId, messageId);
		consumerData.removePublishMessage(clientId, messageId);
	}
	
	public void sendRetainMessage(Channel channel, List<RetainMessage> retainMessageStores, MqttQoS mqttQoS) {
		retainMessageStores.forEach(info -> {
			MqttQoS respQoS = info.getIQosLevel() > mqttQoS.value() ? mqttQoS : MqttQoS.valueOf(info.getIQosLevel());
			BorkerMessage bMsgInfo = BorkerMessage.builder().sourceClientId(info.getSourceClientId())
					.sourceMsgId(info.getSourceMsgId()).topicName(info.getTopicName()).iQosLevel(info.getIQosLevel())
					.msgBytes(info.getMsgBytes()).retain(false).build();

			this.sendSubscribMessageForRetain(channel, respQoS, bMsgInfo);
		});
	}

	public void sendSubscribMessage(BorkerMessage bMsgInfo, List<SubscribeTopicInfo> topicClientList) {
		topicClientList.forEach(topicClient -> {
			String clientId = topicClient.getClientId();
		
			// 订阅者收到MQTT消息的QoS级别, 最终取决于发布消息的QoS和主题订阅的QoS
			MqttQoS respQoS = bMsgInfo.getIQosLevel() > topicClient.getMqttQoS()
					? MqttQoS.valueOf(topicClient.getMqttQoS())
					: MqttQoS.valueOf(bMsgInfo.getIQosLevel());

		    sendSubscribMessageForPublic(clientId, respQoS, bMsgInfo);	
		});
	}
	
	
	private boolean getMustStorePubMessage(MqttQoS respQoS) {
		boolean bSaveMsg = false;
		if (respQoS == MqttQoS.AT_MOST_ONCE) {
			bSaveMsg = false;
		} else if (respQoS == MqttQoS.AT_LEAST_ONCE) {
			bSaveMsg = true;
		} else if (respQoS == MqttQoS.EXACTLY_ONCE) {
			bSaveMsg = false;
			;
		}
		return bSaveMsg;
	}

	public void sendSubscribMessageForRetain(Channel channel, MqttQoS respQoS, BorkerMessage bMsgInfo) {
		String clientId = NettyUtil.getClientId(channel);
		boolean bSaveMsg = getMustStorePubMessage(respQoS);
		boolean bSend = true;
		sendSubscribMessage(clientId, channel, respQoS, bSaveMsg, bSend, bMsgInfo);
	}
	
	public void sendSubscribMessageForPublic(String clientId, MqttQoS respQoS, BorkerMessage bMsgInfo) {
		boolean bSaveMsg = getMustStorePubMessage(respQoS);
		boolean bSend = true;
		
		Channel channel = null;
		if (bSend) {
		    channel = processInternalMessage(clientId, respQoS, bMsgInfo);
			if (channel == null) {
				bSend = false;
				bSaveMsg = false;
			}
		}
		sendSubscribMessage(clientId, channel, respQoS, bSaveMsg, bSend, bMsgInfo);
	}
	
	public void sendSubscribMessageForInternal(InternalMessage msg) {
		String clientId = msg.getDestClientId();	
		MqttSession mqttSession = sessionService.getSession(clientId);
		if (mqttSession == null) {
			return;
		}
		
		MqttQoS respQoS = MqttQoS.valueOf(msg.getRespQoS()); 
		boolean bSaveMsg = getMustStorePubMessage(respQoS);
		boolean bSend = true;
		
		BorkerMessage bMsgInfo = BorkerMessage.builder()
				.topicName(msg.getTopicName())
				.msgBytes(msg.getMsgBytes())
				.dup(msg.isDup())
				.retain(msg.isRetain())
				.build();
		sendSubscribMessage(clientId, mqttSession.channel(), respQoS, bSaveMsg, bSend, bMsgInfo);
	}

	private void sendSubscribMessage(String clientId, Channel channel, MqttQoS respQoS, boolean bSavePubMsg,
			boolean bSend, BorkerMessage bMsgInfo) {

		if ((!bSavePubMsg) && (!bSend)) {
			return ;
		}
		
		String topicName = bMsgInfo.getTopicName();
		boolean isDup = bMsgInfo.isDup();
		boolean isRetain = bMsgInfo.isRetain();
		byte[] msgBytes = bMsgInfo.getMsgBytes();
		int bMsgId =  getMustSendMessageId(respQoS, clientId); 

		if (bSavePubMsg) {
			ConsumerMessage pubMsgObj = ConsumerMessage.builder().sourceClientId(clientId).topic(topicName)
					.mqttQoS(respQoS.value()).messageBytes(msgBytes).messageId(bMsgId).build();
			consumerData.putPublishMessage(clientId, pubMsgObj);
		}

		if (bSend && channel != null) {
			MqttPublishMessage publishMessage = ProtocolUtil.publishMessage(topicName, isDup, respQoS.value(), isRetain,
					bMsgId, msgBytes);
			
			this.sendProcess.sendPublishMessage(channel, publishMessage);
		}
	}
	
	private int getMustSendMessageId(MqttQoS qosLevel, String clientId) {
		int msgId = 0;
		if (qosLevel == MqttQoS.AT_MOST_ONCE) {
			msgId = 0;
		} else if (qosLevel == MqttQoS.AT_LEAST_ONCE) {
			msgId = messageIdService.getNextMessageId(clientId);
		} else if (qosLevel == MqttQoS.EXACTLY_ONCE) {
			msgId = messageIdService.getNextMessageId(clientId);
		}
		return msgId;
	}
	
	/**
	 * 
	 * @param clientId
	 * @param respQoS
	 * @param bMsgInfo
	 * @return
	 */
	private Channel processInternalMessage(String clientId, MqttQoS respQoS, BorkerMessage bMsgInfo) {
		Channel channel = null;
		MqttSession mqttSession = sessionService.getSession(clientId);
		if (mqttSession == null) {
			if (internalSend != null) {
				InternalMessage internalMessage = InternalMessage.builder().topicName(bMsgInfo.getTopicName())
						.destClientId(clientId).respQoS(respQoS.value()).msgBytes(bMsgInfo.getMsgBytes())
						.dup(bMsgInfo.isDup()).retain(bMsgInfo.isRetain())
						.build();
				internalSend.internalSend(internalMessage);
			}
		} else {
			channel = mqttSession.channel();
		}
		
		return channel;
	}
}
