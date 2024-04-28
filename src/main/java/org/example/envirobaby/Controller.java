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
    @FXML
    private TextField maxTempBox;
    @FXML
    private TextField minTempBox;


    private MQTTSubscriber mqttSubscriber;

    private Parameters parameters;
    private Thresholds thresholds;

    @FXML
    public void initialize() { //Creates new subscriber object

        mqttSubscriber = new MQTTSubscriber( noiseLabel,  tempLabel,  humLabel,  maxNoise,  maxTempBox,  minTempBox,  minHumBox,  maxHumBox);
        parameters = new Parameters(noiseLabel, tempLabel, humLabel);
        thresholds = new Thresholds(maxNoise, maxTempBox, minTempBox, maxHumBox, minHumBox);

        maxNoise.setText(String.valueOf(thresholds.getNoiseThreshold()));
        minHumBox.setText(String.valueOf(thresholds.getMinHum()));
        maxHumBox.setText(String.valueOf(thresholds.getMaxHum()));
        maxTempBox.setText(String.valueOf(thresholds.getTempUbound()));
        minTempBox.setText(String.valueOf(thresholds.getTempLbound()));
    }

    @FXML
    public void updateNoiseUpperBound(ActionEvent actionEvent){ // controller class method used in FXML file that handles and action event
        thresholds.updateNoiseThreshold(this.maxNoise.getText());
    }
    @FXML
    public void updateTempUbound(ActionEvent actionEvent) {
        thresholds.updateTempUbound(this.maxTempBox.getText());
    }
    @FXML
    public void updateTempLbound(ActionEvent actionEvent) {
        thresholds.updateTempLbound(this.minTempBox.getText());
    }

    @FXML
    public void updateMinHum(ActionEvent actionEvent) {
        thresholds.updateMinHum(this.minHumBox.getText());
    }

    @FXML
    public void updateMaxHum (ActionEvent actionEvent) {
        thresholds.updateMaxHum(this.maxHumBox.getText());
    }

}

