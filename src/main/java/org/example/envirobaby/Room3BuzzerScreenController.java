package org.example.envirobaby;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Room3BuzzerScreenController {
    public Button room1;
    public Button room2;
    public Button room4;
    public Button room3;
    public Button playSoundButton;
    public Button stopSoundButton;

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
    public void playSound(ActionEvent actionEvent) {
    }

    public void stopSound(ActionEvent actionEvent) {
    }

}
