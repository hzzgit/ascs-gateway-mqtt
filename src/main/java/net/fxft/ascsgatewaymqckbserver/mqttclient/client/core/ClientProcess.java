package net.fxft.ascsgatewaymqckbserver.mqttclient.client.core;


import net.fxft.ascsgatewaymqckbserver.common.exception.LoginException;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public interface ClientProcess {
	/**
	 * 登录完成
	 * @param bResult
	 * @param exception
	 */
	void loginFinish(boolean bResult, LoginException exception);
	
	/**
	 * 发送指令关闭
	 */
	void disConnect();
}
