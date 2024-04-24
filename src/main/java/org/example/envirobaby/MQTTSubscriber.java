package org.example.envirobaby;

import javafx.scene.control.Label;
import javafx.application.Platform;
import org.eclipse.paho.client.mqttv3.*;

// This class implements the MqttCallback interface to handle MQTT communications,
// specifically subscribing to topics and updating UI components with received messages.
// Implements MqttCallback interface to use callback methods which are implemented below as methods
public class MQTTSubscriber implements MqttCallback {
    private static final String BROKER_URL = "tcp://broker.hivemq.com:1883";
    private static final String CLIENT_ID = "JavaSubscriber";
    private static final String LOUD_TOPIC = "envirobaby/loud";
    private static final String TEMP_TOPIC = "envirobaby/temp";


    private MqttClient client; // connects to the broker and manages subscription in the constructor
    private Label noiseLabel;
    private Label tempLabel;
    private String noiseValue; // stores last published message
    private String tempValue; // Holds the latest temperature data received via MQTT



    // Constructor sets up labels and MQTT connection, subscribes to topics for loudness and temperature.
    public MQTTSubscriber(Label noiseLabel, Label tempLabel) {
        this.noiseLabel = noiseLabel;
        this.tempLabel = tempLabel;
        try {
            client = new MqttClient(BROKER_URL, CLIENT_ID); //Create mqtt client
            client.setCallback(this);
            client.connect();
            client.subscribe(LOUD_TOPIC);
            client.subscribe(TEMP_TOPIC);
        } catch (MqttException e) {
            //Print error messages
            e.printStackTrace(); //Error handling for MQTT connection issues
        }
    }


    // Below are the callback methods which are defined in the interface and implemented in this class

    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost: " + cause); // Handles the loss of MQTT connection, logging the cause of the disconnection.
    }

    // Messages from Mqtt are stored in the variables present in the if block
    public void messageArrived(String topic, MqttMessage message) throws InterruptedException {

        // Loudness messages stored in variable
        switch (topic) {
            case LOUD_TOPIC -> {
                noiseValue = new String(message.getPayload());
                updateLabel(noiseLabel,noiseValue);
                break;
            }

            case TEMP_TOPIC -> {
                tempValue = new String(message.getPayload());
                updateLabel(tempLabel,tempValue);
                break;
            }
        }

    }

// This method updates respective labels according to the message it receives in the arguments.
    private void updateLabel(Label label, String message) {
        if (label != null && message != null) {
            Platform.runLater(() -> { //Run the label update operation on JavaFX Application Thread
                label.setText(message); //Set label text to the last received message
            });
        }
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        // this method is not implemented yet
    }
}
