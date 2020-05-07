package net.fxft.ascsgatewaymqckbserver.mqttclient.client.mqtt.common;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public enum MessageStatus {
	/**
	 * none
	 */
	None,
	/**
	 * Qos1
	 */
    PUB,
    /**
     * Qos2
     */
    PUBREC,
    /**
     * Qos2
     */
    PUBREL,
    /**
     * finish
     */
    COMPLETE,
}
