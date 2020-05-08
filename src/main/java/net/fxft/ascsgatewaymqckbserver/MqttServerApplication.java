package net.fxft.ascsgatewaymqckbserver;


import net.fxft.ascsgatewaymqckbserver.common.NettyLog;
import net.fxft.ascsgatewaymqckbserver.common.api.SocketApplication;
import net.fxft.ascsgatewaymqckbserver.listen.SpringBeanUtil;
import net.fxft.ascsgatewaymqckbserver.mqttserver.example.mqtt.MqttCustom;
import net.fxft.ascsgatewaymqckbserver.mqttserver.example.mqtt.MqttCustomInit;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.MqttServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;
import java.util.Map.Entry;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@SpringBootApplication
public class MqttServerApplication implements CommandLineRunner, SocketApplication {
	public static void main(String[] args) {
		SpringApplication.run(MqttServerApplication.class, args);
	}

	private MqttServer mqttServer;

	@Override
	public void run(String... strings) {
		mqttServer = new MqttServer(new MqttCustom());
		//initCustom(mqttServer);
		mqttServer.bind(8982);

		NettyLog.info("mqtt server run end ");


	}

	private void initCustom(MqttServer mqttServer) {
		MqttCustom mqttCustom = new MqttCustom();
		mqttCustom.init(mqttServer);

		Map<String, MqttCustomInit> result = SpringBeanUtil.getApplicationContext().getBeansOfType(MqttCustomInit.class);
		for (Entry<String, MqttCustomInit> app : result.entrySet()) {
			NettyLog.info("init custom - " +app.getKey());
			app.getValue().init(mqttServer);
		}
	}

	@Override
	public void shutdown() {
		if (mqttServer != null) {
			mqttServer.shutdown();
			mqttServer = null;
		}
	}
}