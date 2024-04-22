package org.example.envirobaby;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller {

    @FXML
    private Label noiseLabel;
    private MQTTSubscriber mqttSubscriber;

    @FXML
    public void initialize() { //Creates new subscriber object
        mqttSubscriber = new MQTTSubscriber(noiseLabel);
    }}


