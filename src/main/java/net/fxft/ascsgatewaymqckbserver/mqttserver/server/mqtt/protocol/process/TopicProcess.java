package net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.protocol.process;


import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttTopicSubscription;
import lombok.extern.slf4j.Slf4j;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.api.TopicService;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.common.BorkerMessage;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.common.RetainMessage;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.common.SubscribeTopicInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/
@Slf4j
public class TopicProcess {
	private TopicService topicService;
	
	public TopicProcess(TopicService topicService) {
		this.topicService = topicService;
	}

	/**
	 * 发布信息如带有保留
	 * @param bMsgInfo
	 */
	public void publicRetainMessage(BorkerMessage bMsgInfo) {
		byte[] msgBytes = bMsgInfo.getMsgBytes();
		String topicName = bMsgInfo.getTopicName();
		
		if (msgBytes.length == 0) {
			log.debug("save retain remove: {}", topicName);
			topicService.removeRetainMessage(topicName);
		} else {
			log.debug("save retain: {}", topicName);
			RetainMessage retainMessageStore = RetainMessage.builder().sourceClientId(bMsgInfo.getSourceClientId())
					.sourceMsgId(bMsgInfo.getSourceMsgId()).topicName(topicName).iQosLevel(bMsgInfo.getIQosLevel()).msgBytes(msgBytes)
					//.borkerMsgId(bMsgInfo.getBorkerMsgId())
					.build();
			topicService.putRetainMessage(topicName, retainMessageStore);
		}
	}
	
	/**
	 * 删除用户订阅主题
	 */
	public void removeClientTopic(String clientId, List<String> topicFilters) {
		topicFilters.forEach(topicFilter -> {
			topicService.remove(topicFilter, clientId);
			log.debug("UNSUBSCRIBE - clientId: {}, topicFilter: {}", clientId, topicFilter);
		});
	}
	
	/**
	 * 用户登录清除
	 */
	public void removeByCleanSession(String clientId) {
		topicService.removeForClient(clientId);
	}
	
	private boolean validTopicFilter(List<MqttTopicSubscription> topicSubscriptions) {
		return true;
	}
	
	public List<Integer> processTopicSubscribe(String clientId, List<MqttTopicSubscription> topicSubscriptions ) {
		if (this.validTopicFilter(topicSubscriptions)) {
			List<Integer> mqttQoSList = new ArrayList<Integer>();
			topicSubscriptions.forEach(topicSubscription -> {
				String topicFilter = topicSubscription.topicName();
				MqttQoS mqttQoS = topicSubscription.qualityOfService();
				SubscribeTopicInfo subscribeStore = new SubscribeTopicInfo(clientId, topicFilter, mqttQoS.value());
				topicService.put(topicFilter, subscribeStore);
				mqttQoSList.add(mqttQoS.value());
				log.debug("SUBSCRIBE - clientId: {}, topFilter: {}, QoS: {}", clientId, topicFilter,
						mqttQoS.value());
			});
			return mqttQoSList;
			
		} else {
			log.error("error Subscribe");
			return null;
		}
	}
	
	public List<RetainMessage> searchRetainMessage(String topicFilter) {
		return topicService.searchRetainMessage(topicFilter);
	}
	
	public List<SubscribeTopicInfo> search(String topicFilter) {
		return topicService.search(topicFilter);
	}
	
	public boolean checkVaildTopic(String topicName) {
		boolean flag = false;
		
		String oneLevelFlagStr = "#";
		String allLevelFlagStr = "*";
		if ((topicName != null) && (topicName.trim().length() > 0)) {
			if (topicName.contains(oneLevelFlagStr) || topicName.contains(allLevelFlagStr)) {
				flag = false;
			} else {
				flag = true;
			}
		}
		return flag;
	}
	
}
