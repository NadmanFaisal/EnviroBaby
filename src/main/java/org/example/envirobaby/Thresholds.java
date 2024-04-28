package org.example.envirobaby;

import javafx.scene.control.TextField;

public class Thresholds {
    private TextField maxNoise; // reference to textField object
    private TextField maxTempBox;
    private TextField minTempBox;
    private TextField maxHumBox;
    private TextField minHumBox;

    private double tempUbound;
    private double tempLbound;
    private double minHum; // stores latest input for minimum humidity threshold
    private double maxHum; // stores latest input for maximum humidity threshold
    private int noiseThreshold;
    private DataProcessor processor;

    public Thresholds(TextField maxNoise, TextField maxTempBox, TextField minTempBox, TextField maxHumBox, TextField minHumBox) {
        this.processor = new DataProcessor();

        this.maxNoise = maxNoise;
        this.maxTempBox = maxTempBox;
        this.minTempBox = minTempBox;
        this.maxHumBox = maxHumBox;
        this.minHumBox = minHumBox;

        this.noiseThreshold = 90;
        this.tempUbound = 25.00;
        this.tempLbound = 18.00;
        this.maxHum = 60;
        this.minHum = 30;
    }

    public void updateNoiseThreshold(String message) { // method that updates the threshold value
        this.noiseThreshold = processor.extractNumber(message);
    }

    public void updateTempUbound(String message) { // method that updates the threshold value
        this.tempUbound = processor.extractDouble(message);
    }
    public void updateTempLbound(String message) {
        this.tempLbound = processor.extractDouble(message);
    }

    public void updateMinHum(String message) { // Updates minimum humidity threshold
        this.minHum = processor.extractDouble(message);
    }

    public void updateMaxHum(String message) { // Updates maximum humidity threshold
        this.maxHum = processor.extractDouble(message);
    }

    public double getMinHum(){ //getter for minimum humidity threshold
        return this.minHum;
    }

    public double getMaxHum(){ //getter for maximum humidity treshold
        return this.maxHum;
    }

    public int getNoiseThreshold() { // getter method to receive threshold value
        return this.noiseThreshold;
    }

    public double getTempUbound() { // getter method to receive threshold value
        return this.tempUbound;
    }
    public double getTempLbound() {
        return this.tempLbound;
    }
    public TextField getMaxNoise() {
        return this.maxNoise;
    }

    public TextField getMaxTempBox() {
        return this.maxTempBox;
    }

    public TextField getMinTempBox() {
        return this.minTempBox;
    }

    public TextField getMaxHumBox() {
        return this.maxHumBox;
    }

    public TextField getMinHumBox() {
        return this.minHumBox;
    }

}
