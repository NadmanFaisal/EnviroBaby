package org.example.envirobaby;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Random;

public class MQTTSender {

    private static final String BROKER_URL = "tcp://broker.hivemq.com:1883";
    private static final String CLIENT_ID = "JavaSubscriber";


    // Define the topic to publish the message (replace if needed)

    private MqttClient client;

    public MQTTSender() throws MqttException {
        this.client = new MqttClient(BROKER_URL, CLIENT_ID + getRandomString());
        this.client.connect();
    }

    public void sendMessage(String message, String topic) throws MqttException, InterruptedException {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        mqttMessage.setQos(1); // QoS 1 for at least once delivery

        client.publish(topic, mqttMessage);
        System.out.println("Sent message" + "'" + message + "'" + "to topic: " + topic);
        Thread.sleep(1000);
    }

    public String getRandomString() {
        String alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * alphabets.length());
            salt.append(alphabets.charAt(index));
        }
        String randomString = salt.toString();
        return randomString;

    }
}
