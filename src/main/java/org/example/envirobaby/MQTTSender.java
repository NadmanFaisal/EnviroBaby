package org.example.envirobaby;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTSender {

    private static final String BROKER_URL = "tcp://broker.hivemq.com:1883";
    private static final String CLIENT_ID = "JavaTempUnit";

    // Define the topic to publish the message (replace if needed)

    private MqttClient client;

    public MQTTSender() throws MqttException {
        this.client = new MqttClient(BROKER_URL, CLIENT_ID);
        this.client.connect();
    }

    public void sendMessage(String message, String topic) throws MqttException, InterruptedException {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        mqttMessage.setQos(1); // QoS 1 for at least once delivery

        client.publish(topic, mqttMessage);
        System.out.println("Sent message" + "'" + message + "'" + "to topic: " + topic);
        Thread.sleep(1000);
    }
}
