package org.example.envirobaby;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    private Label noiseLabel;
    @FXML
    private TextField upperBoundBox;
    @FXML
    private Label tempLabel;
    @FXML
    private Label humLabel;

    private MQTTSubscriber mqttSubscriber;

    @FXML
    public void initialize() { //Creates new subscriber object

        mqttSubscriber = new MQTTSubscriber(noiseLabel, tempLabel, humLabel, upperBoundBox);
        upperBoundBox.setText(String.valueOf(mqttSubscriber.getThreshold()));
    }

    @FXML
    public void updateUpperBound(ActionEvent actionEvent){ // controller class method used in FXML file that handles and action event
    mqttSubscriber.updateNoiseThreshold();
    }

}


