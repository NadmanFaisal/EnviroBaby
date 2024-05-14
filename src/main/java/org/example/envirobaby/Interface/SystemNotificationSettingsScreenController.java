package org.example.envirobaby.Interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.envirobaby.Entity.UserExchanger;

import java.io.IOException;

public class SystemNotificationSettingsScreenController {

    @FXML
    private Button homebutton;
    @FXML
    private Button tempTurnOFFButton;

    @FXML
    private Button tempTurnONButton;


    @FXML
    private Button humiTurnONButton;
    @FXML
    private Button humiTurnOFFButton;
    @FXML
    private Button noiseTurnONButton;

    @FXML
    private Button noiseTurnOFFButton;

    @FXML
    private Label tempTXT;

    @FXML
    private Label humiTXT;

    @FXML
    private Label noiseTXT;

    private UserExchanger instanceUser;

    @FXML
    void initialize() {
        instanceUser= UserExchanger.getInstance();
    }
    @FXML
    void loadHomeScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("homeScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    void systemSettingsClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("systemNotificationSettingsScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void temperatureTurnONButton(ActionEvent actionEvent) {
        instanceUser.tempNotiON();
    }

    public void temperatureTurnOFFButton(ActionEvent actionEvent) {
        instanceUser.tempNotiOFF();
    }

    public void humidityTurnONButton(ActionEvent actionEvent) {
        instanceUser.humiNotiON();
    }

    public void humidityTurnOFFButton(ActionEvent actionEvent) {
        instanceUser.humiNotiOFF();
    }

    public void noiseTurnONButton(ActionEvent actionEvent) {
        instanceUser.noiseNotiON();
    }

    public void noiseTurnOFFButton(ActionEvent actionEvent) {
        instanceUser.noiseNotiOFF();
    }



    /**
     * Handles click events for the room notification settings.
     * Currently, this method does not perform any actions but can be used to implement
     * functionality related to room settings notifications in the future.
     *
     * @param actionEvent the event triggered by clicking the notification settings control
     */

    public void roomNotificationSettingsClick(ActionEvent actionEvent) {}
}
