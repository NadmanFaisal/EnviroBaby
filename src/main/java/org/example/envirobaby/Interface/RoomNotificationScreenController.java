package org.example.envirobaby.Interface;

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
import org.example.envirobaby.Database.DatabaseControl;
import org.example.envirobaby.Entity.Room;
import org.example.envirobaby.Entity.User;
import org.example.envirobaby.Entity.UserExchanger;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for toggling notifications settings in specific rooms selection.
 */

public class RoomNotificationScreenController {

    @FXML
    private GridPane overview;

    @FXML
    private RowConstraints overviewRow;

    private static final int FIRST_ROOM = 1;
    private static final int SECOND_ROOM = 2;
    private static final int THIRD_ROOM = 3;
    private static final int FOURTH_ROOM = 4;

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
    private DatabaseControl database;

    public void initialize () throws IOException, SQLException { // based on user`s data , after the FXML file loaded, configures visibility and startup settings.

        database = new DatabaseControl();

        // Hide all buttons initially
        room1.setVisible(false);
        room2.setVisible(false);
        room3.setVisible(false);
        room4.setVisible(false);

        turnOnTempNotifButton.setVisible(false);
        turnOffTempNotifButton.setVisible(false);
        turnOnHumiNotifButton.setVisible(false);
        turnOffHumiNotifButton.setVisible(false);
        turnOnNoiseNotifButton.setVisible(false);
        turnOffNoiseNotifButton.setVisible(false);

    // Display the room buttons based on how many rooms the current user has
        switch (currentUser.getRooms().size()) {
        case 1 -> {
            room1.setText(currentUser.getRoom(FIRST_ROOM).getRoomName());
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
            room1.setText(currentUser.getRoom(FIRST_ROOM).getRoomName());
            room1.setVisible(true);
            room2.setText(currentUser.getRoom(SECOND_ROOM).getRoomName());
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
            room1.setText(currentUser.getRoom(FIRST_ROOM).getRoomName());
            room1.setVisible(true);
            room2.setText(currentUser.getRoom(SECOND_ROOM).getRoomName());
            room2.setVisible(true);
            room3.setText(currentUser.getRoom(THIRD_ROOM).getRoomName());
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
            room1.setText(currentUser.getRoom(FIRST_ROOM).getRoomName());
            room1.setVisible(true);
            room2.setText(currentUser.getRoom(SECOND_ROOM).getRoomName());
            room2.setVisible(true);
            room3.setText(currentUser.getRoom(THIRD_ROOM).getRoomName());
            room3.setVisible(true);
            room4.setText(currentUser.getRoom(FOURTH_ROOM).getRoomName());
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
     * Navigates back to the home screen.
     * @param event The event that triggered this action.
     * @throws IOException if unable to load the home screen FXML.
     */
    public void homeButtonClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("homeScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    /**
     * Sets the current room to the first room.
     * @param event The event that triggered this action.
     */
    public void moveToRoom1(ActionEvent event) {
        instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRooms().get(FIRST_ROOM));
    }

    /**
     * Sets the current room to the second room.
     * @param event The event that triggered this action.
     */

    public void moveToRoom2(ActionEvent event) {
        instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRooms().get(SECOND_ROOM));
    }

    /**
     * Sets the current room to the third room.
     * @param event The event that triggered this action.
     */

    public void moveToRoom3(ActionEvent event) {
        instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRooms().get(THIRD_ROOM));
    }

    /**
     * Sets the current room to the fourth room.
     * @param event The event that triggered this action.
     */

    public void moveToRoom4(ActionEvent event) {
        instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRooms().get(FOURTH_ROOM));
    }

    public RoomNotificationScreenController() {
        this.instanceUser = UserExchanger.getInstance();
    }

    /**
     * Turns temperature notifications on for the current room.
     * @param actionEvent The event that triggered this action.
     */
    public void tempNotiOn(ActionEvent actionEvent) throws SQLException {
        instanceUser.getCurrentRoom().settTempNotifON(true);
        updateRoomNotifSettings();
    }

    /**
     * Turns temperature notifications off for the current room.
     * @param actionEvent The event that triggered this action.
     */
    public void tempNotiOff(ActionEvent actionEvent) throws SQLException {
        instanceUser.getCurrentRoom().settTempNotifON(false);
        updateRoomNotifSettings();
    }

    /**
     * Turns humidity notifications on for the current room.
     * @param actionEvent The event that triggered this action.
     */
    public void humiNotiOn(ActionEvent actionEvent) throws SQLException {
        instanceUser.getCurrentRoom().setHumiNotifON(true);
        updateRoomNotifSettings();
    }

    /**
     * Turns humidity notifications off for the current room.
     * @param actionEvent The event that triggered this action.
     */
    public void humiNotiOff(ActionEvent actionEvent) throws SQLException {
        instanceUser.getCurrentRoom().setHumiNotifON(false);
        updateRoomNotifSettings();
    }

    /**
     * Turns noise notifications on for the current room.
     * @param actionEvent The event that triggered this action.
     */
    public void noiseNotiOn(ActionEvent actionEvent) throws SQLException {
        instanceUser.getCurrentRoom().setNoiseNotifON(true);
        updateRoomNotifSettings();
    }

    /**
     * Turns noise notifications off for the current room.
     * @param actionEvent The event that triggered this action.
     */
    public void noiseNotiOff(ActionEvent actionEvent) throws SQLException {
        instanceUser.getCurrentRoom().setNoiseNotifON(false);
        updateRoomNotifSettings();
    }

    public void updateRoomNotifSettings() throws SQLException {
        Room currentRoom = instanceUser.getCurrentRoom();
        database.updateAlertToggle(currentRoom.getUserId(), currentRoom.getRoomName(), currentRoom.isNoiseNotif(),currentRoom.isTempNotif(),currentRoom.isHumiNotif());
    }

}
