package org.example.envirobaby;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class NotificationSettingsScreenController {

    @FXML
    private Button homebutton;

    @FXML
    private Button roomNotificationsButton;

    @FXML
    private Button systemNotificationsButton;

    @FXML
    void loadHomeScreen(ActionEvent event) throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("homeScreen.fxml"));
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

        }

    @FXML
    void roomNotificationSettingsScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("room1NotificationSettingsScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);


    }

    @FXML
    void systemNotificationSettingsScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("systemNotificationSettingsScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);

}
}