package net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.api;


import net.fxft.ascsgatewaymqckbserver.common.core.CacheList;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.common.ConsumerMessage;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.protocol.data.ConsumerClientData;

import java.util.List;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public interface ConsumerDataService {
	/**
	 * 自定义缓存
	 * @param cacheList
	 */
	public void setConsumerCacheList(CacheList<ConsumerClientData> cacheList);
	
	/**
	 * 向消费者发送信息
	 * @param clientId
	 * @param dupPublishMessage
	 */
	public void putPublishMessage(String clientId, ConsumerMessage dupPublishMessage);
	
	/**
	 * 获取未确认的信息
	 * @param clientId
	 * @return
	 */
	public List<ConsumerMessage> getPublishMessage(String clientId);
	
	/**
	 * 删除指定用户的信息
	 * @param clientId
	 * @param messageId
	 */
	public void removePublishMessage(String clientId, int messageId);
	
	/**
	 * 用户退出处理
	 * @param clientId
	 */
	public void removeByClient(String clientId);
}
