package net.fxft.ascsgatewaymqckbserver.common.api;


import net.fxft.ascsgatewaymqckbserver.common.core.CacheList;
import net.fxft.ascsgatewaymqckbserver.common.core.UniqueIdInteger;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public interface GlobalUniqueId {
	
	void setCacheList(CacheList<UniqueIdInteger> cacheList);
	/**
	 * get uniuqe id
	 * @return
	 */
	int getNextMessageId(String clientId);
}
