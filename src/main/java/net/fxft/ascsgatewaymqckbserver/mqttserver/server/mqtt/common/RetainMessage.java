package net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.common;

import lombok.*;

import java.io.Serializable;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class RetainMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String sourceClientId;
	private int sourceMsgId;

	private String topicName;
	private int iQosLevel;
	private byte[] msgBytes;
	
	//@Builder.Default
	//private int borkerMsgId = -1;
	
	@Builder.Default
	private long timestamp = System.currentTimeMillis();
}
