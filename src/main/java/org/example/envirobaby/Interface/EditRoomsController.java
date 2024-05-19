package org.example.envirobaby.Interface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.example.envirobaby.Database.DatabaseControl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.example.envirobaby.Entity.Room;
import org.example.envirobaby.Entity.User;
import org.example.envirobaby.Entity.UserExchanger;

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
    @FXML
    private GridPane roomView;

    private Alert deleteConfirmation;
    private UserExchanger instanceUser;
    private User currentUser;
    private DatabaseControl database;
    private int selectedRoomNum;
    private SharedControllerFunctions setupFunctions = new SharedControllerFunctions();


    public void initialize () throws IOException, SQLException { // based on user`s data , after the FXML file loaded, configures visibility and startup settings.

        database = new DatabaseControl();
        instanceUser= UserExchanger.getInstance();
        currentUser = instanceUser.getInstanceUser();

        setupFunctions.setupRoomsDisplay(room1,room2,room3,room4,roomView);

        //Loads the room overview screen for the currently selected room, if there is one
        if (!currentUser.getRooms().isEmpty()) {
            selectedRoomNum=1;
            ObservableList<String> ageGroupOptions = FXCollections.observableArrayList(
                    "0-6 Months",
                    "6-12 Months",
                    "12-24 Months"
            );
            AgeGroupPicker.setItems(ageGroupOptions);
            displayRoomData();
        }

        deleteConfirmation= new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to delete " + instanceUser.getCurrentRoom().getRoomName() +"?", ButtonType.YES, ButtonType.NO);
        deleteConfirmation.setTitle("Confirm Delete");

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
        if (currentUser.getRooms().isEmpty()){
            roomView.setVisible(false);
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
        setupFunctions.resetButtons(room1,room2,room3,room4);
        room1.setDisable(true);
        displayRoomData();
    }

    @FXML
    void moveToRoom2(ActionEvent event) {
        instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRooms().get(2));
        selectedRoomNum =2;
        setupFunctions.resetButtons(room1,room2,room3,room4);
        room2.setDisable(true);
        displayRoomData();
    }

    @FXML
    void moveToRoom3(ActionEvent event) {
        instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRooms().get(3));
        selectedRoomNum =3;
        setupFunctions.resetButtons(room1,room2,room3,room4);
        room3.setDisable(true);
        displayRoomData();
    }

    @FXML
    void moveToRoom4(ActionEvent event) {
        instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRooms().get(4));
        selectedRoomNum =4;
        setupFunctions.resetButtons(room1,room2,room3,room4);
        room4.setDisable(true);
        displayRoomData();
    }
    public void addRoomClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("addRooms.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    void displayRoomData() {
       Room currentRoom = instanceUser.getCurrentRoom();
       NewRoomName.setText(currentRoom.getRoomName());
       AgeGroupPicker.setValue(currentRoom.isAgeGroup());
       String capacity = String.format("%d",currentRoom.getCapacity());
       MaxCapacity.setText(capacity);
    }

}
