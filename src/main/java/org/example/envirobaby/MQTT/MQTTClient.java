package org.example.envirobaby.MQTT;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MQTTClient {

    private static final String BROKER_URL = "tcp://broker.hivemq.com:1883";
    private MqttClient client;

    public MQTTClient(String CLIENT_ID, MqttCallback callback) throws MqttException {
        this.client = new MqttClient(BROKER_URL, CLIENT_ID);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);

        client.setCallback(callback);

        client.connect(options);
    }

    public void subscribe(String topic) throws MqttException{
        client.subscribe(topic);
    }
}
