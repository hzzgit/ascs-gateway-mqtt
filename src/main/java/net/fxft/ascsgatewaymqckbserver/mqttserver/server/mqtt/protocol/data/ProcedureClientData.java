package net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.protocol.data;


import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.common.ProcedureMessage;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class ProcedureClientData
		extends
			BaseDataInMap<Integer, ProcedureMessage> {
	private static final long serialVersionUID = 1L;

	public ProcedureClientData(String name) {
		super(name);
	}
	public ProcedureClientData() {

	}
}
