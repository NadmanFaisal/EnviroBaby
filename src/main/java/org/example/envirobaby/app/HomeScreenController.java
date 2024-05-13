package org.example.envirobaby.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.example.envirobaby.MQTTSender;
import org.example.envirobaby.Room;
import org.example.envirobaby.UserExchanger;

import java.io.IOException;
import java.util.HashMap;

public class HomeScreenController {

    @FXML
    private Button findMe;
    private MQTTSender sender;
    @FXML
    private Button notificationSettings;

    private UserExchanger instanceUser;
    private HashMap<Integer,Room> userRooms;

    @FXML
    public void initialize() throws MqttException {
        instanceUser = UserExchanger.getInstance();
        Thread thread = new Thread(instanceUser.getInstanceUser());
        thread.start();



    }

    @FXML
    private void roomViewClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("roomOverview.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    private void findMeClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("room1BuzzerScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    private void AddRoomsClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("addRooms.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    private void ViewRoomsClick(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("viewRooms.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
    @FXML
    void notificationSettingsClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("systemNotificationSettingsScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

}

