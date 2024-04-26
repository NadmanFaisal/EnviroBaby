package org.example.envirobaby;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller {

    @FXML
    private Label noiseLabel;

    @FXML
    private Label tempLabel;

    @FXML
    private Label humLabel;

    private MQTTSubscriber mqttSubscriber;

    @FXML
    public void initialize() { //Creates new subscriber object
        mqttSubscriber = new MQTTSubscriber(noiseLabel, tempLabel, humLabel);

    }}


