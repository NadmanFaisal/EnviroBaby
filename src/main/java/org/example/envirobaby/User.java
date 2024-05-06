package org.example.envirobaby;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class User {

    private String userID;
    private DatabaseControl database;
    private HashMap<String,Room> rooms;

    public User(String userId) throws SQLException, MqttException {
        database = new DatabaseControl();
        this.userID = userId;
        this.rooms = setRooms(userId);
    }

    public HashMap<String,Room> setRooms(String userID) throws SQLException, MqttException {
        HashMap<String,Room> registeredRooms = new HashMap<>();
        ResultSet roomResults= database.retrieveRooms(userID); //get list of rooms already owned by the user

        while (roomResults.next()) {
            // for each room owned by the user, get the stored values
            String roomName = roomResults.getString("room_name");
            int capacity = roomResults.getInt("capacity");
            boolean ageGroup = roomResults.getBoolean("age_group");
            int maxNoise = roomResults.getInt("maxnoise");
            double maxtemp= roomResults.getDouble("maxtemp");
            double mintemp= roomResults.getDouble("mintemp");
            double maxHum= roomResults.getDouble("maxhum");
            double minHum= roomResults.getDouble("minhum");
            boolean celsius = roomResults.getBoolean("celsius");
            boolean noiseAlerts = roomResults.getBoolean("noise_alerts");
            boolean tempAlerts = roomResults.getBoolean("temp_alerts");
            boolean humAlerts = roomResults.getBoolean("hum_alerts");

            Room newRoom = new Room(userID,roomName,capacity,ageGroup); //create new room object using these instances
            newRoom.getThresholds().setAllThresholds(maxNoise,maxtemp,mintemp,maxHum,minHum); //update the new room objects thresholds to the stored value

            //initialise notification toggle and celsius when implemented

            registeredRooms.put(roomName,newRoom); //add room to list
        }
        return registeredRooms;
    }

    public HashMap<String, Room> getRooms() {
        return rooms;
    }

    public Room getRoom(String roomName) {
        return rooms.get(roomName);
    }

    public String getUserID() {
        return userID;
    }
}
