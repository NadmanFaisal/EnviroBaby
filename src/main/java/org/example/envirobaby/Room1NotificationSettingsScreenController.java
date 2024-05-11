package org.example.envirobaby;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Room1NotificationSettingScreenController {

    @FXML
    private Button homeButtonClick;
    @FXML
    private Button tempTurnOFFButton;

    @FXML
    private Button tempTurnONButton;

    @FXML
    private Button humiTurnOFFButton;

    @FXML
    private Button humiTurnONButton;

    @FXML
    private Button noiseTurnOFFButton;

    @FXML
    private Button room1NotificationSettings;

    @FXML
    private Button room2NotificationSettings;

    @FXML
    private Button room3NotificationSettings;

    @FXML
    private Button room4NotificationSettings;



    @FXML
    void temperatureTurnOFFButton(ActionEvent event) {

    }

    @FXML
    void temperatureTurnONButton(ActionEvent event) {

    }

    @FXML
    void HumidityTurnONButton(ActionEvent event) {

    }

    @FXML
    void NoiseTurnONButton(ActionEvent event) {

    }

    @FXML
    void Room1NotificationSettingScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("room1NotificationSettingsScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    void Room2NotificationSettingScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("room2NotificationSettingsScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);

    @FXML
    void Room3NotificationSettingScreen(ActionEvent event) throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("room3NotificationSettingsScreen.fxml"));
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

    @FXML
    void Room4NotificationSettingScreen(ActionEvent event) throws IOException {
                Parent root = FXMLLoader.load(getClass().getResource("room4NotificationSettingsScreen.fxml"));
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);

    @FXML
    void homeButtonClick(ActionEvent event) { throws IOException {
                    Parent root = FXMLLoader.load(getClass().getResource("homeScreen.fxml"));
                    Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);

    }



}