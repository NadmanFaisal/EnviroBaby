package org.example.envirobaby.Notification;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.text.DecimalFormat;


public class Notification {


    private boolean tempNotifOn;
    private boolean humiNotifOn;
    private boolean noiseNotifOn;

    private long lastNoiseAlert;
    private long lastMaxTempAlert;
    private long lastMinTempAlert;
    private long lastMaxHumAlert;
    private long lastMinHumAlert;


    DecimalFormat df = new DecimalFormat("#.00");

    public Notification() {
        this.lastNoiseAlert=0L;
        this.lastMaxTempAlert=0L;
        this.lastMinTempAlert=0L;
        this.lastMaxHumAlert=0L;
        this.lastMinHumAlert=0L;

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
     * @param notifType
     * The type of notification
     */
    public void createNotification(String title, String message, String notifType) {

        long currentTime = System.currentTimeMillis();
        long notificationInterval = 10000L;
        long timeDif = 0L;

        switch (notifType) { // Calculates the time difference between the current time and the last notification time, based on the notification type
            case "maxTemp":
                timeDif= currentTime-lastMaxTempAlert;
            case "minTemp":
                timeDif= currentTime-lastMinTempAlert;
            case "maxHum":
                timeDif= currentTime-lastMaxHumAlert;
            case "minHum":
                timeDif= currentTime-lastMinHumAlert;
            case "maxNoise":
                timeDif= currentTime-lastNoiseAlert;
                }

        if (timeDif > notificationInterval) { // Checks if the time difference exceeds the minimum notification interval
            Platform.runLater(() -> {

                // Source for the code:- https://javadoc.io/doc/org.controlsfx/controlsfx/8.40.16/org/controlsfx/control/Notifications.html
                Notifications notifications = Notifications.create().title(title).text(message).graphic(null).hideAfter(Duration.seconds(7)).position(Pos.BOTTOM_RIGHT);
                notifications.showWarning();
            });
            switch (notifType) { // Updates the last notification time to the current time, based on the notification type
                case "maxTemp":
                    lastMaxTempAlert = currentTime;
                case "minTemp":
                    lastMinTempAlert = currentTime;
                case "maxHum":
                    lastMaxHumAlert = currentTime;
                case "minHum":
                    lastMinHumAlert = currentTime;
                case "maxNoise":
                    lastNoiseAlert = currentTime;
            }
        }
    }


    public void setTempNotifOn(boolean tempNotifOn){ this.tempNotifOn=tempNotifOn;}
    public void setHumiNotifOn(boolean humiNotifOn) { this.humiNotifOn = humiNotifOn;}
    public void setNoiseNotifOn(boolean noiseNotifOn) { this.noiseNotifOn = noiseNotifOn;}


    public boolean isNoiseNotifOn() {
        return noiseNotifOn;
    }
    public boolean isTempNotifOn() {
        return tempNotifOn;
    }
    public boolean isHumiNotifOn() {
        return humiNotifOn;
    }


}
