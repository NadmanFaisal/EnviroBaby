package org.example.envirobaby;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import java.io.IOException;

public class Room4NotificationSettingScreenController {

    @FXML
    private Button Room1;

    @FXML
    private Button Room2;

    @FXML
    private Button Room3;

    @FXML
    private Button Room4;

    @FXML
    private ToggleButton ToggleHumiditybutton;

    @FXML
    private ToggleButton ToggleNoisebutton;

    @FXML
    private ToggleButton ToggleTemperatureButton;

    @FXML
    void Room1NotificationSettingsScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("room1NotificationSettingsScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    void Room2NotificationSettingsScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("room2NotificationSettingsScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    void Room3NotificationSettingsScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("room3NotificationSettingsScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
    @FXML
    void Room4NotificationSettingsScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("room4NotificationSettingsScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
    @FXML
    void Room4ToggleTemperaturebutton(ActionEvent event) {
        // Toggle the temperature settings for Room 4
        boolean isActive = ToggleTemperatureButton.isSelected();
        System.out.println("Room 4 Temperature Notifications: " + (isActive ? "ON" : "OFF"));
        // Enable/disable the temperature notifications
    }
    @FXML
    void Room4ToggleHumiditybutton(ActionEvent event) {
        // Toggle the humidity settings for Room 4
        boolean isActive = ToggleHumiditybutton.isSelected();
        System.out.println("Room 4 Humidity Notifications: " + (isActive ? "ON" : "OFF"));
        // Enable/disable the humidity notifications
    }

    @FXML
    void Room4ToggleNoisebutton(ActionEvent event) {
        // Toggle the noise settings for Room 4
        boolean isActive = ToggleNoisebutton.isSelected();
        System.out.println("Room 4 Noise Notifications: " + (isActive ? "ON" : "OFF"));
        // Enable/disable the noise notifications
    }

}
