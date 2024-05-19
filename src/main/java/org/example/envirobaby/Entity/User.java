package org.example.envirobaby.Entity;

import javafx.application.Platform;
import javafx.scene.control.Button;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.example.envirobaby.Database.DatabaseControl;
import org.example.envirobaby.MQTT.MQTTSender;
import org.example.envirobaby.Notification.Notification;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class User {

    private String userID;
    private DatabaseControl database;
    private HashMap<Integer,Room> rooms;
    private Room room;
    private boolean celsius;

    private MQTTSender sender;
    private boolean tempNotiStatus;
    private boolean humiNotiStatus;
    private boolean noiseNotiStatus;

    private String selectedDataView; //store which data display the user created for this instance, allow consistency across pages

    DecimalFormat df = new DecimalFormat("#.00");


    public User(String userId, boolean tempNotiStatus, boolean humiNotiStatus, boolean noiseNotiStatus) throws SQLException, MqttException {
        database = new DatabaseControl();
        this.userID = userId;
        this.celsius = true;
        setRooms(userId);
        this.tempNotiStatus = tempNotiStatus; //default settings for the system temperature notifications
        this.humiNotiStatus = humiNotiStatus; //default settings for the system humidity notifications
        this.noiseNotiStatus = noiseNotiStatus; //default settings for the system noise notifications
        this.selectedDataView="temp"; //ensure the user always initially views the temp graph (default option)
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
            int terminalNum= roomResults.getInt("terminal_topic_num");

            Room newRoom = new Room(userID,roomName,capacity,ageGroup, "/envirobaby/room" + terminalNum + "/loud", "/envirobaby/room" + terminalNum + "/temp", "/envirobaby/room" + terminalNum + "/humi", tempAlerts, humAlerts, noiseAlerts, terminalNum); //create new room object using these instances
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
                           double minHum, boolean celsius) throws SQLException, MqttException {

        ResultSet usedTopics = database.recieveRegisteredTerminalTopics(this.userID);
        List<Integer> topicsInUse = new ArrayList<>();
        int topicNum=0;
        for (int i=1; i<=4; i++) {
            while (usedTopics.next()){
                topicsInUse.add(usedTopics.getInt("terminal_topic_num"));
            }

            if(!topicsInUse.contains(i)) {
                topicNum = i;
                break;
            }
        }

        Room room = new Room(userId, roomName, capacity, ageGroup, "/envirobaby/room" + topicNum + "/loud", "/envirobaby/room" + topicNum + "/temp", "/envirobaby/room" + topicNum + "/humi", true,true,true, topicNum);
        room.getThresholds().setAllThresholds(maxNoise, maxTemp, minTemp, maxHum, minHum);
        database.addRoom(roomName, userID, capacity, ageGroup, maxNoise, maxTemp,
                minTemp, maxHum, minHum, celsius, true, true, true, topicNum);

        // Generates a room number and adds the room to the user's rooms
        Integer roomCount= rooms.size() + 1;
        rooms.put(roomCount, room);

        return room;
    }

    public void deleteRoom(int roomHashmapKey) throws SQLException { // take the hashmap index of the room
        database.removeRoom(rooms.get(roomHashmapKey).getRoomName(),this.userID);
        rooms.remove(roomHashmapKey);

        for(int i= roomHashmapKey; i<=rooms.size(); i++){ //when room is removed, reorder hashmap indexes following it
            int oldKey=i+1;
            Room room= rooms.get(oldKey);
            rooms.remove(oldKey);
            rooms.put(i,room);
        }
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
            if (tempNotiStatus && room.getAlerts().isTempNotifOn()) { //Condition triggers the temperature notifications
                if (tempLvl > room.getThresholds().getTempUpperBound()) {
                    room.getAlerts().createNotification("Temperature notification", "TEMPERATURE IN " + room.getRoomName().toUpperCase() + " EXCEEDS THRESHOLD: " + tempMsg,"maxTemp");
                } else if (tempLvl < room.getThresholds().getTempLowerBound()) {
                    room.getAlerts().createNotification("Temperature notification", "TEMPERATURE IN " + room.getRoomName().toUpperCase() + " EXCEEDS THRESHOLD: " + tempMsg, "minTemp");
                }
            }

            // humidity alerts
            if (humiNotiStatus && room.getAlerts().isHumiNotifOn()) { //Condition triggers the humidity notifications
                if (humLvl > room.getThresholds().getHumUpperBound()) {
                    room.getAlerts().createNotification("Humidity notification", "HUMIDITY IN "  + room.getRoomName().toUpperCase() +  " EXCEEDS THRESHOLD: " + df.format(humLvl) + "%","maxHum");
                } else if (humLvl < room.getThresholds().getHumLowerBound()) {
                    room.getAlerts().createNotification("Humidity notification", "HUMIDITY IN "  + room.getRoomName().toUpperCase() +  " EXCEEDS THRESHOLD: " + df.format(humLvl) + "%","minHum");
                }
            }

            // noise alerts
            if (noiseNotiStatus && room.getAlerts().isNoiseNotifOn()) { //Condition triggers the noise notifications
                if (noiseLvl > room.getThresholds().getLoudThreshold()) {
                    room.getAlerts().createNotification("Noise notification", "NOISE THRESHOLD IN "  + room.getRoomName().toUpperCase() + " CROSSED: " + noiseLvl + " db", "maxNoise");
                }
            }
        }
    }

    public void recordData() {
        for (int i =1 ; i <= rooms.size(); i++){
            Room room = rooms.get(i);

            String userId = room.getUserId();
            String roomName = room.getRoomName();
            String recordTime = String.valueOf(LocalTime.now().truncatedTo(ChronoUnit.MINUTES));
            String recordDate = String.valueOf(LocalDate.now());
            int loudVal = room.getSensorReading().getLoudValue();
            double tempVal = room.getSensorReading().getTempValue();
            double humVal = room.getSensorReading().getHumValue();

            try {
                database.recordData(userId,roomName,recordDate,recordTime,loudVal,tempVal,humVal);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public String getSelectedDataView() {
        return selectedDataView;
    }

    public void setSelectedDataView(String selectedDataView) {
        this.selectedDataView = selectedDataView;
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

    public void setTempNotiStatus(boolean tempNotiStatus) {
        this.tempNotiStatus = tempNotiStatus;
    }

    public void setNoiseNotiStatus(boolean noiseNotiStatus) {
        this.noiseNotiStatus = noiseNotiStatus;
    }

    public void setHumiNotiStatus(boolean humiNotiStatus) {
        this.humiNotiStatus = humiNotiStatus;
    }

    public boolean isTempNotiStatus() {
        return tempNotiStatus;
    }

    public boolean isHumiNotiStatus() {
        return humiNotiStatus;
    }

    public boolean isNoiseNotiStatus() {
        return noiseNotiStatus;
    }


    public void backGroundFunctions() {
        int delay=0;
        int currentTime = LocalTime.now().getMinute();

        if (currentTime>30) {
            delay = 60-currentTime;
        } else if(currentTime!=0 && currentTime!=30) {
            delay=30-currentTime;
        } // set delay so that we start recording data only when the clock is at HH:00 or HH:30

        // Create a ScheduledExecutorService for notifications
        ScheduledExecutorService notificationScheduler = Executors.newSingleThreadScheduledExecutor();
        //Schedule sendAlerts with initial delay of 101 to avoid notifications during initialisation
        notificationScheduler.scheduleAtFixedRate(this::sendAlerts, 101, 100, TimeUnit.MILLISECONDS);

        // Create a ScheduleExecutorService for storing records
        ScheduledExecutorService storeData = Executors.newSingleThreadScheduledExecutor();
        storeData.scheduleAtFixedRate(this::recordData, delay, 30, TimeUnit.MINUTES);
        // starts recording data every 30 minutes after an initial delay.
    }

//    public void run() {
//        int delay=0;
//        int currentTime = LocalTime.now().getMinute();
//
//        if (currentTime>30) {
//            delay = 60-currentTime;
//        } else if(currentTime!=0 && currentTime!=30) {
//            delay=30-currentTime;
//        } // set delay so that we start recording data only when the clock is at HH:00 or HH:30
//
//        // Create a ScheduledExecutorService for notifications
//        ScheduledExecutorService notificationScheduler = Executors.newSingleThreadScheduledExecutor();
//        //Schedule sendAlerts with initial delay of 101 to avoid notifications during initialisation
//        notificationScheduler.scheduleAtFixedRate(this::sendAlerts, 101, 100, TimeUnit.MILLISECONDS);
//
//        // Create a ScheduleExecutorService for storing records
//        ScheduledExecutorService storeData = Executors.newSingleThreadScheduledExecutor();
//        storeData.scheduleAtFixedRate(this::recordData, delay, 30, TimeUnit.MINUTES);
//        // starts recording data every 30 minutes after an initial delay.
//    }
}

