package org.example.envirobaby.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.example.envirobaby.MQTTSender;

import java.io.IOException;

public class Room4BuzzerScreenController {
    public Button room1;
    public Button room2;
    public Button room4;
    public Button room3;
    public Button playSoundButton;
    public Button stopSoundButton;
    private MQTTSender sender;

    /**
     * Upon clicking with a mouse on the button "Room 1",
     * the screen switches to the Room 1 screen.
     * @param event
     * @throws IOException
     */

    public void homeButtonClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("homeScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void room1BuzzerScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("room1BuzzerScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    /**
     * Upon clicking with a mouse on the button "Room 2",
     * the screen switches to the Room 2 screen.
     * @param event
     * @throws IOException
     */
    public void room2BuzzerScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("room2BuzzerScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    /**
     * Upon clicking with a mouse on the button "Room 3",
     * the screen switches to the Room 3 screen.
     * @param event
     * @throws IOException
     */
    public void room3BuzzerScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("room3BuzzerScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    /**
     * Upon clicking with a mouse on the button "Room 4",
     * the screen switches to the Room 4 screen.
     * @param event
     * @throws IOException
     */
    public void room4BuzzerScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("room4BuzzerScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    /**
     * When the "Play sound" button is clicked, it publishes
     * message "BUZZ" message to certain topic so that the
     * WIO terminal starts buzzing.
     * @param actionEvent
     * @throws MqttException
     * @throws InterruptedException
     */
    public void playSound(ActionEvent actionEvent) throws MqttException, InterruptedException {
        sender = new MQTTSender();
        sender.sendMessage("BUZZ", "/envirobaby/room4/buzzer");
    }

    /**
     * When the "Stop sound" button is clicked, it publishes
     * message "STOP" message to certain topic so that the
     * WIO terminal stops buzzing.
     * @param actionEvent
     * @throws MqttException
     * @throws InterruptedException
     */
    public void stopSound(ActionEvent actionEvent) throws MqttException, InterruptedException {
        sender = new MQTTSender();
        sender.sendMessage("STOP", "/envirobaby/room4/buzzer");
    }

}
