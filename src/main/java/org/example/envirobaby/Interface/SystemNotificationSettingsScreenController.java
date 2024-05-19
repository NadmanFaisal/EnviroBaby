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
import org.example.envirobaby.Database.DatabaseControl;
import org.example.envirobaby.Entity.User;
import org.example.envirobaby.Entity.UserExchanger;

import java.io.IOException;
import java.sql.SQLException;

public class SystemNotificationSettingsScreenController {

    @FXML
    private Button homebutton;
    @FXML
    private Button tempNotiTurnOffButton;
    @FXML
    private Button tempNotiTurnOnButton;
    @FXML
    private Button humiNotiTurnOnButton;
    @FXML
    private Button humiNotiTurnOffButton;
    @FXML
    private Button noiseNotiTurnOnButton;
    @FXML
    private Button noiseNotiTurnOffButton;
    @FXML
    private Button roomNotificationSettings;
    @FXML
    private Button systemsettings;

    @FXML
    private Label tempTXT;

    @FXML
    private Label humiTXT;

    @FXML
    private Label noiseTXT;

    private UserExchanger instanceUser;
    private DatabaseControl database;

    @FXML
    void initialize() throws SQLException {
        instanceUser= UserExchanger.getInstance();
        database = new DatabaseControl();

        systemsettings.setDisable(true);

        if (instanceUser.getInstanceUser().isTempNotiStatus()){
            tempNotiTurnOnButton.setDisable(true);
        } else {
            tempNotiTurnOffButton.setDisable(true);
        }
        if (instanceUser.getInstanceUser().isHumiNotiStatus()){
            humiNotiTurnOnButton.setDisable(true);
        } else {
            humiNotiTurnOffButton.setDisable(true);
        }
        if (instanceUser.getInstanceUser().isNoiseNotiStatus()){
            noiseNotiTurnOnButton.setDisable(true);
        } else {
            noiseNotiTurnOffButton.setDisable(true);
        }
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

    public void tempNotiOn(ActionEvent actionEvent) throws SQLException {
        instanceUser.getInstanceUser().setTempNotiStatus(true);
        updateDatabaseSettings();
        tempNotiTurnOffButton.setDisable(false);
        tempNotiTurnOnButton.setDisable(true);
    }

    public void tempNotiOff(ActionEvent actionEvent) throws SQLException {

        instanceUser.getInstanceUser().setTempNotiStatus(false);
        updateDatabaseSettings();
        tempNotiTurnOffButton.setDisable(true);
        tempNotiTurnOnButton.setDisable(false);
    }

    public void humiNotiOn(ActionEvent actionEvent) throws SQLException {

        instanceUser.getInstanceUser().setHumiNotiStatus(true);
        updateDatabaseSettings();
        humiNotiTurnOffButton.setDisable(false);
        humiNotiTurnOnButton.setDisable(true);
    }

    public void humiNotiOff(ActionEvent actionEvent) throws SQLException {
        instanceUser.getInstanceUser().setHumiNotiStatus(false);
        updateDatabaseSettings();
        humiNotiTurnOffButton.setDisable(true);
        humiNotiTurnOnButton.setDisable(false);
    }

    public void noiseNotiOn(ActionEvent actionEvent) throws SQLException {
        instanceUser.getInstanceUser().setNoiseNotiStatus(true);
        updateDatabaseSettings();
        noiseNotiTurnOnButton.setDisable(true);
        noiseNotiTurnOffButton.setDisable(false);
    }

    public void noiseNotiOff(ActionEvent actionEvent) throws SQLException {
        instanceUser.getInstanceUser().setNoiseNotiStatus(false);
        updateDatabaseSettings();
        noiseNotiTurnOnButton.setDisable(false);
        noiseNotiTurnOffButton.setDisable(true);
    }

    public void updateDatabaseSettings() throws SQLException {
        User currentUser = instanceUser.getInstanceUser();
        database.updateSystemNotificationSettings(currentUser.getUserID(),currentUser.isNoiseNotiStatus(),currentUser.isTempNotiStatus(),currentUser.isHumiNotiStatus());
    }
    public void moveToRoom1Screen(ActionEvent actionEvent)  throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("RoomNotificationScreen.fxml"));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
