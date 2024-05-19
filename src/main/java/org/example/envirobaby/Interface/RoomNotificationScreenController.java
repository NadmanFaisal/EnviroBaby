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
    @FXML
    private GridPane roomView;

    private DatabaseControl database;
    private Room currentRoom;
    private SharedControllerFunctions setupFunctions = new SharedControllerFunctions();

    public void initialize () throws IOException, SQLException { // based on user`s data , after the FXML file loaded, configures visibility and startup settings.

        database = new DatabaseControl();

        // Hide all buttons initially
        setupFunctions.setupRoomsDisplay(room1,room2,room3,room4,roomView);
        displayRoomNotifSettings();
    }


    /**
     * Navigates back to the home screen.
     * @param event The event that triggered this action.
     * @throws IOException if unable to load the home screen FXML.
     */
    public void goBackButtonClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("SystemNotificationSettingsScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        double chosenHeight = stage.getHeight();
        double chosenWidth = stage.getWidth();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setHeight(chosenHeight);
        stage.setWidth(chosenWidth);
    }

    /**
     * Sets the current room to the first room.
     * @param event The event that triggered this action.
     */
    public void moveToRoom1(ActionEvent event) {
        instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRooms().get(FIRST_ROOM));
        setupFunctions.resetButtons(room1,room2,room3,room4);
        room1.setDisable(true);
        displayRoomNotifSettings();
    }

    /**
     * Sets the current room to the second room.
     * @param event The event that triggered this action.
     */

    public void moveToRoom2(ActionEvent event) {
        instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRooms().get(SECOND_ROOM));
        setupFunctions.resetButtons(room1,room2,room3,room4);
        room2.setDisable(true);
        displayRoomNotifSettings();
    }

    /**
     * Sets the current room to the third room.
     * @param event The event that triggered this action.
     */

    public void moveToRoom3(ActionEvent event) {
        instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRooms().get(THIRD_ROOM));
        setupFunctions.resetButtons(room1,room2,room3,room4);
        room3.setDisable(true);
        displayRoomNotifSettings();
    }

    /**
     * Sets the current room to the fourth room.
     * @param event The event that triggered this action.
     */

    public void moveToRoom4(ActionEvent event) {
        instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRooms().get(FOURTH_ROOM));
        setupFunctions.resetButtons(room1,room2,room3,room4);
        room4.setDisable(true);
        displayRoomNotifSettings();
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
        turnOnTempNotifButton.setDisable(true);
        turnOffTempNotifButton.setDisable(false);
    }

    /**
     * Turns temperature notifications off for the current room.
     * @param actionEvent The event that triggered this action.
     */
    public void tempNotiOff(ActionEvent actionEvent) throws SQLException {
        instanceUser.getCurrentRoom().settTempNotifON(false);
        updateRoomNotifSettings();
        turnOnTempNotifButton.setDisable(false);
        turnOffTempNotifButton.setDisable(true);
    }

    /**
     * Turns humidity notifications on for the current room.
     * @param actionEvent The event that triggered this action.
     */
    public void humiNotiOn(ActionEvent actionEvent) throws SQLException {
        instanceUser.getCurrentRoom().setHumiNotifON(true);
        updateRoomNotifSettings();
        turnOnHumiNotifButton.setDisable(true);
        turnOffHumiNotifButton.setDisable(false);
    }

    /**
     * Turns humidity notifications off for the current room.
     * @param actionEvent The event that triggered this action.
     */
    public void humiNotiOff(ActionEvent actionEvent) throws SQLException {
        instanceUser.getCurrentRoom().setHumiNotifON(false);
        updateRoomNotifSettings();
        turnOnHumiNotifButton.setDisable(false);
        turnOffHumiNotifButton.setDisable(true);
    }

    /**
     * Turns noise notifications on for the current room.
     * @param actionEvent The event that triggered this action.
     */
    public void noiseNotiOn(ActionEvent actionEvent) throws SQLException {
        instanceUser.getCurrentRoom().setNoiseNotifON(true);
        updateRoomNotifSettings();
        turnOnNoiseNotifButton.setDisable(true);
        turnOffNoiseNotifButton.setDisable(false);
    }

    /**
     * Turns noise notifications off for the current room.
     * @param actionEvent The event that triggered this action.
     */
    public void noiseNotiOff(ActionEvent actionEvent) throws SQLException {
        instanceUser.getCurrentRoom().setNoiseNotifON(false);
        updateRoomNotifSettings();
        turnOnNoiseNotifButton.setDisable(false);
        turnOffNoiseNotifButton.setDisable(true);
    }

    public void updateRoomNotifSettings() throws SQLException {
        Room currentRoom = instanceUser.getCurrentRoom();
        database.updateAlertToggle(currentRoom.getUserId(), currentRoom.getRoomName(), currentRoom.isNoiseNotif(),currentRoom.isTempNotif(),currentRoom.isHumiNotif());
    }

    public void displayRoomNotifSettings() {
        currentRoom=instanceUser.getCurrentRoom();

        turnOnTempNotifButton.setDisable(false);
        turnOffTempNotifButton.setDisable(false);
        turnOnHumiNotifButton.setDisable(false);
        turnOffHumiNotifButton.setDisable(false);
        turnOnNoiseNotifButton.setDisable(false);
        turnOffNoiseNotifButton.setDisable(false);



        if (!currentUser.getRooms().isEmpty()) {
            if (currentRoom.isTempNotif()){
                turnOnTempNotifButton.setDisable(true);
            } else {
                turnOffTempNotifButton.setDisable(true);
            }
            if (currentRoom.isHumiNotif()){
                turnOnHumiNotifButton.setDisable(true);
            } else {
                turnOffHumiNotifButton.setDisable(true);
            }
            if (currentRoom.isNoiseNotif()){
                turnOnNoiseNotifButton.setDisable(true);
            } else {
                turnOffNoiseNotifButton.setDisable(true);
            }
        }
    }

}
