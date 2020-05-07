package net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.api;


import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.common.InternalMessage;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public interface InternalRecvice {
	/**
	 * 接收内部信息处理
	 * @param msg
	 * @return
	 */
	public boolean processInternalRecvice(InternalMessage msg);
}