package net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.protocol.data;


import lombok.Getter;
import lombok.Setter;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.common.RetainMessage;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.common.SubscribeTopicInfo;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class TopicData extends BaseDataInMap<String, SubscribeTopicInfo>{
	private static final long serialVersionUID = 1L;
	
	@Getter
    @Setter
	public RetainMessage retainMessageInfo;
	
	public TopicData() {
		
	}
	public TopicData(String name) {
		super(name);
	}
}
