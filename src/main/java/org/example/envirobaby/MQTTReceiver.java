package org.example.envirobaby;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTReceiver {
    private static final String LOUD_TOPIC = "envirobaby/loud";
    private static final String TEMP_TOPIC = "envirobaby/temp";
    private static final String HUM_TOPIC = "envirobaby/humi";

    private MQTTClient client;
    private ParameterData readings;


    public MQTTReceiver(String userId) throws MqttException {
        this.client = connectToMQTTClient(userId);
        this.readings = new ParameterData();
        this.client.subscribe(LOUD_TOPIC);
        this.client.subscribe(TEMP_TOPIC);
        this.client.subscribe(HUM_TOPIC);
    }

    private MQTTClient connectToMQTTClient(String userId) throws MqttException {
        MqttCallback callback = new MqttCallback() {
            public void connectionLost(Throwable cause) {
                System.out.println("Connection lost: " + cause); // Handles the loss of MQTT connection, logging the cause of the disconnection.
            }

            public void messageArrived(String topic, MqttMessage message){
                switch (topic) {
                    case LOUD_TOPIC -> {
                        readings.setLoudValue(Integer.parseInt(new String(message.getPayload()))); //send received messages to readings object
                        break;
                    }
                    case TEMP_TOPIC -> {
                        readings.setTempValue(Double.parseDouble(new String(message.getPayload())));
                        break;
                    }
                    case HUM_TOPIC -> {
                        readings.setHumValue(Double.parseDouble(new String(message.getPayload())));
                        break;
                    }
                }
            }


            public void deliveryComplete(IMqttDeliveryToken token) {
                // this method is not needed
            }
        };
        return new MQTTClient(userId, callback);
    }

    public ParameterData getReadings() {
        return readings;
    }

}
