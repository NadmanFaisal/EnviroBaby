package org.example.envirobaby.Interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import org.example.envirobaby.Entity.User;
import org.example.envirobaby.Entity.UserExchanger;

import java.io.IOException;

public class ViewRoomsController {


    @FXML
    private Button Room1_Button;

    @FXML
    private Button Room2_Button;

    @FXML
    private Button Room3_Button;

    @FXML
    private Button Room4_Button;
    @FXML
    GridPane overview;
    @FXML
    RowConstraints overviewRow;

    private UserExchanger instanceUser = UserExchanger.getInstance();
    private User currentUser = instanceUser.getInstanceUser();

    /** Initializes the controller and sets up the UI based on how many rooms the current user has
     *
     * @throws IOException If there is an error loading the FXML file
     */
    public void initialize () throws IOException {

        Room1_Button.setVisible(false);
        Room2_Button.setVisible(false);
        Room3_Button.setVisible(false);
        Room4_Button.setVisible(false);

        // Display the room buttons based on how many rooms the current user has
        switch (currentUser.getRooms().size()){
            case 1 -> {
                Room1_Button.setText(currentUser.getRoom(1).getRoomName());
                Room1_Button.setVisible(true);
            }
            case 2 -> {
                Room1_Button.setText(currentUser.getRoom(1).getRoomName());
                Room1_Button.setVisible(true);
                Room2_Button.setText(currentUser.getRoom(2).getRoomName());
                Room2_Button.setVisible(true);
            }
            case 3 -> {
                Room1_Button.setText(currentUser.getRoom(1).getRoomName());
                Room1_Button.setVisible(true);
                Room2_Button.setText(currentUser.getRoom(2).getRoomName());
                Room2_Button.setVisible(true);
                Room3_Button.setText(currentUser.getRoom(3).getRoomName());
                Room3_Button.setVisible(true);
            }
            case 4 -> {
                Room1_Button.setText(currentUser.getRoom(1).getRoomName());
                Room1_Button.setVisible(true);
                Room2_Button.setText(currentUser.getRoom(2).getRoomName());
                Room2_Button.setVisible(true);
                Room3_Button.setText(currentUser.getRoom(3).getRoomName());
                Room3_Button.setVisible(true);
                Room4_Button.setText(currentUser.getRoom(4).getRoomName());
                Room4_Button.setVisible(true);
            }
        }

        //Loads the room overview screen for the currently selected room, if there is one
        if (instanceUser.getCurrentRoom()!=null) {
            AnchorPane newLoadedPane = FXMLLoader.load(getClass().getResource("roomOverview.fxml"));
            overview.add(newLoadedPane,0,1);
            //Loads the room overview screen if the user has rooms saved but no room currently selected
        } else if (!currentUser.getRooms().isEmpty()) {
            instanceUser.setCurrentRoom(currentUser.getRoom(1)); //Sets the first room as the current room
            AnchorPane newLoadedPane = FXMLLoader.load(getClass().getResource("roomOverview.fxml"));
            overview.add(newLoadedPane,0,1);
        }
    }

    /** Loads the room overview screen
     *
     * @param event
     * @throws IOException If there is an error loading the FXML file
     */
    public void loadFxml (ActionEvent event) throws IOException {
        AnchorPane newLoadedPane = FXMLLoader.load(getClass().getResource("roomOverview.fxml"));
        overview.add(newLoadedPane,0,1);
    }

    public void Room1ButtonClick (ActionEvent event) throws IOException {
        instanceUser.setCurrentRoom(currentUser.getRoom(1));
        loadFxml(event);
    }

    public void Room2ButtonClick (ActionEvent event) throws IOException {
        instanceUser.setCurrentRoom(currentUser.getRoom(2));
        loadFxml(event);
    }

    public void Room3ButtonClick (ActionEvent event) throws IOException {
        instanceUser.setCurrentRoom(currentUser.getRoom(3));
        loadFxml(event);
    }

    public void Room4ButtonClick (ActionEvent event) throws IOException {
        instanceUser.setCurrentRoom(currentUser.getRoom(4));
        loadFxml(event);
    }

    public void homeButtonClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("homeScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}


