package net.fxft.ascsgatewaymqckbserver.common;

import io.netty.util.AttributeKey;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class NettyConstant {
	public static final AttributeKey<String> CLIENTID_KEY = AttributeKey.valueOf("cha.clientId");
	public static final String HANDLER_NAME_HEARTCHECK = "idle";
	public static final String HANDLER_NAME_SSL = "ssl";
}
