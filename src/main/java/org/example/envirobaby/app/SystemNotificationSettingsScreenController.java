package org.example.envirobaby.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.envirobaby.User;
import org.example.envirobaby.UserExchanger;

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
    private Button roomNotificationSettings;

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
        instanceUser.getInstanceUser().tempNotiON();
    }

    public void temperatureTurnOFFButton(ActionEvent actionEvent) {

        instanceUser.getInstanceUser().tempNotiOFF();
    }

    public void humidityTurnONButton(ActionEvent actionEvent) {

        instanceUser.getInstanceUser().humiNotiON();
    }

    public void humidityTurnOFFButton(ActionEvent actionEvent) {
        instanceUser.getInstanceUser().humiNotiOFF();
    }

    public void noiseTurnONButton(ActionEvent actionEvent) {
        instanceUser.getInstanceUser().noiseNotiON();
    }

    public void noiseTurnOFFButton(ActionEvent actionEvent) {
        instanceUser.getInstanceUser().noiseNotiOFF();
    }
    public void moveToRoom1Screen(ActionEvent actionEvent)  throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("RoomNotificationScreen.fxml"));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
