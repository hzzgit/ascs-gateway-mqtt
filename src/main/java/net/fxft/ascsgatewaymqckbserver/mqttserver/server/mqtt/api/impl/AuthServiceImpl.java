package net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.api.impl;


import lombok.extern.slf4j.Slf4j;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.api.MqttAuth;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/
@Slf4j
public class AuthServiceImpl implements MqttAuth {
	@Override
	public boolean checkValid(String deviceId, String username, String password) {
		log.debug("AuthServiceImpl");
		return true;
	}
	
}
