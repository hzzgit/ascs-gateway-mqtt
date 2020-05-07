package net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.api;


import net.fxft.ascsgatewaymqckbserver.common.api.GlobalUniqueIdSet;
import net.fxft.ascsgatewaymqckbserver.common.core.CacheList;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.common.MessageData;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public interface MqttConsumer extends GlobalUniqueIdSet {
	/**
	 * 自定义缓存
	 * @param msgList
	 */
	public void setCacheList(CacheList<MessageData> msgList);
	
	/**
	 * 订阅主题
	 * @param topic
	 * @param qosValue
	 */
	public void subscribe(String topic, int qosValue);
	
	/**
	 * 接收订阅主题消息
	 * @param listener
	 */
	public void setConsumerListener(MqttConsumerListener listener);
	

}
