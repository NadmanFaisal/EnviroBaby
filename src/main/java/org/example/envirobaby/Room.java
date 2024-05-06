package org.example.envirobaby;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Room{
    private NotificationThreshold thresholds;
    private ParameterData sensorReading;
    private MQTTReceiver client;
    private String userId;
    private String roomName;
    private int capacity;
    private boolean ageGroup;

    private DatabaseControl database;

    public Room(String userId, String roomName, int capacity, boolean ageGroup) throws MqttException {
        this.userId = userId;
        this.roomName = roomName;
        this.capacity = capacity;
        this.ageGroup= ageGroup;
        this.thresholds = new NotificationThreshold();
        this.client = new MQTTReceiver(userId);
        this.sensorReading = client.getReadings();
    }



    public void updateThreshold(TextField textField) throws SQLException { // method that updates the threshold value
        String thresholdTextValue = textField.getText(); // gets and stores the string value from textField

        if (textField.getId().equals("maxNoise")) {
            if (thresholdTextValue.matches("\\d+")) { //condition to find if there are any numeric value
                thresholds.setLoudThreshold(Integer.parseInt(thresholdTextValue)); // converts the string into integer
                database.updateThresholds(this.userId,this.roomName,"maxnoise",Double.parseDouble(thresholdTextValue)); //store the updated threshold for the room
            } else {
                System.out.println("Enter a numeric value, Thank you!"); // if no numeric value id found this is printed
            }
        } else {
            if (thresholdTextValue.matches("[0-9]+(\\.[0-9][0-9]?)?")) { //checks for double using Regex
                switch (textField.getId()) {
                    case "maxTemp":
                        thresholds.setTempUpperBound(Double.parseDouble(thresholdTextValue));
                        database.updateThresholds(this.userId,this.roomName,"maxtemp",Double.parseDouble(thresholdTextValue)); //store the updated threshold for the room
                        break;
                    case "minTemp":
                        thresholds.setTempLowerBound(Double.parseDouble(thresholdTextValue));
                        database.updateThresholds(this.userId,this.roomName,"mintemp",Double.parseDouble(thresholdTextValue));
                        break;
                    case "maxHum":
                        thresholds.setHumUpperBound(Double.parseDouble(thresholdTextValue));
                        database.updateThresholds(this.userId,this.roomName,"maxhum",Double.parseDouble(thresholdTextValue));
                        break;
                    case "minHum":
                        thresholds.setHumLowerBound(Double.parseDouble(thresholdTextValue));
                        database.updateThresholds(this.userId,this.roomName,"minhum",Double.parseDouble(thresholdTextValue));
                        break;
                }
            } else {
                System.out.println("Enter a numeric value, Thank you!");
            }
        }
    }

    public String getRoomName() {
        return roomName;
    }
    public int getCapacity() {
        return capacity;
    }
    public boolean isAgeGroup() {
        return ageGroup;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    public void setAgeGroup(boolean ageGroup) {
        this.ageGroup = ageGroup;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public NotificationThreshold getThresholds() {
        return thresholds;
    }
    public ParameterData getSensorReading() {
        return sensorReading;
    }
}
