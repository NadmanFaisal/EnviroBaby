package org.example.envirobaby;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.eclipse.paho.client.mqttv3.*;

// This class implements the MqttCallback interface to handle MQTT communications,
// specifically subscribing to topics and updating UI components with received messages.
// Implements MqttCallback interface to use callback methods which are implemented below as methods
public class MQTTSubscriber implements MqttCallback {
    private static final String BROKER_URL = "tcp://broker.hivemq.com:1883";
    private static final String CLIENT_ID = "JavaSubscriber";
    private static final String LOUD_TOPIC = "envirobaby/loud";
    private static final String TEMP_TOPIC = "envirobaby/temp";
    private static final String HUM_TOPIC = "envirobaby/humi";

    private MqttClient client; // connects to the broker and manages subscription in the constructor

    // Messages received from the MQTT broker
    private String noiseValue; // stores last published message
    private String tempValue; // Holds the latest temperature data received via MQTT
    private String humValue; // Holds the latest humidity data received via MQTT

    // Components for the composite class (MQQTTSubscriber class)
    private Notification notification;
    private Parameters parameters;
    private Thresholds thresholds;


    // Constructor sets up labels and MQTT connection, subscribes to topics for loudness and temperature.
    public MQTTSubscriber(Label noiseLabel, Label tempLabel, Label humLabel, TextField maxNoise, TextField maxTempBox, TextField minTempBox, TextField minHumBox, TextField maxHumBox) {
        this.parameters = new Parameters(noiseLabel, tempLabel, humLabel);
        this.notification = new Notification();
        this.thresholds = new Thresholds(maxNoise, maxTempBox, minTempBox, maxHumBox, minHumBox);

        try {
            client = new MqttClient(BROKER_URL, CLIENT_ID); //Create mqtt client
            client.setCallback(this);
            client.connect();
            client.subscribe(LOUD_TOPIC);
            client.subscribe(TEMP_TOPIC);
            client.subscribe(HUM_TOPIC);
        } catch (MqttException e) {
            //Print error messages
            e.printStackTrace(); //Error handling for MQTT connection issues
        }
    }


    // Below are the callback methods which are defined in the interface and implemented in this class


    // Messages from Mqtt are stored in the variables present in the if block
    public void messageArrived(String topic, MqttMessage message) {

        // Loudness messages stored in variable
        switch (topic) {
            case LOUD_TOPIC -> {
                noiseValue = new String(message.getPayload());
                Label noiseLabel = parameters.getNoiseLabel();
                parameters.updateLabel(noiseLabel, noiseValue);
                thresholds.updateNoiseThreshold(thresholds.getMaxNoise().getText());
                notification.createNoiseNotification(noiseValue, thresholds.getNoiseThreshold());
            }

            case TEMP_TOPIC -> {
                tempValue = new String(message.getPayload());
                Label tempLabel = parameters.getTempLabel();
                parameters.updateLabel(tempLabel, tempValue);
                thresholds.updateTempUbound(thresholds.getMaxTempBox().getText());
                thresholds.updateTempLbound(thresholds.getMinTempBox().getText());
                notification.createTempNotification(tempValue, thresholds.getTempUbound(), thresholds.getTempLbound());
            }

            case HUM_TOPIC -> {
                humValue = new String(message.getPayload());
                Label humLabel = parameters.getHumLabel();
                parameters.updateLabel(humLabel, humValue);
                thresholds.updateMaxHum(thresholds.getMaxHumBox().getText());
                thresholds.updateMinHum(thresholds.getMinHumBox().getText());
                notification.createHumNotification(humValue, thresholds.getMaxHum(), thresholds.getMinHum());
            }
        }

    }

    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost: " + cause); // Handles the loss of MQTT connection, logging the cause of the disconnection.
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        // this method is not implemented yet
    }

}