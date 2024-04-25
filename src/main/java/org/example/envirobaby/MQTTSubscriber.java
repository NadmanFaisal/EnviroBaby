package org.example.envirobaby;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.application.Platform;
import javafx.util.Duration;
import org.eclipse.paho.client.mqttv3.*;
import org.controlsfx.control.Notifications;
import java.io.IOException;


// Implements MqttCallback interface to use callback methods which are implemented below as methods
public class MQTTSubscriber implements MqttCallback {
    private static final String BROKER_URL = "tcp://broker.hivemq.com:1883";
    private static final String CLIENT_ID = "JavaSubscriber";
    private static final String LOUD_TOPIC = "envirobaby/loud";
    private static final int NOISE_THRESHOLD = 70;

    private MqttClient client; // connects to the broker and manages subscription in the constructor
    private Label noiseLabel;
    private String noiseValue; // stores last published message


    public MQTTSubscriber(Label noiseLabel) {
        this.noiseLabel = noiseLabel;
        try {
            client = new MqttClient(BROKER_URL, CLIENT_ID); //Create mqtt client
            client.setCallback(this);
            client.connect();
            client.subscribe(LOUD_TOPIC);
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
    public void messageArrived(String topic, MqttMessage message) throws IOException {

        // Loudness messages stored in variable
        switch (topic) {
            case LOUD_TOPIC -> {
                noiseValue = new String(message.getPayload());
                updateLabel(noiseLabel,noiseValue);

                if(extractNumber(noiseValue) > NOISE_THRESHOLD) {
                    createNotification();
                }
                break;
            }
        }
    }

// This method updates respective labels according to the message it receives in the arguments.
    private void updateLabel(Label label, String message) {
        if (label != null && message != null) {
            Platform.runLater(() -> { //Run the label update operation on JavaFX Application Thread
                noiseLabel.setText(noiseValue); //Set label text to the last received message
            });
        }
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        // this method is not implemented yet
    }

    public int extractNumber(String message) {
        StringBuilder number = new StringBuilder();
        boolean found = false;

        for (char letter : message.toCharArray()) {
            if (Character.isDigit(letter)) {
                number.append(letter);
                found = true;
            } else if (found) {
                // Break the loop if we have already found some digits and then encounter a non-digit
                break;
            }
        }

        if (!number.isEmpty()) {

            // Converts a string to an int if it is compatible to do so
            return Integer.parseInt(number.toString());
        } else {
            throw new NumberFormatException("No number found in the text");
        }
    }

    public void createNotification() throws IOException {

        Platform.runLater(() -> {
            Notifications notifications = Notifications.create().title("Notification").text("Notification").graphic(null).hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
            notifications.showConfirm();
        });
    }
}
