
package net.fxft.ascsgatewaymqckbserver.common.core;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ben
 * @Title: UniqueIdInteger.java
 * @Description:
 **/

@Data
public class UniqueIdInteger implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int value = 1;

	public int addInc() {
		value = value + 1;
		if (value > 65535) {
			value = 1;
		}
		return value;
	}
	
	public int id() {
		return value;
	}
}
