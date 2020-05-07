package net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.protocol.process;


import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttQoS;
import net.fxft.ascsgatewaymqckbserver.common.NettyLog;
import net.fxft.ascsgatewaymqckbserver.common.NettyUtil;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.api.ProcedureDataService;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.common.BorkerMessage;
import net.fxft.ascsgatewaymqckbserver.mqttserver.server.mqtt.common.ProcedureMessage;

import java.util.List;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class ProcedureProcess {
	private final SendMessageProcess sendProcess;
	private ProcedureDataService procedureData;

	public ProcedureProcess(SendMessageProcess sendProcess, ProcedureDataService procedureData) {
		this.sendProcess = sendProcess;
		this.procedureData = procedureData;
	}

	private boolean getMustStoreRelMessage(MqttQoS qosLevel) {
		boolean bSaveMsg = false;
		if (qosLevel == MqttQoS.AT_MOST_ONCE) {
			bSaveMsg = false;
		} else if (qosLevel == MqttQoS.AT_LEAST_ONCE) {
			bSaveMsg = false;
		} else if (qosLevel == MqttQoS.EXACTLY_ONCE) {
			bSaveMsg = true;
		}
		return bSaveMsg;
	}



	public void removeByCleanSession(String clientId) {
		procedureData.removeByClient(clientId);
	}

	public ProcedureMessage processPubRel(Channel channel, int messageId) {
		String clientId = NettyUtil.getClientId(channel);
		NettyLog.debug("PUBREL - clientId: {}, messageId: {}", clientId, messageId);
		//
		ProcedureMessage relInfo = procedureData.removePubRelMessage(clientId, messageId);
		return relInfo;
	}

	public void processHistoryPubRel(Channel channel) {
		String clientId = NettyUtil.getClientId(channel);

		List<ProcedureMessage> dupPubRelMessageStoreList = procedureData.getPubRelMessageForClient(clientId);
		dupPubRelMessageStoreList.forEach(relInfo -> {
			this.sendProcess.sendPubRecMessage(channel, relInfo.getSourceMsgId());
		});
	}

	public void processPublish(Channel channel, BorkerMessage bMsgInfo) {
		MqttQoS qosLevel = MqttQoS.valueOf(bMsgInfo.getIQosLevel());
		String clientId = NettyUtil.getClientId(channel);
		int packetId = bMsgInfo.getSourceMsgId();
		String topicName = bMsgInfo.getTopicName();
		byte[] msgBytes = bMsgInfo.getMsgBytes();

		boolean bSaveRelMsg = getMustStoreRelMessage(qosLevel);
		if (bSaveRelMsg) {
			ProcedureMessage relMsgObj = ProcedureMessage.builder().sourceClientId(clientId)
					.sourceMsgId(packetId).topicName(topicName).iQosLevel(qosLevel.value()).msgBytes(msgBytes)
					//.borkerMsgId(bMsgInfo.getBorkerMsgId())
					.build();
			procedureData.putPubRelMessage(clientId, relMsgObj);
		}
	}
}
