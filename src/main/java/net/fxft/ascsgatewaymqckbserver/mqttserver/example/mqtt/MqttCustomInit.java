
package net.fxft.ascsgatewaymqckbserver.mqttserver.example.mqtt;


import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.MqttServer;

/**
 * @author ben
 * @Title: CustomInit.java
 * @Description:
 **/

public interface MqttCustomInit {
	/**
	 * init
	 * @param mqttServer
	 */
	public void init(MqttServer mqttServer);
}
