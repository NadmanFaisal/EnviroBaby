package org.example.envirobaby.app;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.example.envirobaby.OverviewManager;
import org.example.envirobaby.Room;
import org.example.envirobaby.User;

import java.io.IOException;
import java.sql.SQLException;

public class RoomController {

    @FXML
    private Label noiseLabel;
    @FXML
    private TextField maxNoise;
    @FXML
    private Label tempLabel;
    @FXML
    private Label humLabel;
    @FXML
    private TextField minHumBox;
    @FXML
    private TextField maxHumBox;
    @FXML
    private TextField maxTempBox;
    @FXML
    private TextField minTempBox;
    @FXML
    private Label roomCapLabel;


    private OverviewManager roomOverview;
    private UserExchanger instanceUser;
    private Room currentRoom;
    private User currentUser;


    @FXML
    public void initialize() throws MqttException { //Creates new subscriber object
        instanceUser= UserExchanger.getInstance();
        currentUser = instanceUser.getInstanceUser();
        currentRoom = instanceUser.getCurrentRoom();

        roomOverview = new OverviewManager(noiseLabel,tempLabel,humLabel, currentRoom); //initialise room object which implements runnable
        Thread thread = new Thread(roomOverview); //connect runnable to thread

        //initialize the data threshold boxes with stored room threshold data

        maxNoise.setText(String.valueOf(roomOverview.getUserRoom().getThresholds().getLoudThreshold()));
        minHumBox.setText(String.valueOf(roomOverview.getUserRoom().getThresholds().getHumLowerBound()));
        maxHumBox.setText(String.valueOf(roomOverview.getUserRoom().getThresholds().getHumUpperBound()));
        maxTempBox.setText(String.valueOf(roomOverview.getUserRoom().getThresholds().getTempUpperBound()));
        minTempBox.setText(String.valueOf(roomOverview.getUserRoom().getThresholds().getTempLowerBound()));
        roomCapLabel.setText("Capacity: " + currentRoom.getCapacity());

        thread.start(); //start thread
    }

    @FXML
    public void updateNoiseUpperBound(ActionEvent actionEvent) throws SQLException { // controller class method used in FXML file that handles and action event
        roomOverview.getUserRoom().updateThreshold(maxNoise);
    }
    @FXML
    public void updateTempUbound(ActionEvent actionEvent) throws SQLException {
        roomOverview.getUserRoom().updateThreshold(maxTempBox);;
    }
    @FXML
    public void updateTempLbound(ActionEvent actionEvent) throws SQLException {
        roomOverview.getUserRoom().updateThreshold(minTempBox);;
    }

    @FXML
    public void updateMinHum(ActionEvent actionEvent) throws SQLException {
        roomOverview.getUserRoom().updateThreshold(minHumBox);
    }

    @FXML
    public void updateMaxHum (ActionEvent actionEvent) throws SQLException {
        roomOverview.getUserRoom().updateThreshold(maxHumBox);
    }

    public void homeButtonClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("homeScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}

