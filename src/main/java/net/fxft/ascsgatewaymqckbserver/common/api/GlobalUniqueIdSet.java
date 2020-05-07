package net.fxft.ascsgatewaymqckbserver.common.api;


import net.fxft.ascsgatewaymqckbserver.common.core.CacheList;
import net.fxft.ascsgatewaymqckbserver.common.core.UniqueIdInteger;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/
public interface GlobalUniqueIdSet {
	/**
	 * Custom set GlobalUniqueId object
	 * @param globalUniquedId
	 */
	public void setGlobalUniqueId(GlobalUniqueId globalUniquedId);
	
	/**
	 * 
	 * @param cacheList
	 */
	public void setGlobalUniqueIdCache(CacheList<UniqueIdInteger> cacheList);
}
