package net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import net.fxft.ascsgatewaymqckbserver.common.NettyUtil;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.protocol.ProtocolProcess;

import java.io.IOException;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/
@Slf4j
public class MqttServerHandler extends SimpleChannelInboundHandler<MqttMessage> {

	private ProtocolProcess protocolProcess;

	public MqttServerHandler(ProtocolProcess protocolProcess) {
		this.protocolProcess = protocolProcess;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) throws Exception {
		
		if (!msg.decoderResult().isSuccess()) {
			log.error("error decoder");
			ctx.close(); 
			return;
		}
		
		log.debug("read: " + msg.fixedHeader().messageType());
		
		if (msg.fixedHeader().messageType() == MqttMessageType.CONNECT) {
			protocolProcess.processConnect(ctx.channel(), (MqttConnectMessage) msg);
		} else {
			if (!NettyUtil.isLogin(ctx.channel())) {
				log.info("not login");
				return ;
			}
		}

		switch (msg.fixedHeader().messageType()) {
		case CONNECT:
			break;
		case CONNACK:
			break;
		case PUBLISH:
			protocolProcess.processPublish(ctx.channel(), (MqttPublishMessage) msg);
			break;
		case PUBACK:
			protocolProcess.processPubAck(ctx.channel(), (MqttMessageIdVariableHeader) msg.variableHeader());
			break;
		case PUBREC:
			protocolProcess.processPubRec(ctx.channel(), (MqttMessageIdVariableHeader) msg.variableHeader());
			break;
		case PUBREL:
			protocolProcess.processPubRel(ctx.channel(), (MqttMessageIdVariableHeader) msg.variableHeader());
			break;
		case PUBCOMP:
			protocolProcess.processPubComp(ctx.channel(), (MqttMessageIdVariableHeader) msg.variableHeader());
			break;
		case SUBSCRIBE:
			protocolProcess.processSubscribe(ctx.channel(), (MqttSubscribeMessage) msg); 
			break;
		case SUBACK:
			break;
		case UNSUBSCRIBE:
			protocolProcess.processUnSubscribe(ctx.channel(), (MqttUnsubscribeMessage) msg);
			break;
		case UNSUBACK:
			break;
		case PINGREQ:
			protocolProcess.processPingReq(ctx.channel(), msg);
			break;
		case PINGRESP:
			break;
		case DISCONNECT:
			protocolProcess.processDisConnect(ctx.channel(), msg);
			break;
		default:
			break;
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (cause instanceof IOException) {
			// 远程主机强迫关闭了一个现有的连接的异常
			ctx.close();
		} else {
			super.exceptionCaught(ctx, cause);
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
			if (idleStateEvent.state() == IdleState.ALL_IDLE) {
				this.protocolProcess.processWillMessage(ctx.channel());
				ctx.close();
			}
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}
}