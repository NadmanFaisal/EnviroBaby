package org.example.envirobaby;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;

public class Controller {

    @FXML
    private Label noiseLabel;
    @FXML
    private TextField maxNoise;
    @FXML
    private Label tempLabel;
    @FXML
    private Label humLabel;
    @FXML
    private TextField minHumBox;
    @FXML
    private TextField maxHumBox;
    @FXML
    private TextField maxTempBox;
    @FXML
    private TextField minTempBox;
    @FXML
    private Button celciusButton;
    @FXML
    private Button fahrenButton;




    private Room room;


    @FXML
    public void initialize() throws MqttException { //Creates new subscriber object

        room = new Room(noiseLabel,tempLabel,humLabel); //initialise room object which implements runnable
        Thread thread = new Thread(room); //connect runnable to thread

        //initialize the data threshold boxes with stored room threshold data

        maxNoise.setText(String.valueOf(room.getThresholds().getLoudThreshold()));
        minHumBox.setText(String.valueOf(room.getThresholds().getHumLowerBound()));
        maxHumBox.setText(String.valueOf(room.getThresholds().getHumUpperBound()));
        maxTempBox.setText(String.valueOf(room.getThresholds().getTempUpperBound()));
        minTempBox.setText(String.valueOf(room.getThresholds().getTempLowerBound()));

        thread.start(); //start thread
    }

    @FXML
    public void updateNoiseUpperBound(ActionEvent actionEvent){ // controller class method used in FXML file that handles and action event
        room.updateThreshold(maxNoise);
    }
    @FXML
    public void updateTempUbound(ActionEvent actionEvent) {
        room.updateThreshold(maxTempBox);
    }
    @FXML
    public void updateTempLbound(ActionEvent actionEvent) {
        room.updateThreshold(minTempBox);
    }

    @FXML
    public void updateMinHum(ActionEvent actionEvent) {
        room.updateThreshold(minHumBox);
    }

    @FXML
    public void updateMaxHum (ActionEvent actionEvent) {
        room.updateThreshold(maxHumBox);
    }

    @FXML
    public void convertToCelsius (ActionEvent actionEvent) throws MqttException, InterruptedException {
        room.sendCelsiusMsg(celciusButton);
    }

    @FXML
    public void convertToFahrenheit (ActionEvent actionEvent) throws MqttException, InterruptedException {
        room.sendFahrenMsg(fahrenButton);
    }

    public void homeButtonClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("homeScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}

