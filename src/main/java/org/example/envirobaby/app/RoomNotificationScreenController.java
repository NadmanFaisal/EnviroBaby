package org.example.envirobaby.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import org.example.envirobaby.User;
import org.example.envirobaby.UserExchanger;

import java.io.IOException;

/**
 * Controller for the system for toggling notifications settings in specific rooms selection.
 */

public class RoomNotificationScreenController {

    @FXML
    private GridPane overview;

    @FXML
    private RowConstraints overviewRow;

    @FXML
    private Button room1;

    @FXML
    private Button room2;

    @FXML
    private Button room3;

    @FXML
    private Button room4;

    @FXML
    private Button turnOnTempNotifButton; // Button to turn on temperature notifications

    @FXML
    private Button turnOffTempNotifButton; // Button to turn off temperature notifications

    @FXML
    private Button turnOnHumiNotifButton; // Button to turn on humidity notifications
    @FXML
    private Button turnOffHumiNotifButton; // Button to turn off humidity notifications
    @FXML
    private Button turnOnNoiseNotifButton; // Button to turn on noise notifications
    @FXML
    private Button turnOffNoiseNotifButton; // Button to turn off noise notifications
    @FXML
    private UserExchanger instanceUser = UserExchanger.getInstance();
    private User currentUser = instanceUser.getInstanceUser();

    private boolean room1IsActive = false;
    private boolean room2IsActive = false;
    private boolean room3IsActive = false;
    private boolean room4IsActive = false;

    public void initialize () throws IOException { // based on user`s data , after the FXML file loaded, configures visibility and startup settings.

        turnOnTempNotifButton.setVisible(false);
        turnOffTempNotifButton.setVisible(false);
        turnOnHumiNotifButton.setVisible(false);
        turnOffHumiNotifButton.setVisible(false);
        turnOnNoiseNotifButton.setVisible(false);
        turnOffNoiseNotifButton.setVisible(false);

    // Display the room buttons based on how many rooms the current user has
        switch (currentUser.getRooms().size()) {
        case 1 -> {
            room1.setText(currentUser.getRoom(1).getRoomName());
            room1.setVisible(true);
            turnOnTempNotifButton.setVisible(true);
            turnOffTempNotifButton.setVisible(true);
            turnOnHumiNotifButton.setVisible(true);
            turnOffHumiNotifButton.setVisible(true);
            turnOnNoiseNotifButton.setVisible(true);
            turnOffNoiseNotifButton.setVisible(true);

            room1IsActive = true;
            room2IsActive = false;
            room3IsActive = false;
            room4IsActive = false;
        }
        case 2 -> {
            room1.setText(currentUser.getRoom(1).getRoomName());
            room1.setVisible(true);
            room2.setText(currentUser.getRoom(2).getRoomName());
            room2.setVisible(true);
            turnOnTempNotifButton.setVisible(true);
            turnOffTempNotifButton.setVisible(true);
            turnOnHumiNotifButton.setVisible(true);
            turnOffHumiNotifButton.setVisible(true);
            turnOnNoiseNotifButton.setVisible(true);
            turnOffNoiseNotifButton.setVisible(true);

            room1IsActive = true;
            room2IsActive = false;
            room3IsActive = false;
            room4IsActive = false;
        }
        case 3 -> {
            room1.setText(currentUser.getRoom(1).getRoomName());
            room1.setVisible(true);
            room2.setText(currentUser.getRoom(2).getRoomName());
            room2.setVisible(true);
            room3.setText(currentUser.getRoom(3).getRoomName());
            room3.setVisible(true);
            turnOnTempNotifButton.setVisible(true);
            turnOffTempNotifButton.setVisible(true);
            turnOnHumiNotifButton.setVisible(true);
            turnOffHumiNotifButton.setVisible(true);
            turnOnNoiseNotifButton.setVisible(true);
            turnOffNoiseNotifButton.setVisible(true);

            room1IsActive = true;
            room2IsActive = false;
            room3IsActive = false;
            room4IsActive = false;
        }
        case 4 -> {
            room1.setText(currentUser.getRoom(1).getRoomName());
            room1.setVisible(true);
            room2.setText(currentUser.getRoom(2).getRoomName());
            room2.setVisible(true);
            room3.setText(currentUser.getRoom(3).getRoomName());
            room3.setVisible(true);
            room4.setText(currentUser.getRoom(4).getRoomName());
            room4.setVisible(true);
            turnOnTempNotifButton.setVisible(true);
            turnOffTempNotifButton.setVisible(true);
            turnOnHumiNotifButton.setVisible(true);
            turnOffHumiNotifButton.setVisible(true);
            turnOnNoiseNotifButton.setVisible(true);
            turnOffNoiseNotifButton.setVisible(true);

            room1IsActive = true;
            room2IsActive = false;
            room3IsActive = false;
            room4IsActive = false;
        }
    }
    }
    /**
     * after clicking on the button "Room 1",
     * the screen switches to the Room 1 screen.
     * @param event
     * @throws IOException
     */

    public void homeButtonClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("homeScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    /**
     * after clicking on the button "Room 2",
     * the screen triggers room1 flag to be true,
     * indicating that the user is in room 1
     * @param event
     */
    public void moveToRoom1(ActionEvent event) {
        room1IsActive = true;
        room2IsActive = false;
        room3IsActive = false;
        room4IsActive = false;
    }

    /**
     * after clicking on the button "Room 2",
     * the screen triggers room2 flag to be true,
     * indicating that the user is in room 2
     * @param event
     */

    public void moveToRoom2(ActionEvent event) {
        room1IsActive = false;
        room2IsActive = true;
        room3IsActive = false;
        room4IsActive = false;
    }

    /**
     * after clicking on the button "Room 3",
     * the screen triggeers room 3 flag to be true,
     * indicating that the user is in room 3.
     * @param event
     */

    public void moveToRoom3(ActionEvent event) {
        room1IsActive = false;
        room2IsActive = false;
        room3IsActive = true;
        room4IsActive = false;
    }

    /**
     * after clicking on the button "Room 4",
     * the screen triggers room4 flag to be true,
     * indicating that the user is in room 4.
     * @param event
     */

    public void moveToRoom4(ActionEvent event) {
        room1IsActive = false;
        room2IsActive = false;
        room3IsActive = false;
        room4IsActive = true;
    }
    public RoomNotificationScreenController() {
        this.instanceUser = UserExchanger.getInstance();
    }
    public void temperatureTurnONButton(ActionEvent actionEvent) { // enable temperature notifications
        instanceUser.tempNotiON();
    }

    public void temperatureTurnOFFButton(ActionEvent actionEvent) { // disable temperature notifications
        instanceUser.tempNotiOFF();
    }

    public void humidityTurnONButton(ActionEvent actionEvent) { // enable humidity notifications
        instanceUser.humiNotiON();
    }

    public void humidityTurnOFFButton(ActionEvent actionEvent) { // disable humidity notifications
        instanceUser.humiNotiOFF();
    }
    public void noiseTurnONButton(ActionEvent actionEvent) { // enable noise notifications
        instanceUser.noiseNotiON();
    }

    public void noiseTurnOFFButton(ActionEvent actionEvent) { // disable noise notifications
        instanceUser.noiseNotiOFF();
    }

}
