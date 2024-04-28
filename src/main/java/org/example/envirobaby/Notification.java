package org.example.envirobaby;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


// Source for the code:- https://javadoc.io/doc/org.controlsfx/controlsfx/8.40.16/org/controlsfx/control/Notifications.html
public class Notification {

    private DataProcessor processor = new DataProcessor();

    public void createNoiseNotification(String value, double threshold) {
        if(processor.extractNumber(value) > threshold) {
            Platform.runLater(() -> {
                String notiMessage = "NOISE THRESHOLD CROSSED: " + processor.extractNumber(value) + " db";
                String notiTitle = "Noise notification";
                Notifications notifications = Notifications.create().title(notiTitle).text(notiMessage).graphic(null).hideAfter(Duration.seconds(7)).position(Pos.BOTTOM_RIGHT);
                notifications.showWarning();
            });
        }
    }

    public void createTempNotification(String value, double upperThreshold, double lowerThreshold) {
        if(processor.extractDouble(value) > upperThreshold) {
            Platform.runLater(() -> {
                String notiMessaeg = "TEMPERATURE EXCEEDS THRESHOLD: " + processor.extractDouble(value) + " C";
                String notiTitle = "Temperature notification";
                Notifications notifications = Notifications.create().title(notiTitle).text(notiMessaeg).graphic(null).hideAfter(Duration.seconds(7)).position(Pos.BOTTOM_RIGHT);
                notifications.showWarning();
            });
        } else if (processor.extractDouble(value) < lowerThreshold) {
            Platform.runLater(() -> {
                String notiMessage = "TEMPERATURE BELOW THRESHOLD: " + processor.extractDouble(value) + " C";
                String notiTitle = "Temperature notification";
                Notifications notifications = Notifications.create().title(notiTitle).text(notiMessage).graphic(null).hideAfter(Duration.seconds(7)).position(Pos.BOTTOM_RIGHT);
                notifications.showWarning();
            });
        }
    }

    public void createHumNotification(String value, double upperThreshold, double lowerThreshold) {
        if(processor.extractDouble(value) > upperThreshold) {
            Platform.runLater(() -> {
                String notiMessaeg = "Humidity exceeded! " + value;
                String notiTitle = "Humidity notification";
                Notifications notifications = Notifications.create().title(notiTitle).text(notiMessaeg).graphic(null).hideAfter(Duration.seconds(7)).position(Pos.BOTTOM_RIGHT);
                notifications.showWarning();
            });
        } else if (processor.extractDouble(value) < lowerThreshold) {
            Platform.runLater(() -> {
                String notiMessage = "Humidity too low! " + value;
                String notiTitle = "Humidity notification";
                Notifications notifications = Notifications.create().title(notiTitle).text(notiMessage).graphic(null).hideAfter(Duration.seconds(7)).position(Pos.BOTTOM_RIGHT);
                notifications.showWarning();
            });
        }
    }

}
