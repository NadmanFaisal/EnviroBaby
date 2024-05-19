package org.example.envirobaby.Database;

import java.sql.*;

public class DatabaseControl {
     String jdbcURL;
     String username;
     String password;
     Connection connection;
     Statement statement;

    public DatabaseControl() throws SQLException {
        String jdbcURL = "";
        String username = "";
        String password = "";
        connection = DriverManager.getConnection(jdbcURL,username,password);
        statement = connection.createStatement(); //initialise statement
    }

    public boolean signUpUser(String userID, String userPassword) throws SQLException {
        boolean userNotAvailable;

        String availabilityQuery = String.format("SELECT id, password FROM USERS WHERE id= '%s'", userID); // check if the username the user is trying to register already exists

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
        String sqlQuery = String.format("SELECT * FROM ROOM WHERE userid='%s' ORDER BY created_at", userID);
        ResultSet rs = statement.executeQuery(sqlQuery);
        return rs;
    }

    public void addRoom(String roomName, String userID, int capacity, String ageGroup, int maxNoise, double maxTemp, double minTemp, double maxHum, double minHum, boolean celsius, boolean noiseAlert, boolean tempAlert, boolean humAlert, int topicNum) throws SQLException {

        String insertSQL = String.format("INSERT INTO ROOM(room_name, userid,capacity,age_group,maxnoise,maxtemp,mintemp,maxhum,minhum,celsius, noise_alerts,temp_alerts,hum_alerts,terminal_topic_num) " +
                "VALUES('%s','%s',%d,'%s',%d,%f,%f,%f,%f,%s,%s,%s,%s,%d)", roomName,userID,capacity,ageGroup,maxNoise,maxTemp,minTemp,maxHum,minHum,celsius,noiseAlert,tempAlert,humAlert,topicNum);
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
    public ResultSet recieveRegisteredTerminalTopics(String userId) throws SQLException {
        String sqlQuery = String.format("SELECT terminal_topic_num FROM ROOM WHERE userid='%s'",userId);
        ResultSet rs = statement.executeQuery(sqlQuery);
        return rs;
    } //return the room numbers the user already has in use for terminal topics

    public void updateAlertToggle(String userId, String roomName, boolean noiseAlert, boolean tempAlert, boolean humAlert) throws SQLException {
        String updateQuery = String.format("UPDATE ROOM SET noise_alerts=%s, temp_alerts=%s,hum_alerts=%s" +
                " WHERE userid='%s' AND room_name='%s'",noiseAlert,tempAlert,humAlert,userId,roomName);
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

    public ResultSet recieveRecordDates(String userId, String roomName) throws SQLException {
        String sqlQuery = String.format("SELECT DISTINCT record_date FROM RECORD WHERE userid='%s' AND room_name='%s' ORDER BY record_date DESC",userId,roomName);
        ResultSet rs = statement.executeQuery(sqlQuery);
        return rs;
    } //return only one instance of each unique recorded date for the room.

    public ResultSet recieveRecordedData(String userId, String roomName, String recordDate) throws SQLException {
        String sqlQuery = String.format("SELECT record_time, loud_data, temp_data, hum_data FROM RECORD" +
                " WHERE userid='%s' AND room_name='%s' AND record_date='%s' ORDER BY record_time", userId,roomName,recordDate);
        ResultSet rs = statement.executeQuery(sqlQuery);
        return rs;
    } //return data stored in the db

    public ResultSet retrieveSystemNotificationSettings(String userId) throws SQLException {
        String sqlQuery = String.format("SELECT noise_alert_setting, temp_alert_setting, hum_alert_setting FROM USERS WHERE id='%s'", userId);
        ResultSet rs = statement.executeQuery(sqlQuery);
        return rs;
    } //return the stored notification settings for the users system

    public void updateSystemNotificationSettings(String userId, boolean noiseAlert, boolean tempAlert, boolean humAlert) throws SQLException {
        String updateQuery = String.format("UPDATE USERS SET noise_alert_setting=%s, temp_alert_setting=%s,hum_alert_setting=%s" +
                " WHERE id='%s'",noiseAlert,tempAlert,humAlert,userId);
        statement.executeUpdate(updateQuery); //update preferences for receiving notifications
    }

}
