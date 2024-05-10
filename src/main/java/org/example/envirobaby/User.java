package org.example.envirobaby;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class User {

    private String userID;
    private DatabaseControl database;
    private HashMap<Integer,Room> rooms;
    private Room room;

    public User(String userId) throws SQLException, MqttException {
        database = new DatabaseControl();
        this.userID = userId;
        setRooms(userId);
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
        }
        this.rooms = registeredRooms;
    }

    /** Creates a room based on the specified parameters, adds it to the user's rooms, and stores it to the database
     *
     * @throws SQLException If there is an error interacting with the database
     * @throws MqttException If there is an error with the MQTT connection
     */
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

    public HashMap<Integer, Room> getRooms() {
        return rooms;
    }

    public Room getRoom(Integer roomNumber) {
        return rooms.get(roomNumber);
    }

    public String getUserID() {
        return userID;
    }
}
