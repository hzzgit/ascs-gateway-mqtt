package net.fxft.ascsgatewaymqckbserver.common.exception;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class LoginException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public LoginException(String message) {
		super(message);
	}
}
