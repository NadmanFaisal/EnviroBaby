package org.example.envirobaby;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;

public class HomeScreenController {

    @FXML
    private Button findMe;
    @FXML
    private Button homebutton;
    @FXML
    private Button notificationSettings;

    private MQTTSender sender;

    /**
     * This method will switch to the room1BuzzerScreen when the button
     * "Find me" is clicked with a mouse.
     * @param event
     * @throws IOException
     */
    @FXML
    void findMeClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("room1BuzzerScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    void loadHomeScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("homeScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);

    }

    @FXML
    void notificationSettingsClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("notificationSettingsScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);

    }

}
