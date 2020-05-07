package net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.api.impl;


import net.fxft.ascsgatewaymqckbserver.common.core.CacheList;
import net.fxft.ascsgatewaymqckbserver.common.core.CacheListLocalMemory;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.api.ProcedureDataService;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.common.ProcedureMessage;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.protocol.data.ProcedureClientData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class ProcedureDataServiceImpl implements ProcedureDataService {
	private CacheList<ProcedureClientData> procedureSureCache;
	public ProcedureDataServiceImpl() {
		procedureSureCache = new CacheListLocalMemory<ProcedureClientData>();
	}
	
	@Override
	public void setProcedureCacheList(CacheList<ProcedureClientData> cacheList) {
		if (cacheList != null) {
			procedureSureCache = cacheList;
		}	
	}

	@Override
	public void removeByClient(String clientId) {
		if (procedureSureCache.containsKey(clientId)) {
			procedureSureCache.remove(clientId);
		}
	}

	@Override
	public List<ProcedureMessage> getPubRelMessageForClient(String clientId) {
		if (procedureSureCache.containsKey(clientId)) {
			ProcedureClientData map = procedureSureCache.get(clientId);
			Collection<ProcedureMessage> collection = map.values();
			return new ArrayList<ProcedureMessage>(collection);
		}
		return new ArrayList<ProcedureMessage>();
	}

	@Override
	public ProcedureMessage getPubRelMessage(String clientId, int packId) {
		if (procedureSureCache.containsKey(clientId)) {
			ProcedureClientData map = procedureSureCache.get(clientId);
			if (map.containsKey(packId)) {
				return map.get(packId);
			}
		}
		return null;
	}

	@Override
	public ProcedureMessage removePubRelMessage(String clientId, int packId) {
		ProcedureMessage info = null;
		if (procedureSureCache.containsKey(clientId)) {
			ProcedureClientData map = procedureSureCache.get(clientId);
			if (map.containsKey(packId)) {
				info = map.get(packId);
				map.remove(packId);
				if (map.size() > 0) {
					procedureSureCache.put(clientId, map);
				} else {
					procedureSureCache.remove(clientId);
				}
			}
		}
		return info;
	}

	@Override
	public void putPubRelMessage(String clientId, ProcedureMessage info) {
		ProcedureClientData map = procedureSureCache.containsKey(clientId) ? procedureSureCache.get(clientId)
				: null;
		if (map == null) {
			map = new ProcedureClientData(clientId);
		}
		map.put(info.getSourceMsgId(), info);
		procedureSureCache.put(clientId, map);
	}
}
