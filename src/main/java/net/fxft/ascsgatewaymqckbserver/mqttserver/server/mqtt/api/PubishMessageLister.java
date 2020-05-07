package net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.api;


import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.common.BorkerMessage;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/


public interface PubishMessageLister {
	/**
	 * custom define process
	 * @param msg
	 */
	void processMessage(BorkerMessage msg);
}
