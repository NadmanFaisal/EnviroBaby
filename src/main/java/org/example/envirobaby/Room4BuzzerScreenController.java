package org.example.envirobaby;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;

public class Room4BuzzerScreenController {
    public Button room1;
    public Button room2;
    public Button room4;
    public Button room3;
    public Button playSoundButton;
    public Button stopSoundButton;
    private MQTTSender sender;

    public void room1BuzzerScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("room1BuzzerScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void room2BuzzerScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("room2BuzzerScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void room3BuzzerScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("room3BuzzerScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void room4BuzzerScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("room4BuzzerScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
    public void playSound(ActionEvent actionEvent) throws MqttException, InterruptedException {
        sender = new MQTTSender();
        sender.sendMessage("BUZZ", "envirobaby/room4/buzzer");
    }

    public void stopSound(ActionEvent actionEvent) {
    }

}
