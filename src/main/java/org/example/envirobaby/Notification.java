package org.example.envirobaby;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Notification {

    private int lastNoiseAlert;
    private double lastMaxTempAlert;
    private double lastMinTempAlert;
    private double lastMaxHumAlert;
    private double lastMinHumAlert;

    private boolean tempNotifOn;
    private boolean humiNotifOn;
    private boolean noiseNotifOn;

    DecimalFormat df = new DecimalFormat("#.00");

    public Notification() {
        this.lastNoiseAlert=0;
        this.lastMaxTempAlert=0;
        this.lastMinTempAlert=0;
        this.lastMaxHumAlert=0;
        this.lastMinHumAlert=0;

        this.noiseNotifOn=true;
        this.tempNotifOn=true;
        this.humiNotifOn=true;


    }

    /**
     * A method what will create notifications when
     * any environmental parameter is breached. It creates
     * notifications for temperature, humidity, and noise
     * threshold breaches.
     * @param title
     * The title of the notification
     * @param message
     * The message the notification will  show once it appears.
     */
    public void createNotification(String title, String message) {
        Platform.runLater(() -> {

                // Source for the code:- https://javadoc.io/doc/org.controlsfx/controlsfx/8.40.16/org/controlsfx/control/Notifications.html
                Notifications notifications = Notifications.create().title(title).text(message).graphic(null).hideAfter(Duration.seconds(7)).position(Pos.BOTTOM_RIGHT);
                notifications.showWarning();
        });
    }

    public void setLastNoiseAlert(int lastNoiseAlert) {
        this.lastNoiseAlert = lastNoiseAlert;
    }
    public void setLastMaxTempAlert(double lastMaxTempAlert) {
        this.lastMaxTempAlert = lastMaxTempAlert;
    }
    public void setLastMinTempAlert(double lastMinTempAlert) {
        this.lastMinTempAlert = lastMinTempAlert;
    }
    public void setLastMaxHumAlert(double lastMaxHumAlert) {
        this.lastMaxHumAlert = lastMaxHumAlert;
    }
    public void setLastMinHumAlert(double lastMinHumAlert) {
        this.lastMinHumAlert = lastMinHumAlert;
    }
    public void setTempNotifOn(boolean tempNotifOn){ this.tempNotifOn=tempNotifOn;}
    public void setHumiNotifOn(boolean humiNotifOn) {
        this.humiNotifOn = humiNotifOn;
    }
    public void setNoiseNotifOn(boolean noiseNotifOn) {
        this.noiseNotifOn = noiseNotifOn;
    }

    public int getLastNoiseAlert() {
        return lastNoiseAlert;
    }
    public double getLastMaxTempAlert() {
        return lastMaxTempAlert;
    }
    public double getLastMinTempAlert() {
        return lastMinTempAlert;
    }
    public double getLastMaxHumAlert() {
        return lastMaxHumAlert;
    }
    public double getLastMinHumAlert() {
        return lastMinHumAlert;
    }

    public boolean isNoiseNotifOn() {
        return noiseNotifOn;
    }
    public boolean isTempNotifOn() {
        return tempNotifOn;
    }
    public boolean isHumiNotifOn() {
        return humiNotifOn;
    }

//    @Override
//    public void run() {
//        // Create a ScheduledExecutorService for notifications
//        ScheduledExecutorService notificationScheduler = Executors.newSingleThreadScheduledExecutor();
//        //Schedule sendAlerts with initial delay of 101 to avoid notifications during initialisation
//        notificationScheduler.scheduleAtFixedRate(this::sendAlerts, 101, 100, TimeUnit.MILLISECONDS);
//    }

}
