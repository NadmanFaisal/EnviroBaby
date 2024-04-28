package org.example.envirobaby;

import javafx.scene.control.Label;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import org.eclipse.paho.client.mqttv3.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private String noiseValue; // stores last published message
    private TextField maxNoise; // reference to textField object

    private TextField maxTempBox;
    private TextField minTempBox;
    private int noiseThreshold; // stores last received noise level value
    private double tempUbound;
    private double tempLbound;

    private String tempValue; // Holds the latest temperature data received via MQTT
    private String humValue; // Holds the latest humidity data received via MQTT

    private double minHum; // stores latest input for minimum humidity threshold
    private double maxHum; // stores latest input for maximum humidity threshold
    private TextField minHumBox; //reference to the TextField object
    private TextField maxHumBox; //reference to the TextField object

    private Notification notification; // instance variable from Notification class

    private Parameters parameters;



    // Constructor sets up labels and MQTT connection, subscribes to topics for loudness and temperature.
    public MQTTSubscriber(Label noiseLabel, Label tempLabel, Label humLabel, TextField maxNoise, TextField maxTempBox, TextField minTempBox, TextField minHumBox, TextField maxHumBox) {
        this.parameters = new Parameters(noiseLabel, tempLabel, humLabel);
        this.notification = new Notification();
        this.maxTempBox = maxTempBox;
        this.minTempBox =minTempBox;
        this.minHumBox = minHumBox;
        this.maxHumBox = maxHumBox;
        this.maxNoise = maxNoise;

        this.tempUbound = 25.00;
        this.tempLbound = 18.00;
        this.minHum = 30;
        this.maxHum = 60;
        this.noiseThreshold = 90;

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
                Label noiseLabel = parameters.getNoiseLabel();
                parameters.updateLabel(noiseLabel, noiseValue);

                notification.createNoiseNotification(noiseValue, noiseThreshold);
            }

            case TEMP_TOPIC -> {
                tempValue = new String(message.getPayload());
                Label tempLabel = parameters.getTempLabel();
                parameters.updateLabel(tempLabel, tempValue);
                notification.createTempNotification(tempValue, tempUbound, tempLbound);
            }

            case HUM_TOPIC -> {
                humValue = new String(message.getPayload());
                Label humLabel = parameters.getHumLabel();
                parameters.updateLabel(humLabel, humValue);
                notification.createHumNotification(humValue, maxHum, minHum);
            }
        }

    }

    public void updateNoiseThreshold() { // method that updates the threshold value
        String thresholdTextValue = maxNoise.getText(); // gets and stores the string value from textField

            if (thresholdTextValue.matches("\\d+")) { //condition to find if there are any numeric value
                this.noiseThreshold = Integer.parseInt(thresholdTextValue); // converts the string into integer
            } else {
                System.out.println("Enter a numeric value, Thank you!"); // if no numeric value id found this is printed
            }
    }
    public void updateTempUbound() { // method that updates the threshold value
        String thresholdTextValue = maxTempBox.getText();

        if (thresholdTextValue.matches("[0-9]{1,13}(\\.[0-9]*)?")) { //checks for double using Regex
            this.tempUbound = Double.parseDouble(thresholdTextValue); // converts the string into double
        } else {
            System.out.println("Enter a numeric value, Thank you!");
        }
    }
        public void updateTempLbound() {
        String thresholdTextValue = minTempBox.getText();

        if (thresholdTextValue.matches("[0-9]{1,13}(\\.[0-9]*)?")) {
            this.tempLbound = Double.parseDouble(thresholdTextValue); // converts the string into double
        } else {
            System.out.println("Enter a numeric value, Thank you!");
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

    public double getTempUbound() { // getter method to receive threshold value
        return tempUbound;
    }
    public double getTempLbound() {
        return tempLbound;
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

    public double extractDouble(String message) { //  locate double within a String
        Pattern doublePattern = Pattern.compile("([0-9]{1,13}(\\.[0-9]*)?)"); //setup regex pattern
        Matcher doubleMatcher = doublePattern.matcher(message); // search for pattern in string
        double extracted = 0;

        if(doubleMatcher.find()) {
            extracted = Double.parseDouble(doubleMatcher.group()); //if found, parse into double
        }
        return extracted;
    }
}