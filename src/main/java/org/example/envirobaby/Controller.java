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
    private TextField maxTempBox;
    @FXML
    private TextField minTempBox;

    private MQTTSubscriber mqttSubscriber;

    @FXML
    public void initialize() { //Creates new subscriber object

        mqttSubscriber = new MQTTSubscriber( noiseLabel,  tempLabel,  humLabel,  maxNoise,  maxTempBox,  minTempBox,  minHumBox,  maxHumBox);
        maxNoise.setText(String.valueOf(mqttSubscriber.getNoiseThreshold()));
        maxTempBox.setText(String.valueOf(mqttSubscriber.getTempUbound()));
        minTempBox.setText(String.valueOf(mqttSubscriber.getTempLbound()));
    }

    @FXML
    public void updateNoiseUpperBound(ActionEvent actionEvent){ // controller class method used in FXML file that handles and action event
    mqttSubscriber.updateNoiseThreshold();
    }
    @FXML
    public void updateTempUbound(ActionEvent actionEvent) {mqttSubscriber.updateTempUbound();}
    @FXML
    public void updateTempLbound(ActionEvent actionEvent) {mqttSubscriber.updateTempLbound();}

}


