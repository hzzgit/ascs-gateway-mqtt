package net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.protocol.data;


import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.common.ConsumerMessage;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class ConsumerClientData extends BaseDataInMap<Integer, ConsumerMessage> {
	private static final long serialVersionUID = 1L;

	public ConsumerClientData(String name) {
		super(name);
	}
	
	public ConsumerClientData() {

	}
}
