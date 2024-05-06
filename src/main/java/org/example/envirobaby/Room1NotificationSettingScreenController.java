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

public class Room1NotificationSettingScreenController {

    @FXML
    private Button Room1;

    @FXML
    private Button Room2;

    @FXML
    private Button Room3;

    @FXML
    private Button Room4;

    @FXML
    private ToggleButton ToggleHumidityButton;

    @FXML
    private ToggleButton ToggleNoiseButton;

    @FXML
    private ToggleButton ToggleTemperatureButton;

    private Room room;



    public void loadRoom1SettingsScreen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("room1NotificationSettingsScreen.fxml"));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void loadRoom2SettingsScreen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("room2NotificationSettingsScreen.fxml"));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void loadRoom3SettingsScreen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("room3NotificationSettingsScreen.fxml"));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void loadRoom4SettingsScreen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("room4NotificationSettingsScreen.fxml"));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
    @FXML
    void Room1ToggleTemperaturebutton(ActionEvent event) {
        // Toggle the temperature settings for Room 1
        boolean isActive = ToggleTemperatureButton.isSelected();
        System.out.println("Room 1 Temperature Notifications: " + (isActive ? "ON" : "OFF"));
        // Enable/disable the humidity notifications
    }
    @FXML
    void Room1ToggleHumiditybutton(ActionEvent event) {
        // Toggle the humidity settings for Room 1
        boolean isActive = ToggleHumidityButton.isSelected();
        System.out.println("Room 1 Humidity Notifications: " + (isActive ? "ON" : "OFF"));

        // Enable/disable the humidity notifications
    }

    @FXML
    void Room1ToggleNoisebutton(ActionEvent event) {
        // Toggle the humidity settings for Room 1
        boolean isActive = ToggleNoiseButton.isSelected();
        System.out.println("Room 1 Noise Notifications: " + (isActive ? "ON" : "OFF"));
        // Enable/disable the humidity notifications
    }

}

