package net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.protocol;


import io.netty.handler.codec.mqtt.MqttPublishMessage;
import net.fxft.ascsgatewaymqckbserver.common.MqttProtocolUtil;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class ProtocolUtil extends MqttProtocolUtil {
	public static MqttPublishMessage publishMessage(String topic, byte[] payload) {
		return publishMessage(topic, payload, 0, 10, false);
	}
}
