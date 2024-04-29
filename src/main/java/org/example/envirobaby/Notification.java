package org.example.envirobaby;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;



// Source for the code:- https://javadoc.io/doc/org.controlsfx/controlsfx/8.40.16/org/controlsfx/control/Notifications.html
public class Notification {

    private int lastNoiseAlert;
    private double lastMaxTempAlert;
    private double lastMinTempAlert;
    private double lastMaxHumAlert;
    private double lastMinHumAlert;

    public Notification() {
        this.lastNoiseAlert=0;
        this.lastMaxTempAlert=0;
        this.lastMinTempAlert=0;
        this.lastMaxHumAlert=0;
        this.lastMinHumAlert=0;
    }

    public void createNotification(String title, String message) {
        Platform.runLater(() -> {
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
}
