package net.fxft.ascsgatewaymqckbserver.mqttserver.example.mqtt;

import com.alibaba.fastjson.JSON;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.MqttServer;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.api.InternalSend;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.common.InternalMessage;


/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class MqttCustomCommunicate extends BaseMqttCustom implements InternalSend, MqttServerRecvice {
	@Override
	public void init(MqttServer mqttServer) {
		super.init(mqttServer);
		mqttServer.initMqtt().setInternalSend(this);
	}

	@Override
	public void internalSend(InternalMessage msg) {
		processInternalSend(JSON.toJSONString(msg));
	}
	protected void processInternalSend(String msg) {	
	}

	public boolean processInternalRecvice(InternalMessage msg) {	
		return mqttServer.internalRecvice().processInternalRecvice(msg);
	}

	@Override
	public boolean processServerRecviceMesage(String message) {
		InternalMessage msgObj = JSON.parseObject(message, InternalMessage.class);
		return processInternalRecvice(msgObj);
	}
}
