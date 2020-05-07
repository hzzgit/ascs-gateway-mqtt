package net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.api;


import net.fxft.ascsgatewaymqckbserver.common.api.GlobalUniqueIdSet;
import net.fxft.ascsgatewaymqckbserver.common.core.CacheList;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.common.MessageData;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/
public interface MqttProducer extends GlobalUniqueIdSet {
	/**
	 * 自定义缓存
	 * @param msgList
	 */
	void setCacheList(CacheList<MessageData> msgList);
	
	
	/**
	 * 发布消息
	 * @param topic
	 * @param message
	 * @param qosValue
	 * @param isRetain
	 */
	void sendPubishMessage(String topic, String message, int qosValue, boolean isRetain);
	

}
