package net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.*;
import net.fxft.ascsgatewaymqckbserver.common.NettyLog;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.protocol.ClientProtocolProcess;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class MqttClientHandler extends SimpleChannelInboundHandler<Object> {
	private ClientProtocolProcess clientProtocolProcess;

	public MqttClientHandler(ClientProtocolProcess clientProtocolProcess) {
		this.clientProtocolProcess = clientProtocolProcess; 
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msgx) throws Exception {
		if (msgx == null) {return ;}
		MqttMessage msg = (MqttMessage) msgx;
		NettyLog.debug("read: {}", msg.fixedHeader().messageType());
		MqttFixedHeader mqttFixedHeader = msg.fixedHeader();
		switch (mqttFixedHeader.messageType()) {
		case CONNACK:
			clientProtocolProcess.processConnectBack(ctx.channel(), (MqttConnAckMessage) msg);
			break;
		case UNSUBACK:
			clientProtocolProcess.processUnSubBack(ctx.channel(), msg);
			break;
		case PUBLISH:
			clientProtocolProcess.processPublish(ctx.channel(), (MqttPublishMessage) msg);
			break;
		case PUBACK:
			clientProtocolProcess.processPubAck(ctx.channel(), msg);
			break;
		case PUBREC:
			clientProtocolProcess.processPubRec(ctx.channel(), msg);
			break;
		case PUBREL:
			clientProtocolProcess.processPubRel(ctx.channel(), msg);
			break;
		case PUBCOMP:
			clientProtocolProcess.processPubComp(ctx.channel(), msg);
			break;
		case SUBACK:
			clientProtocolProcess.processSubAck(ctx.channel(), (MqttSubAckMessage) msg);
			break;
		default:
			break;
		}
	}
}
