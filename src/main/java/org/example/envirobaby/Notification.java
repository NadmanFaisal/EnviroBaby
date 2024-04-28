package org.example.envirobaby;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


// Source for the code:- https://javadoc.io/doc/org.controlsfx/controlsfx/8.40.16/org/controlsfx/control/Notifications.html
public class Notification {

    public void createNoiseNotification(String value, double threshold) {
        if(extractNumber(value) > threshold) {
            Platform.runLater(() -> {
                String notiMessage = "NOISE THRESHOLD CROSSED: " + extractNumber(value) + " db";
                String notiTitle = "Noise notification";
                Notifications notifications = Notifications.create().title(notiTitle).text(notiMessage).graphic(null).hideAfter(Duration.seconds(7)).position(Pos.BOTTOM_RIGHT);
                notifications.showWarning();
            });
        }
    }

    public void createTempNotification(String value, double upperThreshold, double lowerThreshold) {
        if(extractDouble(value) > upperThreshold) {
            Platform.runLater(() -> {
                String notiMessaeg = "TEMPERATURE EXCEEDS THRESHOLD: " + extractDouble(value) + " C";
                String notiTitle = "Temperature notification";
                Notifications notifications = Notifications.create().title(notiTitle).text(notiMessaeg).graphic(null).hideAfter(Duration.seconds(7)).position(Pos.BOTTOM_RIGHT);
                notifications.showWarning();
            });
        } else if (extractDouble(value) < lowerThreshold) {
            Platform.runLater(() -> {
                String notiMessage = "TEMPERATURE BELOW THRESHOLD: " + extractDouble(value) + " C";
                String notiTitle = "Temperature notification";
                Notifications notifications = Notifications.create().title(notiTitle).text(notiMessage).graphic(null).hideAfter(Duration.seconds(7)).position(Pos.BOTTOM_RIGHT);
                notifications.showWarning();
            });
        }
    }

    public void createHumNotification(String value, double upperThreshold, double lowerThreshold) {
        if(extractDouble(value) > upperThreshold) {
            Platform.runLater(() -> {
                String notiMessaeg = "Humidity exceeded! " + value;
                String notiTitle = "Humidity notification";
                Notifications notifications = Notifications.create().title(notiTitle).text(notiMessaeg).graphic(null).hideAfter(Duration.seconds(7)).position(Pos.BOTTOM_RIGHT);
                notifications.showWarning();
            });
        } else if (extractDouble(value) < lowerThreshold) {
            Platform.runLater(() -> {
                String notiMessage = "Humidity too low! " + value;
                String notiTitle = "Humidity notification";
                Notifications notifications = Notifications.create().title(notiTitle).text(notiMessage).graphic(null).hideAfter(Duration.seconds(7)).position(Pos.BOTTOM_RIGHT);
                notifications.showWarning();
            });
        }
    }

    public int extractNumber(String message) {
        StringBuilder number = new StringBuilder();
        boolean found = false;

        // Loops and checks if a character is a digit
        for (char letter : message.toCharArray()) {
            if (Character.isDigit(letter)) {
                number.append(letter);
                found = true;
            } else if (found) {
                break;
            }
        }

        if (!number.isEmpty()) {

            // Converts a string to an int if it is compatible to do so
            return Integer.parseInt(number.toString());
        } else {
            throw new NumberFormatException("No number found in the text");
        }
    }

    public double extractDouble(String message) { //  locate double within a String
        Pattern doublePattern = Pattern.compile("([0-9]{1,13}(\\.[0-9]*)?)"); //setup regex pattern
        Matcher doubleMatcher = doublePattern.matcher(message); // search for pattern in string
        double extracted = 0;

        if(doubleMatcher.find()) {
            extracted = Double.parseDouble(doubleMatcher.group()); //if found, parse into double
        }
        return extracted;
    }

}
