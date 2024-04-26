package org.example.envirobaby;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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

    private MQTTSubscriber mqttSubscriber;

    @FXML
    public void initialize() { //Creates new subscriber object

        mqttSubscriber = new MQTTSubscriber(noiseLabel, tempLabel, humLabel, maxNoise, minHumBox, maxHumBox);
        maxNoise.setText(String.valueOf(mqttSubscriber.getNoiseThreshold()));
        minHumBox.setText(String.valueOf(mqttSubscriber.getMinHum()));
        maxHumBox.setText(String.valueOf(mqttSubscriber.getMaxHum()));
    }

    @FXML
    public void updateUpperBound(ActionEvent actionEvent){ // controller class method used in FXML file that handles and action event
    mqttSubscriber.updateNoiseThreshold();
    }

    @FXML
    public void updateMinHum(ActionEvent actionEvent) { mqttSubscriber.updateMinHum(); }

    public void updateMaxHum (ActionEvent actionEvent) {mqttSubscriber.updateMaxHum();}

}

