package org.example.envirobaby;

import java.sql.*;

public class DatabaseControl {
     String jdbcURL = "";
     String username = "";
     String password = "";
     Connection connection;
     Statement statement;

    public DatabaseControl() throws SQLException {
        connection = DriverManager.getConnection(jdbcURL,username,password);
        statement = connection.createStatement(); //initialise statement
    }

    public boolean signUpUser(String userID, String userPassword) throws SQLException {
        boolean userNotAvailable;

        String availabilityQuery = String.format("SELECT * FROM USERS WHERE id= '%s'", userID); // check if the username the user is trying to register already exists

        ResultSet rs = statement.executeQuery(availabilityQuery);
        userNotAvailable = rs.next(); //equals true if query returned results (username already exists)

        if(!userNotAvailable) { //if username is available, insert new user into users table and return true
            String insertSQL = String.format("INSERT INTO USERS(id, password) VALUES('%s','%s')",userID,userPassword);
            statement.executeUpdate(insertSQL);
            return true;
        } else {return false;} //if username isn't available, return false
    }

    public boolean attemptLogIn(String userID, String password) throws SQLException {
        String sqlQuery = String.format("SELECT * FROM USERS WHERE id= '%s' AND password= '%s'", userID, password); //check if a user with those exact credentials exist
        ResultSet rs= statement.executeQuery(sqlQuery);
        return rs.next(); // if query returns results, it means credentials are correct. return true
    }

    public ResultSet retrieveRooms(String userID) throws SQLException {
        String sqlQuery = String.format("SELECT * FROM ROOM WHERE userid='%s'", userID); //select all rooms owned by the user
        ResultSet rs = statement.executeQuery(sqlQuery);
        return rs; //return list of rooms
    }

    public void addRoom(String roomName, String userID, int capacity, String ageGroup, int maxNoise, double maxTemp, double minTemp, double maxHum, double minHum, boolean celsius, boolean noiseAlert, boolean tempAlert, boolean humAlert) throws SQLException {

        String insertSQL = String.format("INSERT INTO ROOM(room_name, userid,capacity,age_group,maxnoise,maxtemp,mintemp,maxhum,minhum,celsius, noise_alerts,temp_alerts,hum_alerts) " +
                "VALUES('%s','%s',%d,'%s',%d,%f,%f,%f,%f,%s,%s,%s,%s)", roomName,userID,capacity,ageGroup,maxNoise,maxTemp,minTemp,maxHum,minHum,celsius,noiseAlert,tempAlert,humAlert);
        statement.executeUpdate(insertSQL); //save room data and settings into row
    }

    public void removeRoom(String roomName, String userID) throws SQLException {
        String removeQuery = String.format("DELETE FROM ROOM WHERE room_name='%s' AND userid='%s'",roomName,userID); //remove room from database where name and id match the parameters
        statement.executeUpdate(removeQuery);
    }
    
    public void updateRoom(String userId, String currentRoom, String roomName,int capacity, String ageGroup) throws SQLException {
        String updateQuery = String.format("UPDATE ROOM SET room_name='%s', capacity=%d,age_group='%s'" +
                "WHERE userid='%s' AND room_name='%s'", roomName,capacity,ageGroup,userId,currentRoom);
        statement.executeUpdate(updateQuery); //edit room data
    }

    public void updateAlertToggle(String userId, String roomName, boolean noiseAlert, boolean tempAlert, boolean humAlert) throws SQLException {
        String updateQuery = String.format("UPDATE ROOM SET noise_alerts='%s', temp_alerts='%s',hum_alerts='%s'" +
                "WHERE userid='%s' AND room_name='%s'",noiseAlert,tempAlert,humAlert,userId,roomName);
        statement.executeUpdate(updateQuery); //update preferences for receiving notifications
    }

    public void updateThresholds(String userId, String roomName, String thresholdLabel, double threshold) throws SQLException {
        String updateQuery = String.format("UPDATE ROOM SET %s=%f WHERE userid='%s' AND room_name='%s'", thresholdLabel,threshold, userId,roomName);
        statement.executeUpdate(updateQuery); //update notification threshold value depending on which threshold is selected
    }

    public void updateTempUnit(String userId, boolean celsius) throws SQLException {
        String updateQuery = String.format("UPDATE ROOM SET celsius=%s WHERE userid='%s'", celsius,userId);
        statement.executeUpdate(updateQuery);
    }

    public void recordData(String userId, String roomName, String recordDate, String recordTime, int loudVal, double tempVal, double humVal) throws SQLException {
        String recordQuery = String.format("INSERT INTO RECORD(userid,room_name,record_date,record_time,loud_data,temp_data,hum_data) " +
                "VALUES('%s','%s','%s','%s',%d,%f,%f)", userId,roomName,recordDate,recordTime,loudVal,tempVal,humVal);
        statement.executeUpdate(recordQuery); //create new data record row in record table
    }

    public void addChild(String userId, String roomName, String childName, int age) throws SQLException {
        String insertQuery = String.format("INSERT INTO CHILDREN(userid,room,child,age) VALUES('%s','%s','%s',%d)", userId,roomName,childName,age);
        statement.executeUpdate(insertQuery); //create new row in children table. childId is declared automatically in table
    }

    public void removeChild(int childId) throws SQLException {
        String removeQuery = String.format("DELETE FROM CHILDREN WHERE childid=%d",childId);
        statement.executeUpdate(removeQuery); //remove child from the children table
    }

    public ResultSet retrieveChildren(String userId, String roomName) throws SQLException {
        String sqlQuery = String.format("SELECT * FROM CHILDREN WHERE userid='%s' AND room='%s' ORDER BY child ASC", userId,roomName);
        ResultSet rs = statement.executeQuery(sqlQuery);
        return rs; //return all children data for a specific room owned by the user
    }
}