package org.example.envirobaby;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Room implements Runnable {

    private Label noiseLabel;
    private Label tempLabel;
    private Label humLabel;
    private NotificationThreshold thresholds;
    private ParameterData sensorReadings;
    private MQTTReceiver client;
    private Notification alerts;
    private MQTTSender sender;
    private boolean isFahrenheit;


    DecimalFormat df = new DecimalFormat("#.00");

    public Room(Label noiseLabel, Label tempLabel, Label humLabel) throws MqttException, InterruptedException {
        this.sender = new MQTTSender();
        this.thresholds = new NotificationThreshold();
        this.client = new MQTTReceiver();
        this.sensorReadings = client.getReadings();
        this.alerts= new Notification();
        this.noiseLabel=noiseLabel;
        this.tempLabel=tempLabel;
        this.humLabel=humLabel;
        this.isFahrenheit = false;
    }



    private void updateOverview() {

        String tempMsg;
        if (isFahrenheit) {
            tempMsg = df.format(sensorReadings.getTempValue()) + " F";
        } else {
            tempMsg = df.format(sensorReadings.getTempValue()) + " C";
        }

        String humMsg = df.format(sensorReadings.getHumValue()) +" %";
        String noiseMsg = sensorReadings.getLoudValue() + " db";

        updateLabel(tempLabel,tempMsg);
        updateLabel(noiseLabel, noiseMsg);
        updateLabel(humLabel,humMsg);
    }

    private void sendAlerts(){
        int noiseLvl = sensorReadings.getLoudValue();
        double tempLvl = sensorReadings.getTempValue();
        double humLvl = sensorReadings.getHumValue();

        //only send notifications if above/below threshold AND if it isn't the same value as the last sent notification to avoid duplicates
        if (noiseLvl > thresholds.getLoudThreshold() && alerts.getLastNoiseAlert()!=noiseLvl && alerts.isNoiseNotifOn()) {
            alerts.createNotification("Noise notification", "NOISE THRESHOLD CROSSED: " + noiseLvl + " db");
            alerts.setLastNoiseAlert(noiseLvl);
        }

        if (isFahrenheit) {
            if (tempLvl > thresholds.getTempUpperBound() && alerts.getLastMaxTempAlert()!=tempLvl && alerts.isTempNotifOn() ) {
                alerts.createNotification("Temperature notification", "TEMPERATURE EXCEEDS THRESHOLD: " + df.format(tempLvl) + " F");
                alerts.setLastMaxTempAlert(tempLvl);
            } else if (tempLvl < thresholds.getTempLowerBound() && alerts.getLastMinTempAlert()!=tempLvl && alerts.isTempNotifOn()) {
                alerts.createNotification("Temperature notification", "TEMPERATURE BELOW THRESHOLD: " + df.format(tempLvl) + " F");
                alerts.setLastMinTempAlert(tempLvl);
            }
        } else {
            if (tempLvl > thresholds.getTempUpperBound() && alerts.getLastMaxTempAlert()!=tempLvl && alerts.isTempNotifOn()) {
                alerts.createNotification("Temperature notification", "TEMPERATURE EXCEEDS THRESHOLD: " + df.format(tempLvl) + " C");
                alerts.setLastMaxTempAlert(tempLvl);
            } else if (tempLvl < thresholds.getTempLowerBound() && alerts.getLastMinTempAlert()!=tempLvl && alerts.isTempNotifOn()) {
                alerts.createNotification("Temperature notification", "TEMPERATURE BELOW THRESHOLD: " + df.format(tempLvl) + " C");
                alerts.setLastMinTempAlert(tempLvl);
            }
        }

        if (humLvl > thresholds.getHumUpperBound() && alerts.getLastMaxHumAlert()!=humLvl && alerts.isHumiNotifOn()) {
            alerts.createNotification("Humidity notification", "HUMIDITY EXCEEDS THRESHOLD: " + df.format(humLvl) + " %");
            alerts.setLastMaxHumAlert(humLvl);
        } else if (humLvl < thresholds.getHumLowerBound() && alerts.getLastMinHumAlert()!=humLvl && alerts.isHumiNotifOn()) {
            alerts.createNotification("Humidity notification", "HUMIDITY BELOW THRESHOLD: " + df.format(humLvl) + " %");
            alerts.setLastMinHumAlert(humLvl);
        }
    }


    private void updateLabel(Label label, String message) {
        if (label != null && message != null) {
            Platform.runLater(() -> label.setText(message));
        }
    }

    public void updateThreshold(TextField textField) { // method that updates the threshold value
        String thresholdTextValue = textField.getText(); // gets and stores the string value from textField

        if (textField.getId().equals("maxNoise")) {
            if (thresholdTextValue.matches("\\d+")) { //condition to find if there are any numeric value
                thresholds.setLoudThreshold(Integer.parseInt(thresholdTextValue)); // converts the string into integer
            } else {
                System.out.println("Enter a numeric value, Thank you!"); // if no numeric value id found this is printed
            }
        } else {
            if (thresholdTextValue.matches("[0-9]+(\\.[0-9][0-9]?)?")) { //checks for double using Regex
                switch (textField.getId()) {
                    case "maxTemp":
                        thresholds.setTempUpperBound(Double.parseDouble(thresholdTextValue));
                        break;
                    case "minTemp":
                        thresholds.setTempLowerBound(Double.parseDouble(thresholdTextValue));
                        break;
                    case "maxHum":
                        thresholds.setHumUpperBound(Double.parseDouble(thresholdTextValue));
                        break;
                    case "minHum":
                        thresholds.setHumLowerBound(Double.parseDouble(thresholdTextValue));
                        break;
                }
            } else {
                System.out.println("Enter a numeric value, Thank you!");
            }
        }
    }


    public NotificationThreshold getThresholds() {
        return thresholds;
    }
    public ParameterData getSensorReadings() {
        return sensorReadings;
    }
    public Notification getAlerts() {
        return alerts;
    }

    @Override
    public void run() {
        // Create a ScheduledExecutorService for overviews
        ScheduledExecutorService overviewScheduler = Executors.newSingleThreadScheduledExecutor();

        // Schedule updateOverview with a delay
        overviewScheduler.scheduleAtFixedRate(this::updateOverview, 0, 100, TimeUnit.MILLISECONDS);

        // Create a ScheduledExecutorService for notifications
        ScheduledExecutorService notificationScheduler = Executors.newSingleThreadScheduledExecutor();
        //Schedule sendAlerts with initial delay of 101 to avoid notifications during initialisation
        notificationScheduler.scheduleAtFixedRate(this::sendAlerts, 101, 100, TimeUnit.MILLISECONDS);

    }

    public void sendCelsiusMsg(Button actionEvent) throws MqttException, InterruptedException {
        sender.sendMessage("C", "/envirobaby/tempunit");
        isFahrenheit = false;
    }

    public void sendFahrenMsg(Button actionEvent) throws MqttException, InterruptedException {
        sender.sendMessage("F", "/envirobaby/tempunit");
        isFahrenheit = true;
    }


}
