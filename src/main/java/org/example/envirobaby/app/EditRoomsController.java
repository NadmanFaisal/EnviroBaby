package org.example.envirobaby.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.envirobaby.DatabaseControl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.example.envirobaby.Room;
import org.example.envirobaby.User;
import org.example.envirobaby.UserExchanger;

public class EditRoomsController {

    @FXML
    private Label AgeGroupLabel;

    @FXML
    private Label AgeGroupLabel1;

    @FXML
    private ChoiceBox<String> AgeGroupPicker;

    @FXML
    private Button DeleteRoomButton;

    @FXML
    private TextField MaxCapacity;

    @FXML
    private Label MaxCapacityLabel;

    @FXML
    private TextField NewRoomName;

    @FXML
    private Button SaveChangesButton;

    @FXML
    private Button room1;

    @FXML
    private Button room2;

    @FXML
    private Button room3;

    @FXML
    private Button room4;
    private Alert deleteConfirmation;
    private UserExchanger instanceUser;
    private User currentUser;
    private DatabaseControl database;
    private int selectedRoomNum;


    public void initialize () throws IOException, SQLException { // based on user`s data , after the FXML file loaded, configures visibility and startup settings.

        database = new DatabaseControl();
        instanceUser= UserExchanger.getInstance();
        currentUser = instanceUser.getInstanceUser();

        // Hide all buttons initially
        room1.setVisible(false);
        room2.setVisible(false);
        room3.setVisible(false);
        room4.setVisible(false);

        // Display the room buttons based on how many rooms the current user has
        switch (currentUser.getRooms().size()) {
            case 1 -> {
                room1.setText(currentUser.getRoom(1).getRoomName());
                instanceUser.setCurrentRoom(currentUser.getRoom(1));
                selectedRoomNum =1; // set selectedRoomNum so we know which hashmap index needs to be deleted
                room1.setVisible(true);
                displayRoomData();
            }
            case 2 -> {
                room1.setText(currentUser.getRoom(1).getRoomName());
                instanceUser.setCurrentRoom(currentUser.getRoom(1));
                selectedRoomNum =1;
                room1.setVisible(true);
                room2.setText(currentUser.getRoom(2).getRoomName());
                room2.setVisible(true);
                displayRoomData();
            }
            case 3 -> {
                room1.setText(currentUser.getRoom(1).getRoomName());
                instanceUser.setCurrentRoom(currentUser.getRoom(1));
                selectedRoomNum =1;
                room1.setVisible(true);
                room2.setText(currentUser.getRoom(2).getRoomName());
                room2.setVisible(true);
                room3.setText(currentUser.getRoom(3).getRoomName());
                room3.setVisible(true);
                displayRoomData();
            }
            case 4 -> {
                room1.setText(currentUser.getRoom(1).getRoomName());
                instanceUser.setCurrentRoom(currentUser.getRoom(1));
                selectedRoomNum =1;
                room1.setVisible(true);
                room2.setText(currentUser.getRoom(2).getRoomName());
                room2.setVisible(true);
                room3.setText(currentUser.getRoom(3).getRoomName());
                room3.setVisible(true);
                room4.setText(currentUser.getRoom(4).getRoomName());
                room4.setVisible(true);
                displayRoomData();
            }
        }

        deleteConfirmation= new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to delete " + instanceUser.getCurrentRoom().getRoomName() +"?", ButtonType.YES, ButtonType.NO);
        deleteConfirmation.setTitle("Confirm Delete");

        ObservableList<String> ageGroupOptions = FXCollections.observableArrayList(
                "0-6 Months",
                "6-12 Months",
                "12-24 Months"
        );
        AgeGroupPicker.setItems(ageGroupOptions);
    }

    @FXML
    void deleteRoom(ActionEvent event) throws SQLException, IOException {

        deleteConfirmation.showAndWait();
        if(deleteConfirmation.getResult()== ButtonType.YES) {
            currentUser.deleteRoom(selectedRoomNum);

            Parent root = FXMLLoader.load(getClass().getResource("editRooms.fxml")); //refresh editRooms screen so it initialises again and hides the button for the deleted room
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        }
    }

    @FXML
    void editRoom(ActionEvent event) throws SQLException {
        int maxCapacity = Integer.parseInt(MaxCapacity.getText());
        instanceUser.getCurrentRoom().updateRoom(currentUser.getUserID(), NewRoomName.getText(),AgeGroupPicker.getValue(),maxCapacity);

        switch (selectedRoomNum){ // update room button label in case its been changed
            case 1 -> {
                room1.setText(instanceUser.getCurrentRoom().getRoomName());
            }case 2 -> {
                room2.setText(instanceUser.getCurrentRoom().getRoomName());
            }case 3 -> {
                room3.setText(instanceUser.getCurrentRoom().getRoomName());
            }case 4 -> {
                room4.setText(instanceUser.getCurrentRoom().getRoomName());
            }
        }
    }

    @FXML
    void homeButtonClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("homeScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    void moveToRoom1(ActionEvent event) {
        instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRooms().get(1));
        selectedRoomNum =1; // set selectedRoomNum so we know which hashmap index needs to be deleted
        displayRoomData();
    }

    @FXML
    void moveToRoom2(ActionEvent event) {
        instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRooms().get(2));
        selectedRoomNum =2;
        displayRoomData();
    }

    @FXML
    void moveToRoom3(ActionEvent event) {
        instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRooms().get(3));
        selectedRoomNum =3;
        displayRoomData();
    }

    @FXML
    void moveToRoom4(ActionEvent event) {
        instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRooms().get(4));
        selectedRoomNum =4;
        displayRoomData();
    }

    void displayRoomData() {
       Room currentRoom = instanceUser.getCurrentRoom();
       NewRoomName.setText(currentRoom.getRoomName());
       AgeGroupPicker.setValue(currentRoom.isAgeGroup());
       String capacity = String.format("%d",currentRoom.getCapacity());
       MaxCapacity.setText(capacity);
    }
}
