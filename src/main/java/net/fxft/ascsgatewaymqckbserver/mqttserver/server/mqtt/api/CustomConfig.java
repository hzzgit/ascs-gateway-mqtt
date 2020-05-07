package net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.api;


import net.fxft.ascsgatewaymqckbserver.common.core.CacheList;
import net.fxft.ascsgatewaymqckbserver.common.core.UniqueIdInteger;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.MqttSession;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.protocol.data.ClientTopic;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.protocol.data.ConsumerClientData;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.protocol.data.ProcedureClientData;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.protocol.data.TopicData;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public interface CustomConfig {
	/**
	 * setInternalSend
	 * @param internalSend
	 */
	public void setInternalSend(InternalSend internalSend);
	
	/**
	 * setAuthService
	 * @param authService
	 */
	public void setAuthService(MqttAuth authService);
	
	/**
	 * setConsumerCacheList
	 * @param cacheList
	 */
	public void setConsumerCacheList(CacheList<ConsumerClientData> cacheList);
	
	/**
	 * setProcedureCacheList
	 * @param cacheList
	 */
	public void setProcedureCacheList(CacheList<ProcedureClientData> cacheList);
	
	/**
	 * setTopicList
	 * @param cacheList
	 */
	public void setTopicList(CacheList<TopicData> cacheList);
	
	/**
	 * setClientTopicList
	 * @param cacheList
	 */
	public void setClientTopicList(CacheList<ClientTopic> cacheList);
	

	/**
	 * setGlobalUniqueIdCacheList
	 * @param cacheList
	 */
	public void setGlobalUniqueIdCacheList(CacheList<UniqueIdInteger> cacheList);
	
	/**
	 * 自定义生产者消息处理
	 * @param msgLister
	 */
	public void setPubishMessageLister(PubishMessageLister msgLister);
	
	/**
	 * 会话远端存储设置
	 * @param cacheList
	 */
	public void setRemoteSessionCache(CacheList<MqttSession> cacheList);
}
