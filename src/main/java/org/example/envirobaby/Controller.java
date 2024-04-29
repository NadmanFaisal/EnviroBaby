package org.example.envirobaby;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.eclipse.paho.client.mqttv3.MqttException;

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
        room.updateThreshold(maxTempBox);;
    }
    @FXML
    public void updateTempLbound(ActionEvent actionEvent) {
        room.updateThreshold(minTempBox);;
    }

    @FXML
    public void updateMinHum(ActionEvent actionEvent) {
        room.updateThreshold(maxHumBox);;
    }

    @FXML
    public void updateMaxHum (ActionEvent actionEvent) {
        room.updateThreshold(minHumBox);;
    }

}

