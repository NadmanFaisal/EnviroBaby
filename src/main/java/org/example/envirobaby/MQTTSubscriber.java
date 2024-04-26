package org.example.envirobaby;

import javafx.scene.control.Label;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import org.eclipse.paho.client.mqttv3.*;
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
    private TextField upperBoundNoise; // reference to textField object
    private int threshold; // stores last received noise level value

    private Notification notification; // instance variable from Notification class


    public MQTTSubscriber(Label noiseLabel, TextField upperBoundNoise) {
        this.noiseLabel = noiseLabel;
        this.upperBoundNoise = upperBoundNoise;
        this.threshold = 90;
        this.notification = new Notification();
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
                    notification.createNotification("Noise notification", "NOISE THRESHOLD CROSSED: " + extractNumber(noiseValue) + " db");
                }
                break;
            }
        }
    }

// This method updates respective labels according to the message it receives in the arguments.
    private void updateLabel(Label label, String message) {
        if (label != null && message != null) {
            Platform.runLater(() -> { //Runs the label update operation on JavaFX Application Thread
                noiseLabel.setText(noiseValue); //Sets label text to the last received message
            });
        }
    }

    public void updateNoiseThreshold(){ // method that updates the threshold value
        String thresholdTextValue = upperBoundNoise.getText(); // gets and stores the string value from textField
        if (thresholdTextValue.matches("\\d+")){ //condition to find if there are any numeric value
            this.threshold = Integer.parseInt(thresholdTextValue); // converts the string into integer
        } else {
            System.out.println("Enter a numeric value, Thank you!"); // if no numeric value id found this is printed
        }
    }
    public int getThreshold() { // getter method to receive threshold value
        return threshold;
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        // this method is not implemented yet
    }

    public int extractNumber(String message) {
        StringBuilder number = new StringBuilder();
        boolean found = false;

        // Loops and checks if a character is a digit
        for (char letter : message.toCharArray()) {
            if (Character.isDigit(letter)) {
                number.append(letter);
                found = true;
            } else if (found) {
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
}
