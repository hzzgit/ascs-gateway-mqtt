
package net.fxft.ascsgatewaymqckbserver.mqttclient.example.mqtt;


import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.MqttClient;

/**
 * @author ben
 * @Title: CustomInit.java
 * @Description:
 **/

public interface MqttClientCustomInit {
	/**
	 * init
	 * @param nettyClient
	 */
	public void init(MqttClient nettyClient);
}
