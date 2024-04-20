package org.example.envirobaby;

import org.eclipse.paho.client.mqttv3.*;


// Implements MqttCallback interface to use callback methods which are implemented below as methods
public class MQTTSubscriber implements MqttCallback {
    private static final String BROKER_URL = "tcp://broker.hivemq.com:1883";
    private static final String CLIENT_ID = "JavaSubscriber";
    private static final String MQTT_TOPIC = "envirobaby";

    private MqttClient client; // connects to the broker and manages subscription
    private String lastReceivedMessage; // stores last published message

    public MQTTSubscriber() {

        // if any error occurs, they are caught in the try catch block and printed
        try {
            client = new MqttClient(BROKER_URL, CLIENT_ID);
            client.setCallback(this);
            client.connect();
            client.subscribe(MQTT_TOPIC);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // Below are the callback methods which are defined in the interface and implemented in this class


    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost: " + cause);
    }

    // Messages from Mqtt are stored in the variables present in the if block
    public void messageArrived(String topic, MqttMessage message) {

        // Loudness messages stored in variable
        if (MQTT_TOPIC.equals(topic)) {
            lastReceivedMessage = new String(message.getPayload());
        }
    }

    public String getLastReceivedMessage() {
        return lastReceivedMessage;
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        // this method is not implemented yet
    }
}
