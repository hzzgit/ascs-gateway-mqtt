package net.fxft.ascsgatewaymqckbserver.mqttserver.example.mqtt;


import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.MqttServer;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.api.MqttAuth;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.api.PubishMessageLister;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.common.BorkerMessage;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class MqttCustom extends BaseMqttCustom implements MqttAuth, PubishMessageLister {
	@Override
	public boolean checkValid(String clientId, String username, String password) {
		System.err.println(String.format("login: clientId-%s, username-%s, password-%s", clientId, username, password));
		return true;
	}
	
	@Override
	public void processMessage(BorkerMessage msg) {
		System.err.println("msg: " + msg);
	}
	
	@Override
	public void init(MqttServer mqttServer) {
		super.init(mqttServer);
		mqttServer.initMqtt().setAuthService(this);
		mqttServer.initMqtt().setPubishMessageLister(this);
	}
}
