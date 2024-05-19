package org.example.envirobaby.Interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.example.envirobaby.MQTT.MQTTSender;
import org.example.envirobaby.Entity.Room;
import org.example.envirobaby.Entity.UserExchanger;

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
        instanceUser.getInstanceUser().runBackgroundFunctions(); // start executor services for notifications and passive data recording

    }

    @FXML
    private void findMeClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("buzzerScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.resizableProperty().setValue(false);
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    private void AddRoomsClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("addRooms.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.resizableProperty().setValue(false);
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
    @FXML
    private void editRoomsClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("editRooms.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.resizableProperty().setValue(false);
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    private void ViewRoomsClick(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("viewRooms.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.resizableProperty().setValue(false);
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
    @FXML
    void notificationSettingsClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("systemNotificationSettingsScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.resizableProperty().setValue(false);
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

}

