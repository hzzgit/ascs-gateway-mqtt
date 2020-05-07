package net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.api.impl;


import net.fxft.ascsgatewaymqckbserver.common.NettyLog;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.api.MqttAuth;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class AuthServiceImpl implements MqttAuth {
	@Override
	public boolean checkValid(String deviceId, String username, String password) {
		NettyLog.debug("AuthServiceImpl");
		return true;
	}
	
}
