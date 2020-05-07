//package net.fxft.ascsgatewaymqckbserver.mqttserver.server;
//
//
//import net.fxft.ascsgatewaymqckbserver.common.NettyLog;
//import net.fxft.ascsgatewaymqckbserver.common.api.SocketApplication;
//import net.fxft.ascsgatewaymqckbserver.mqttserver.server.core.BaseServer;
//import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.MqttServer;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
///**
// * @author ben
// * @Title: basic
// * @Description:
// **/
//
//@SpringBootApplication
//public class TestServerApplication implements CommandLineRunner, SocketApplication {
//	public static void main(String[] args) {
//		SpringApplication.run(TestServerApplication.class, args);
//	}
//
//	private BaseServer scoketServer;
//
//	@Override
//	public void run(String... strings) {
//		scoketServer = new MqttServer();
//		scoketServer.bind(8989);
//		NettyLog.info("testserver run end ");
//	}
//
//	@Override
//	public void shutdown() {
//		if (scoketServer != null) {
//			scoketServer.shutdown();
//		}
//	}
//}