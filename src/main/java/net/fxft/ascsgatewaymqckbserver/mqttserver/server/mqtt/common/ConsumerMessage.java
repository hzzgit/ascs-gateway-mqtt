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

public class ConsumerMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	private String sourceClientId;
	private String topic;
	private int mqttQoS;
	private byte[] messageBytes;
	private int messageId;
	private boolean retain;
	private boolean dup;
}
