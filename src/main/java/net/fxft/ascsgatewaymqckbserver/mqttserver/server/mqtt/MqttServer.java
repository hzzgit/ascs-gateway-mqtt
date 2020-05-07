package net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt;


import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.core.BaseServer;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.api.CustomConfig;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.api.InternalRecvice;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.api.PubishMessageLister;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.common.InternalMessage;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.protocol.ProtocolProcess;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.protocol.ProtocolProcessConfig;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.protocol.ProtocolUtil;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class MqttServer extends BaseServer {
	protected final String HANDLER_MQTTDECODER="mqttDecoder";
	protected final String HANDLER_MQTTENCODER="mqttEncoder";
	protected final String HANDLER_MQTTHANDER="mqttHander";
	
	private ProtocolProcessConfig cfg;
	private ProtocolProcess protocolProcess;

	public MqttServer( ) {
		checkHeartbeat = true;
		readerIdleTimeSeconds = 0;
		writerIdleTimeSeconds = 0;
		allIdleTimeSeconds = 60;
		protocolProcess = new ProtocolProcess();
		cfg = new ProtocolProcessConfig(protocolProcess);
		protocolProcess.init(cfg);
	}

	public MqttServer(PubishMessageLister pubishMessageLister) {
		checkHeartbeat = true;
		readerIdleTimeSeconds = 0;
		writerIdleTimeSeconds = 0; 
		allIdleTimeSeconds = 60;
		
		protocolProcess = new ProtocolProcess();
		if(pubishMessageLister!=null){//这里允许直接注入接收到程序之后的处理监听器
			protocolProcess.setPubishMessageLister(pubishMessageLister);
		}
		cfg = new ProtocolProcessConfig(protocolProcess);
		protocolProcess.init(cfg);
	}
	
	public InternalRecvice internalRecvice() {
		return this.protocolProcess;
	}
	
	public CustomConfig initMqtt() {
		return cfg;
	}
	
	@Override
	public void broadcastMessageString(String msg) {
		this.broadcastMessage(ProtocolUtil.publishMessage("/broadcast", msg.getBytes()));
	}
 

	
	@Override
	protected void initSocketChannel(SocketChannel ch) {
		super.initSocketChannel(ch);
		ch.pipeline().addLast(HANDLER_MQTTDECODER, new MqttDecoder());
		ch.pipeline().addLast(HANDLER_MQTTENCODER, MqttEncoder.INSTANCE);
		ch.pipeline().addLast(HANDLER_MQTTHANDER, new MqttServerHandler(protocolProcess));	
	}

	public void testInternalMessage() {	
		InternalMessage msg =
				InternalMessage.builder().topicName("testInternalMessage").build(); 
		if (cfg.consumerProcess.getInternalSend() != null) {
			cfg.consumerProcess.getInternalSend().internalSend(msg);
		}
	}
}
