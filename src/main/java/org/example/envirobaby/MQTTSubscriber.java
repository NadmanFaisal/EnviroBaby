package org.example.envirobaby;

import javafx.scene.control.Label;
import javafx.application.Platform;
import org.eclipse.paho.client.mqttv3.*;


// Implements MqttCallback interface to use callback methods which are implemented below as methods
public class MQTTSubscriber implements MqttCallback {
    private static final String BROKER_URL = "tcp://broker.hivemq.com:1883";
    private static final String CLIENT_ID = "JavaSubscriber";
    private static final String MQTT_TOPIC = "envirobaby/loud";

    private MqttClient client; // connects to the broker and manages subscription in the constructor
    private Label noiseLabel;
    private String lastReceivedMessage; // stores last published message


    public MQTTSubscriber(Label noiseLabel) {
        this.noiseLabel = noiseLabel;
        try {
            client = new MqttClient(BROKER_URL, CLIENT_ID); //Create mqtt client
            client.setCallback(this);
            client.connect();
            client.subscribe(MQTT_TOPIC);
        } catch (MqttException e) {
            //Print error messages
            e.printStackTrace();
        }
    }

    // Below are the callback methods which are defined in the interface and implemented in this class

    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost: " + cause);
    }

    // Messages from Mqtt are stored in the variables present in the if block
    public void messageArrived(String topic, MqttMessage message) throws InterruptedException {

        // Loudness messages stored in variable
        if (MQTT_TOPIC.equals(topic)) {
            lastReceivedMessage = new String(message.getPayload());
            updateLabel();
        }
    }

//Called whenever a message is received
    private void updateLabel() {
        if (noiseLabel != null && lastReceivedMessage != null) {
            Platform.runLater(() -> { //Run the label update operation on JavaFX Application Thread
                noiseLabel.setText(lastReceivedMessage); //Set label text to the last received message
            });
        }
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        // this method is not implemented yet
    }
}
