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

public class SubscribeTopicInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String clientId;
	private String topicFilter;
	private int mqttQoS;
}
