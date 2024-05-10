package org.example.envirobaby;

import javafx.application.Platform;
import javafx.scene.control.Label;
import org.example.envirobaby.app.UserExchanger;

import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OverviewManager implements Runnable{
    private Label noiseLabel;
    private Label tempLabel;
    private Label humLabel;

    private Notification alerts;
    private Room room;



    DecimalFormat df = new DecimalFormat("#.00");


    public OverviewManager(Label noiseLabel, Label tempLabel, Label humLabel, Room userRoom){

        this.room = userRoom;
        this.alerts= new Notification();


        this.noiseLabel=noiseLabel;
        this.tempLabel=tempLabel;
        this.humLabel=humLabel;
    }


    private void updateOverview() { //show sensor data on room overview page
        String noiseMsg = room.getSensorReading().getLoudValue() + " db";
        String tempMsg = df.format(room.getSensorReading().getTempValue()) + "C";
        String humMsg = df.format(room.getSensorReading().getHumValue()) +"%";

        updateLabel(noiseLabel, noiseMsg);
        updateLabel(tempLabel,tempMsg);
        updateLabel(humLabel,humMsg);
    }

    private void sendAlerts(){
        int noiseLvl = room.getSensorReading().getLoudValue();
        double tempLvl = room.getSensorReading().getTempValue();
        double humLvl = room.getSensorReading().getTempValue();

        //only send notifications if above/below threshold AND if it isn't the same value as the last sent notification to avoid duplicates
        if (noiseLvl > room.getThresholds().getLoudThreshold() && alerts.getLastNoiseAlert()!=noiseLvl) {
            alerts.createNotification("Noise notification", "NOISE THRESHOLD CROSSED: " + noiseLvl + " db");
            alerts.setLastNoiseAlert(noiseLvl);
        }

        if (tempLvl >  room.getThresholds().getTempUpperBound() && alerts.getLastMaxTempAlert()!=tempLvl) {
            alerts.createNotification("Temperature notification", "TEMPERATURE EXCEEDS THRESHOLD: " + df.format(tempLvl) + "C");
            alerts.setLastMaxTempAlert(tempLvl);
        } else if (tempLvl <  room.getThresholds().getTempLowerBound() && alerts.getLastMinTempAlert()!=tempLvl) {
            alerts.createNotification("Temperature notification", "TEMPERATURE BELOW THRESHOLD: " + df.format(tempLvl) + "C");
            alerts.setLastMinTempAlert(tempLvl);
        }

        if (humLvl >  room.getThresholds().getHumUpperBound() && alerts.getLastMaxHumAlert()!=humLvl) {
            alerts.createNotification("Humidity notification", "HUMIDITY EXCEEDS THRESHOLD: " + df.format(humLvl) + "%");
            alerts.setLastMaxHumAlert(humLvl);
        } else if (humLvl <  room.getThresholds().getHumLowerBound() && alerts.getLastMinHumAlert()!=humLvl) {
            alerts.createNotification("Humidity notification", "HUMIDITY BELOW THRESHOLD: " + df.format(humLvl) + "%");
            alerts.setLastMinHumAlert(humLvl);
        }
    }


    private void updateLabel(Label label, String message) {
        if (label != null && message != null) {
            Platform.runLater(() -> label.setText(message));
        }
    }

    public Room getUserRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
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
}
