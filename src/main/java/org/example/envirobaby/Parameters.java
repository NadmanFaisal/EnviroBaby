package org.example.envirobaby;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Parameters {
    private Label noiseLabel;
    private Label tempLabel;
    private Label humLabel;

    public Parameters(Label noiseLabel, Label tempLabel, Label humLabel) {
        this.noiseLabel = noiseLabel;
        this.tempLabel = tempLabel;
        this.humLabel = humLabel;
    }

    public void updateLabel(Label label, String message) {
        if (label != null && message != null) {
            Platform.runLater(() -> label.setText(message));
        }
    }

    public Label getNoiseLabel() {
        return this.noiseLabel;
    }

    public Label getHumLabel() {
        return this.humLabel;
    }

    public Label getTempLabel() {
        return this.tempLabel;
    }
}
