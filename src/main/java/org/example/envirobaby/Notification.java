package org.example.envirobaby;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class Notification {

    public void createNotification(String title, String message) {
        Platform.runLater(() -> {
            Notifications notifications = Notifications.create().title(title).text(message).graphic(null).hideAfter(Duration.seconds(7)).position(Pos.BOTTOM_RIGHT);
            notifications.showWarning();
        });
    }

}
