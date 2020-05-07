package net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt;


import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import lombok.extern.slf4j.Slf4j;
import net.fxft.ascsgatewaymqckbserver.common.exception.MethodNotSupportException;
import net.fxft.ascsgatewaymqckbserver.common.utils.StringsUtil;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.core.BaseClient;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.api.MqttConsumer;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.api.MqttConsumerProcess;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.api.MqttProducer;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.api.MqttProducerProcess;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.api.impl.MqttConsumerProcessImpl;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.api.impl.MqttProducerProcessImpl;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.common.MqttConnectOptions;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.protocol.ClientProtocolProcess;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.protocol.ClientProtocolUtil;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/
@Slf4j
public class MqttClient extends BaseClient {
	private MqttConnectOptions info = new MqttConnectOptions();
	private MqttProducerProcess producerProcess;
	private MqttConsumerProcess consumerProcess;
	private MqttConsumer consumer;
	private MqttProducer producer;

	public MqttClient() {
		this.setSyncConnect(true);

		info.setClientIdentifier(StringsUtil.getUuid());
		MqttProducerProcessImpl producerProcessObj = new MqttProducerProcessImpl(this);
		MqttConsumerProcessImpl consumerProcessObj = new MqttConsumerProcessImpl(this);

		producerProcess = producerProcessObj;
		consumerProcess = consumerProcessObj;
		
		consumer = consumerProcess;
		producer = producerProcess;

	}
	
	public MqttConnectOptions mqttOptions() {
		return info;
	}

	public MqttConsumer consumer() {
		return consumer;
	}

	public MqttProducer producer() {
		return producer;
	}
	
	@Override
	public String getClientId() {
		return info.getClientIdentifier();
	}

	@Override
	protected void initConnect() {
		super.initConnect();
	}

	@Override
	public void sendMessage(String msg) {
		throw new MethodNotSupportException();
	}

	@Override
	protected void initSocketChannel(SocketChannel ch) {
		ClientProtocolProcess proObj = new ClientProtocolProcess(this, consumerProcess, producerProcess);
		super.initSocketChannel(ch);

		ch.pipeline().addLast("decoder", new MqttDecoder());
		ch.pipeline().addLast("encoder", MqttEncoder.INSTANCE);
		ch.pipeline().addLast("mqttHander", new MqttClientHandler(proObj));
	}

	@Override
	protected boolean loginInit() {
		log.debug("loginInit: " + info.toString());
		channel.writeAndFlush(ClientProtocolUtil.connectMessage(info));
		return true;
	}

	@Override
	public void disConnect() {
		if (channel != null) {
			log.debug("disConnect: ");
			channel.writeAndFlush(ClientProtocolUtil.disConnectMessage());
		}
	}
}
