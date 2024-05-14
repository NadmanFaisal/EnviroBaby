package org.example.envirobaby;

import javafx.scene.control.Button;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class User implements Runnable {

    private String userID;
    private DatabaseControl database;
    private HashMap<Integer,Room> rooms;
    private Room room;
    private Notification alerts;
    private boolean celsius;

    private MQTTSender sender;
    private boolean tempNotiStatus;
    private boolean humiNotiStatus;
    private boolean noiseNotiStatus;

    DecimalFormat df = new DecimalFormat("#.00");


    public User(String userId) throws SQLException, MqttException {
        database = new DatabaseControl();
        this.userID = userId;
        this.alerts = new Notification();
        this.celsius = true;
        setRooms(userId);
        this.tempNotiStatus = true; //default settings for the system temperature notifications
        this.humiNotiStatus = true; //default settings for the system humidity notifications
        this.noiseNotiStatus = true; //default settings for the system noise notifications
    }

    public void setRooms(String userID) throws SQLException, MqttException {
        HashMap<Integer,Room> registeredRooms = new HashMap<>();
        ResultSet roomResults= database.retrieveRooms(userID); //get list of rooms already owned by the user

        while (roomResults.next()) {
            // for each room owned by the user, get the stored values
            String roomName = roomResults.getString("room_name");
            int capacity = roomResults.getInt("capacity");
            String ageGroup = roomResults.getString("age_group");
            int maxNoise = roomResults.getInt("maxnoise");
            double maxtemp= roomResults.getDouble("maxtemp");
            double mintemp= roomResults.getDouble("mintemp");
            double maxHum= roomResults.getDouble("maxhum");
            double minHum= roomResults.getDouble("minhum");
            boolean celsius = roomResults.getBoolean("celsius");
            boolean noiseAlerts = roomResults.getBoolean("noise_alerts");
            boolean tempAlerts = roomResults.getBoolean("temp_alerts");
            boolean humAlerts = roomResults.getBoolean("hum_alerts");

            Room newRoom = new Room(userID,roomName,capacity,ageGroup, "/envirobaby/room" + (registeredRooms.size() + 1) + "/loud", "/envirobaby/room" +( registeredRooms.size() +1) + "/temp", "/envirobaby/room" + (registeredRooms.size() +1) + "/humi"); //create new room object using these instances
            newRoom.getThresholds().setAllThresholds(maxNoise,maxtemp,mintemp,maxHum,minHum); //update the new room objects thresholds to the stored value

            //initialise notification toggle and celsius when implemented
            Integer roomCount= registeredRooms.size() + 1;
            registeredRooms.put(roomCount,newRoom); //add room to list

            this.celsius = celsius;
        }
        this.rooms = registeredRooms;
    }

    // Creates a room based on the specified parameters, adds it to the user's rooms, and stores it to the database
    public Room createRoom(String roomName, String userId, int capacity, String ageGroup,
                                                            int maxNoise, double maxTemp, double minTemp, double maxHum,
                                                            double minHum, boolean celsius, boolean noiseAlert,
                                                            boolean tempAlert, boolean humAlert) throws SQLException, MqttException {
        Room room = new Room(userId, roomName, capacity, ageGroup, "/envirobaby/room" + (rooms.size() + 1) + "/loud", "/envirobaby/room" + (rooms.size() + 1) + "/temp", "/envirobaby/room" + (rooms.size() + 1) + "/humi");
        room.getThresholds().setAllThresholds(maxNoise, maxTemp, minTemp, maxHum, minHum);
        database.addRoom(roomName, userID, capacity, ageGroup, maxNoise, maxTemp,
                minTemp, maxHum, minHum, celsius, noiseAlert, tempAlert, humAlert);

        // Generates a room number and adds the room to the user's rooms
        Integer roomCount= rooms.size() + 1;
        rooms.put(roomCount, room);

        return room;
    }

    public void sendAlerts(){
        for (int i = 1; i <= rooms.size(); i++) {
            Room room = rooms.get(i);
            int noiseLvl = room.getSensorReading().getLoudValue();
            double tempLvl = room.getSensorReading().getTempValue();
            double humLvl = room.getSensorReading().getHumValue();
            String tempMsg = df.format(tempLvl) +"C";

            if (!celsius) {
                tempMsg = df.format((tempLvl * (9/5)) + 32) + "F";
            }

            //only send notifications if above/below threshold AND if it isn't the same value as the last sent notification to avoid duplicates
            // temperature alerts
            if (tempNotiStatus) { //Condition triggers the temperature notifications
                if (tempLvl > room.getThresholds().getTempUpperBound() && tempLvl != alerts.getLastMaxTempAlert()) {
                alerts.createNotification("Temperature notification", "TEMPERATURE IN " + room.getRoomName().toUpperCase() + " EXCEEDS THRESHOLD: " + tempMsg);
                alerts.setLastMaxTempAlert(tempLvl);
                } else if (tempLvl < room.getThresholds().getTempLowerBound() && tempLvl != alerts.getLastMinTempAlert()) {
                alerts.createNotification("Temperature notification", "TEMPERATURE IN " + room.getRoomName().toUpperCase() +  " BELOW THRESHOLD: " + tempMsg);
                alerts.setLastMinTempAlert(tempLvl);
                }
            }

            // humidity alerts
            if (humiNotiStatus) { //Condition triggers the humidity notifications
                if (humLvl > room.getThresholds().getHumUpperBound() && humLvl != alerts.getLastMaxHumAlert()) {
                alerts.createNotification("Humidity notification", "HUMIDITY IN "  + room.getRoomName().toUpperCase() +  " EXCEEDS THRESHOLD: " + df.format(humLvl) + "%");
                alerts.setLastMaxHumAlert(humLvl);
                } else if (humLvl < room.getThresholds().getHumLowerBound() && humLvl != alerts.getLastMinHumAlert()) {
                alerts.createNotification("Humidity notification", "HUMIDITY IN "  + room.getRoomName().toUpperCase() +  " BELOW THRESHOLD: " + df.format(humLvl) + "%");
                alerts.setLastMinHumAlert(humLvl);
                }
            }

            // noise alerts
            if (noiseNotiStatus) { //Condition triggers the noise notifications
                if (noiseLvl > room.getThresholds().getLoudThreshold() && noiseLvl != alerts.getLastNoiseAlert()) {
                alerts.createNotification("Noise notification", "NOISE THRESHOLD IN "  + room.getRoomName().toUpperCase() + " CROSSED: " + noiseLvl + " db");
                alerts.setLastNoiseAlert(noiseLvl);
                }
            }
        }
    }

    public HashMap<Integer, Room> getRooms() {
        return rooms;
    }

    public Room getRoom(Integer roomNumber) {
        return rooms.get(roomNumber);
    }

    public String getUserID() {
        return userID;
    }

    public void setCelsius(boolean celsius) {
        this.celsius = celsius;
    }

    public boolean isCelsius() {
        return celsius;
    }

    public void tempNotiON(){
        tempNotiStatus = true;
    }

    public void tempNotiOFF(){
        tempNotiStatus = false;
    }

    public void humiNotiON(){
        humiNotiStatus = true;
    }

    public void humiNotiOFF(){
        humiNotiStatus = false;
    }

    public void noiseNotiON(){
        noiseNotiStatus = true;
    }

    public void noiseNotiOFF(){
        noiseNotiStatus = false;
    }


    @Override
    public void run() {
        // Create a ScheduledExecutorService for notifications
        ScheduledExecutorService notificationScheduler = Executors.newSingleThreadScheduledExecutor();
        //Schedule sendAlerts with initial delay of 101 to avoid notifications during initialisation
        notificationScheduler.scheduleAtFixedRate(this::sendAlerts, 101, 100, TimeUnit.MILLISECONDS);
    }
}
