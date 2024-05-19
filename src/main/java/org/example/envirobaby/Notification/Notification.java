package org.example.envirobaby.Notification;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.media.AudioClip;
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

    //Sound Effect by "https://pixabay.com/users/universfield-28281460/?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=126514" from "https://pixabay.com/sound-effects//?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=126514"
    private AudioClip notification = new AudioClip(getClass().getResource("notif.mp3").toExternalForm());




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
        long notificationInterval = 60000L;
        long timeDif = 0L;

        switch (notifType) { // Calculates the time difference between the current time and the last notification time, based on the notification type
            case "maxTemp":
                timeDif= currentTime-lastMaxTempAlert;
                break;
            case "minTemp":
                timeDif= currentTime-lastMinTempAlert;
                break;
            case "maxHum":
                timeDif= currentTime-lastMaxHumAlert;
                break;
            case "minHum":
                timeDif= currentTime-lastMinHumAlert;
                break;
            case "maxNoise":
                timeDif= currentTime-lastNoiseAlert;
                break;
        }

        if (timeDif > notificationInterval) { // Checks if the time difference exceeds the minimum notification interval
            Platform.runLater(() -> {

                // Source for the code:- https://javadoc.io/doc/org.controlsfx/controlsfx/8.40.16/org/controlsfx/control/Notifications.html
                Notifications notifications = Notifications.create().title(title).text(message).graphic(null).hideAfter(Duration.seconds(7)).position(Pos.BOTTOM_RIGHT);
                notifications.showWarning();
                notification.play();
            });
            switch (notifType) { // Updates the last notification time to the current time, based on the notification type
                case "maxTemp":
                    lastMaxTempAlert = currentTime;
                    break;
                case "minTemp":
                    lastMinTempAlert = currentTime;
                    break;
                case "maxHum":
                    lastMaxHumAlert = currentTime;
                    break;
                case "minHum":
                    lastMinHumAlert = currentTime;
                    break;
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
