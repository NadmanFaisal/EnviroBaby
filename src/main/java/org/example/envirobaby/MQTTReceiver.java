package org.example.envirobaby;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Random;

public class MQTTReceiver {
    private static final String LOUD_TOPIC = "/envirobaby/room1/loud";
    private static final String TEMP_TOPIC = "/envirobaby/room1/temp";
    private static final String HUM_TOPIC = "/envirobaby/room1/humi";

    private static final String LOUD_TOPIC_2 = "/envirobaby/room2/loud";
    private static final String TEMP_TOPIC_2 = "/envirobaby/room2/temp";
    private static final String HUM_TOPIC_2 = "/envirobaby/room2/humi";

    private static final String LOUD_TOPIC_3 = "/envirobaby/room3/loud";
    private static final String TEMP_TOPIC_3 = "/envirobaby/room3/temp";
    private static final String HUM_TOPIC_3 = "/envirobaby/room3/humi";


    private MQTTClient client;
    private ParameterData readings;

    private ParameterData readings2;


    public MQTTReceiver(String userId, String loud, String temp, String humi) throws MqttException {
        this.client = connectToMQTTClient(userId + getRandomString());
        this.readings = new ParameterData();
        this.client.subscribe(loud);
        this.client.subscribe(temp);
        this.client.subscribe(humi);
    }

    private MQTTClient connectToMQTTClient(String userId) throws MqttException {
        MqttCallback callback = new MqttCallback() {
            public void connectionLost(Throwable cause) {
                System.out.println("Connection lost: " + cause); // Handles the loss of MQTT connection, logging the cause of the disconnection.
            }

            public void messageArrived(String topic, MqttMessage message){
                    switch (topic) {
                        case LOUD_TOPIC, LOUD_TOPIC_2, LOUD_TOPIC_3 -> {
                            readings.setLoudValue(Integer.parseInt(new String(message.getPayload()))); //send received messages to readings object
                            break;
                        }
                        case TEMP_TOPIC, TEMP_TOPIC_2, TEMP_TOPIC_3 -> {
                            readings.setTempValue(Double.parseDouble(new String(message.getPayload())));
                            break;
                        }
                        case HUM_TOPIC, HUM_TOPIC_2, HUM_TOPIC_3 -> {
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

    /** Generates a random string of characters
     *
     * @return random string
     */
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
