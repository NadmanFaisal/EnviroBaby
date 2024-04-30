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
    private MQTTSender sender;

    @FXML
    void findMeClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("room1BuzzerScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

}
