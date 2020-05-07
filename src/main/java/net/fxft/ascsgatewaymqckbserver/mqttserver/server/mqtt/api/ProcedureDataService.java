package net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.api;


import net.fxft.ascsgatewaymqckbserver.common.core.CacheList;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.common.ProcedureMessage;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.protocol.data.ProcedureClientData;

import java.util.List;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public interface ProcedureDataService {
	/**
	 * custom set procedure cache
	 * @param cacheList
	 */
	public void setProcedureCacheList(CacheList<ProcedureClientData> cacheList);
	
	/**
	 * removeByClient
	 * @param clientId
	 */
	public void removeByClient(String clientId);
	
	/**
	 * getPubRelMessageForClient
	 * @param clientId
	 * @return
	 */
	public List<ProcedureMessage> getPubRelMessageForClient(String clientId);
	
	/**
	 * getPubRelMessage
	 * @param clientId
	 * @param packId
	 * @return
	 */
	public ProcedureMessage getPubRelMessage(String clientId, int packId);
	
	/**
	 * removePubRelMessage
	 * @param clientId
	 * @param packId
	 * @return
	 */
	public ProcedureMessage removePubRelMessage(String clientId, int packId);
	
	/**
	 * putPubRelMessage
	 * @param clientId
	 * @param info
	 */
	public void putPubRelMessage(String clientId, ProcedureMessage info);
}
