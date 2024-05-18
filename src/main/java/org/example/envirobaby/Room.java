package org.example.envirobaby;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Room {
    private static DatabaseControl database;
    private final NotificationThreshold thresholds;
    private final ParameterData sensorReading;
    private final MQTTReceiver client;
    private String userId;
    private String roomName;
    private int capacity;
    private String ageGroup;
    private Notification alerts;


    DecimalFormat df = new DecimalFormat("#.00");


    public Room(String userId, String roomName, int capacity, String ageGroup, String loud, String temp, String humi) throws MqttException, SQLException {
        this.userId = userId;
        this.roomName = roomName;
        this.capacity = capacity;
        this.ageGroup = ageGroup;
        this.alerts = new Notification();
        this.thresholds = new NotificationThreshold();
        this.client = new MQTTReceiver(userId, loud, temp, humi);
        this.sensorReading = client.getReadings();

        database = new DatabaseControl();

    }

    public void updateThreshold(TextField textField) throws SQLException { // method that updates the threshold value
        String thresholdTextValue = textField.getText(); // gets and stores the string value from textField

        if (textField.getId().equals("maxNoise")) {
            if (thresholdTextValue.matches("\\d+")) { //condition to find if there are any numeric value
                thresholds.setLoudThreshold(Integer.parseInt(thresholdTextValue)); // converts the string into integer
                database.updateThresholds(userId, this.roomName, "maxnoise", Double.parseDouble(thresholdTextValue)); //store the updated threshold for the room
            } else {
                System.out.println("Enter a numeric value, Thank you!"); // if no numeric value id found this is printed
            }
        } else {
            if (thresholdTextValue.matches("[0-9]+(\\.[0-9][0-9]?)?")) { //checks for double using Regex
                switch (textField.getId()) {
                    case "maxTemp":
                        thresholds.setTempUpperBound(Double.parseDouble(thresholdTextValue));
                        database.updateThresholds(userId, this.roomName, "maxtemp", Double.parseDouble(thresholdTextValue)); //store the updated threshold for the room
                        break;
                    case "minTemp":
                        thresholds.setTempLowerBound(Double.parseDouble(thresholdTextValue));
                        database.updateThresholds(userId, this.roomName, "mintemp", Double.parseDouble(thresholdTextValue));
                        break;
                    case "maxHum":
                        thresholds.setHumUpperBound(Double.parseDouble(thresholdTextValue));
                        database.updateThresholds(userId, this.roomName, "maxhum", Double.parseDouble(thresholdTextValue));
                        break;
                    case "minHum":
                        thresholds.setHumLowerBound(Double.parseDouble(thresholdTextValue));
                        database.updateThresholds(userId, this.roomName, "minhum", Double.parseDouble(thresholdTextValue));
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

    public String isAgeGroup() {
        return ageGroup;
    }

    public String getUserId() {
        return userId;
    }

    public NotificationThreshold getThresholds() {
        return thresholds;
    }

    public ParameterData getSensorReading() {
        return sensorReading;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public Notification getAlerts() { return alerts; }

}
