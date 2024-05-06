package org.example.envirobaby.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.envirobaby.MQTTSender;

import java.io.IOException;

public class HomeScreenController {

    @FXML
    private Button findMe;
    private MQTTSender sender;

    @FXML
    private void roomViewClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("room_1.fxml"));
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

}
