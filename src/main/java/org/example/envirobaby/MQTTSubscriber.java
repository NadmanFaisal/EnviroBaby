package org.example.envirobaby;

import javafx.scene.control.Label;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import org.eclipse.paho.client.mqttv3.*;
import java.io.IOException;

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
    private Label noiseLabel;
    private Label tempLabel;
    private Label humLabel;
    private String noiseValue; // stores last published message
    private TextField maxNoise; // reference to textField object
    private int noiseThreshold; // stores last received noise level value
    private String tempValue; // Holds the latest temperature data received via MQTT
    private String humValue; // Holds the latest humidity data received via MQTT

    private double minHum; // stores latest input for minimum humidity threshold
    private double maxHum; // stores latest input for maximum humidity threshold
    private TextField minHumBox; //reference to the TextField object
    private TextField maxHumBox; //reference to the TextField object

    private Notification notification; // instance variable from Notification class


    // Constructor sets up labels and MQTT connection, subscribes to topics for loudness and temperature.
    public MQTTSubscriber(Label noiseLabel, Label tempLabel, Label humLabel, TextField maxNoise, TextField minHumBox, TextField maxHumBox ) {
        this.noiseLabel = noiseLabel;
        this.tempLabel = tempLabel;
        this.humLabel = humLabel;
        this.minHumBox = minHumBox;
        this.maxHumBox = maxHumBox;
        this.minHum = 30;
        this.maxHum = 60;
            this.maxNoise = maxNoise;
            this.noiseThreshold = 90;
        this.notification = new Notification();

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

    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost: " + cause); // Handles the loss of MQTT connection, logging the cause of the disconnection.
    }

    // Messages from Mqtt are stored in the variables present in the if block
    public void messageArrived(String topic, MqttMessage message) throws IOException {

        // Loudness messages stored in variable
        switch (topic) {
            case LOUD_TOPIC -> {
                noiseValue = new String(message.getPayload());
                updateLabel(noiseLabel,noiseValue);

                if(extractNumber(noiseValue) > noiseThreshold) {
                    notification.createNotification("Noise notification", "NOISE THRESHOLD CROSSED: " + extractNumber(noiseValue) + " db");
                }
                break;
            }

            case TEMP_TOPIC -> {
                tempValue = new String(message.getPayload());
                updateLabel(tempLabel,tempValue);
                break;
            }
            case HUM_TOPIC -> {
                humValue = new String(message.getPayload());
                updateLabel(humLabel,humValue);
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

    public void updateNoiseThreshold(){ // method that updates the threshold value
        String thresholdTextValue = maxNoise.getText(); // gets and stores the string value from textField
        if (thresholdTextValue.matches("\\d+")){ //condition to find if there are any numeric value
            this.noiseThreshold = Integer.parseInt(thresholdTextValue); // converts the string into integer
        } else {
            System.out.println("Enter a numeric value, Thank you!"); // if no numeric value id found this is printed
        }
    }

    public void updateMinHum() { // Updates minimum humidity threshold
        String minHumTextValue = minHumBox.getText();
        if (minHumTextValue.matches("[0-9]{1,13}(.[0-9]*)?")) {
            this.minHum = Double.parseDouble(minHumTextValue);
        } else {
            System.out.println("Please enter a numeric value");
        }
    }

    public void updateMaxHum() { // Updates maximum humidity threshold
        String maxHumTextValue = maxHumBox.getText();
        if (maxHumTextValue.matches("[0-9]{1,13}(.[0-9]*)?")) {
            this.maxHum = Double.parseDouble(maxHumTextValue);
        } else {
            System.out.println("Please enter a numeric value");
        }
    }

    public double getMinHum(){ //getter for minimum humidity threshold
        return minHum;
    }

    public double getMaxHum(){ //getter for maximum humidity treshold
        return maxHum;
    }

    public int getNoiseThreshold() { // getter method to receive threshold value
        return noiseThreshold;
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
