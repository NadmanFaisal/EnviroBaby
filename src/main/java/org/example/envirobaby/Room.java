package org.example.envirobaby;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
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
    private boolean tempNotif;
    private boolean humiNotif;
    private boolean noiseNotif;
    private ObservableList<Record> records;
    private String recordViewDate; //allow user to view the same date through different data displays
    private int terminalTopic;



    DecimalFormat df = new DecimalFormat("#.00");


    public Room(String userId, String roomName, int capacity, String ageGroup, String loud, String temp, String humi, boolean tempNotif, boolean humiNotif, boolean noiseNotif, int terminalTopic) throws MqttException, SQLException {
        this.userId = userId;
        this.roomName = roomName;
        this.capacity = capacity;
        this.ageGroup = ageGroup;
        this.alerts = new Notification();
        this.thresholds = new NotificationThreshold();
        this.client = new MQTTReceiver(userId, loud, temp, humi);
        this.sensorReading = client.getReadings();
        this.tempNotif = tempNotif;
        this.humiNotif = humiNotif;
        this.noiseNotif = noiseNotif;


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

    public void updateRecordList(String selectedDate) throws SQLException { //retrieve recorded data to display in graph and table
        List<Record> recordsList = new ArrayList<>();
        ResultSet recordedData = database.recieveRecordedData(this.userId, this.roomName, selectedDate);

        while (recordedData.next()){
            String recordTime = recordedData.getString("record_time");
            int noiseLvl = recordedData.getInt("loud_data");
            double tempLvl = recordedData.getDouble("temp_data");
            double humLvl = recordedData.getDouble("hum_data");

            Record newRecord = new Record(recordTime.substring(0,5),noiseLvl,tempLvl,humLvl); //substring used to put sql.Time in HH:MM format

            recordsList.add(newRecord);
        }

        this.records = FXCollections.observableArrayList(recordsList); //must be observableArrayList for table
    }

    public void updateRoom(String userId, String newRoomName, String ageGroup, int capacity) throws SQLException {
        database.updateRoom(this.userId,this.roomName,newRoomName,capacity,ageGroup); //update edited data into db
        this.roomName=newRoomName; //update current room object
        this.ageGroup=ageGroup;
        this.capacity=capacity;
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

    public ObservableList<Record> getRecords() {
        return records;
    }



    public void settTempNotifON(boolean TempNotif){ //sets the temperature notification status
        this.tempNotif=TempNotif;
    }
    public void setHumiNotifON(boolean humiNotif){ //sets the humidity notification status
        this.humiNotif=humiNotif;
    }
    public void setNoiseNotifON(boolean noiseNotif){ //sets the noise notification status
        this.noiseNotif=noiseNotif;
    }

    public boolean isTempNotif() { //Returns the current status of temperature notifications.
        return this.tempNotif;
    }

    public boolean isHumiNotif() { //Returns the current status of humidity notifications.
        return this.humiNotif;
    }

    public boolean isNoiseNotif() { ////Returns the current status of noise notifications.
        return this.noiseNotif;
    }

    public String getRecordViewDate() {
        return recordViewDate;
    }

    public void setRecordViewDate(String recordViewDate) {
        this.recordViewDate = recordViewDate;
    }

    public int getTerminalTopic() {
        return terminalTopic;
    }

    public void setTerminalTopic(int terminalTopic) {
        this.terminalTopic = terminalTopic;
    }
}
