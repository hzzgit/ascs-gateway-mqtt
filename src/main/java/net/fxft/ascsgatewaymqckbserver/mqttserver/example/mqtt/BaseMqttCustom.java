package net.fxft.ascsgatewaymqckbserver.mqttserver.example.mqtt;


import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.MqttServer;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class BaseMqttCustom implements MqttCustomInit{
	protected MqttServer mqttServer;

	@Override
	public void init(MqttServer mqttServer) {
		this.mqttServer = mqttServer;
	}
}
