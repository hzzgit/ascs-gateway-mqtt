package net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.protocol;


import io.netty.handler.codec.mqtt.*;
import net.fxft.ascsgatewaymqckbserver.common.MqttProtocolUtil;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.common.MessageData;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.common.MqttConnectOptions;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.common.SubscribeMessage;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class ClientProtocolUtil extends MqttProtocolUtil {
	public static MqttConnectMessage connectMessage(MqttConnectOptions info) {
		MqttVersion verinfo = info.getMqttVersion();
			MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.CONNECT, false, MqttQoS.AT_MOST_ONCE,
					false, 10);
		MqttConnectVariableHeader mqttConnectVariableHeader = new MqttConnectVariableHeader(verinfo.protocolName(),
				verinfo.protocolLevel(), info.isHasUserName(), info.isHasPassword(), info.isHasWillRetain(),
				info.getWillQos(), info.isHasWillFlag(), info.isHasCleanSession(), info.getKeepAliveTime());
		MqttConnectPayload mqttConnectPayload = new MqttConnectPayload(info.getClientIdentifier(), info.getWillTopic(),
				info.getWillMessage(), info.getUserName(), info.getPassword());
		MqttConnectMessage mqttSubscribeMessage = new MqttConnectMessage(mqttFixedHeader, mqttConnectVariableHeader,
				mqttConnectPayload);
		return mqttSubscribeMessage;
	}

	public static MqttPublishMessage publishMessage(MessageData mqttMessage) {
		return publishMessage(mqttMessage.getTopic(), mqttMessage.isDup(), mqttMessage.getQos(),
				mqttMessage.isRetained(), mqttMessage.getMessageId(), mqttMessage.getPayload());
	}
	
	public static List<MqttTopicSubscription> getSubscribeTopics(SubscribeMessage[] sMsgObj) {
		if (sMsgObj != null) {
			List<MqttTopicSubscription> list = new LinkedList<>();
			for (SubscribeMessage sb : sMsgObj) {
				MqttTopicSubscription mqttTopicSubscription = new MqttTopicSubscription(sb.getTopic(),
						MqttQoS.valueOf(sb.getQos()));
				list.add(mqttTopicSubscription);
			}
			return list;
		} else {
			return null;
		}
	}
	
	public static List<String> getTopics(SubscribeMessage[] sMsgObj) {
		if (sMsgObj != null) {
			List<String> mqttTopicSubscriptions = new LinkedList<>();
			for (SubscribeMessage sb : sMsgObj) {
				mqttTopicSubscriptions.add(sb.getTopic());
			}
			return mqttTopicSubscriptions;
		} else {
			return null;
		}
	}
	
	public static MqttSubscribeMessage subscribeMessage(int messageId, SubscribeMessage... msg) {
		return ClientProtocolUtil.subscribeMessage(getSubscribeTopics(msg), messageId);
	}

}
