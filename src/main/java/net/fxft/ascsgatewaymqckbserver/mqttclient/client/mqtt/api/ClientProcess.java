package net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.api;


import io.netty.channel.Channel;
import net.fxft.ascsgatewaymqckbserver.common.NettyUtil;
import net.fxft.ascsgatewaymqckbserver.common.api.GlobalUniqueId;
import net.fxft.ascsgatewaymqckbserver.common.api.GlobalUniqueIdImpl;
import net.fxft.ascsgatewaymqckbserver.common.api.GlobalUniqueIdSet;
import net.fxft.ascsgatewaymqckbserver.common.core.CacheList;
import net.fxft.ascsgatewaymqckbserver.common.core.UniqueIdInteger;
import net.fxft.ascsgatewaymqckbserver.mqttclient.client.core.BaseClient;

import java.nio.charset.Charset;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class ClientProcess implements GlobalUniqueIdSet {
	private BaseClient client;
	private GlobalUniqueId globalUniqueId;

	public ClientProcess(BaseClient client) {
		globalUniqueId = new GlobalUniqueIdImpl();
		this.client = client;
	}
	
	@Override
	public void setGlobalUniqueId(GlobalUniqueId globalUniqueId) {
		if (globalUniqueId != null) {
			this.globalUniqueId = globalUniqueId;
		}
	}
	@Override
	public void setGlobalUniqueIdCache(CacheList<UniqueIdInteger> cacheList) {
		globalUniqueId.setCacheList(cacheList);
	}
	
	protected String getClientId() {
		return NettyUtil.getClientId(client.getChannel());
	}

	public Channel channel() {
		return client.getChannel();
	}

	public GlobalUniqueId messageId() {
		return globalUniqueId;
	}

	public byte[] encoded(String data) {
		if (data == null) {
			return null;
		}
		return data.getBytes(Charset.forName(this.client.getCharsetName()));
	}

	public String decode(byte[] data) {
		return new String(data, Charset.forName(this.client.getCharsetName()));
	}
}
