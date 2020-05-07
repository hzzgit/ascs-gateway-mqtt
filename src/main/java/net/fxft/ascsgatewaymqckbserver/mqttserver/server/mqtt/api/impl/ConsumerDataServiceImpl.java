package net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.api.impl;


import net.fxft.ascsgatewaymqckbserver.common.core.CacheList;
import net.fxft.ascsgatewaymqckbserver.common.core.CacheListLocalMemory;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.api.ConsumerDataService;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.common.ConsumerMessage;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.protocol.data.ConsumerClientData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class ConsumerDataServiceImpl implements ConsumerDataService {
	private CacheList<ConsumerClientData> sureDataCache;

	public ConsumerDataServiceImpl() {
		sureDataCache = new CacheListLocalMemory<ConsumerClientData>();
	}
	
	@Override
	public void setConsumerCacheList(CacheList<ConsumerClientData> cache) {
		if (cache != null) {
			this.sureDataCache = cache;
		}
	}

	@Override
	public void putPublishMessage(String clientId, ConsumerMessage dupPublishMessage) {
		ConsumerClientData map = sureDataCache.containsKey(clientId) ? sureDataCache.get(clientId)
				: null;
		if (map == null) {
			map = new ConsumerClientData(clientId);
		}
		map.put(dupPublishMessage.getMessageId(), dupPublishMessage);
		sureDataCache.put(clientId, map);
	}

	@Override
	public List<ConsumerMessage> getPublishMessage(String clientId) {
		if (sureDataCache.containsKey(clientId)) {
			ConsumerClientData map = sureDataCache.get(clientId);
			Collection<ConsumerMessage> collection = map.values();
			return new ArrayList<ConsumerMessage>(collection);
		}
		return new ArrayList<ConsumerMessage>();
	}

	@Override
	public void removePublishMessage(String clientId, int messageId) {
		if (sureDataCache.containsKey(clientId)) {
			ConsumerClientData map = sureDataCache.get(clientId);
			if (map.containsKey(messageId)) {
				map.remove(messageId);
				if (map.size() > 0) {
					sureDataCache.put(clientId, map);
				} else {
					sureDataCache.remove(clientId);
				}
			}
		}
	}

	 @Override
	public void removeByClient(String clientId) {
		if (sureDataCache.containsKey(clientId)) {
			sureDataCache.remove(clientId);
		}
	}
}
