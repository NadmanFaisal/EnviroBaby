package org.example.envirobaby.Interface;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import org.example.envirobaby.Entity.User;
import org.example.envirobaby.Entity.UserExchanger;

public class SharedControllerFunctions {


    private UserExchanger instanceUser = UserExchanger.getInstance();
    private User currentUser = instanceUser.getInstanceUser();


    public void setupRoomsDisplay(Button room1, Button room2, Button room3, Button room4,GridPane roomView) {
        room1.setVisible(false);
        room2.setVisible(false);
        room3.setVisible(false);
        room4.setVisible(false);
        roomView.setVisible(false); //if user has no rooms registered, do not show the page data related to specific rooms

        // Display the room buttons based on how many rooms the current user has
        switch (currentUser.getRooms().size()){
            case 1 -> {
                room1.setText(currentUser.getRoom(1).getRoomName());
                room1.setVisible(true);
                room1.setDisable(true);
            }
            case 2 -> {
                room1.setText(currentUser.getRoom(1).getRoomName());
                room1.setVisible(true);
                room1.setDisable(true);
                room2.setText(currentUser.getRoom(2).getRoomName());
                room2.setVisible(true);
            }
            case 3 -> {
                room1.setText(currentUser.getRoom(1).getRoomName());
                room1.setVisible(true);
                room1.setDisable(true);
                room2.setText(currentUser.getRoom(2).getRoomName());
                room2.setVisible(true);
                room3.setText(currentUser.getRoom(3).getRoomName());
                room3.setVisible(true);
            }
            case 4 -> {
                room1.setText(currentUser.getRoom(1).getRoomName());
                room1.setVisible(true);
                room1.setDisable(true);
                room2.setText(currentUser.getRoom(2).getRoomName());
                room2.setVisible(true);
                room3.setText(currentUser.getRoom(3).getRoomName());
                room3.setVisible(true);
                room4.setText(currentUser.getRoom(4).getRoomName());
                room4.setVisible(true);
            }
        }

        if (!currentUser.getRooms().isEmpty()) {
            instanceUser.setCurrentRoom(currentUser.getRoom(1)); //ensure first room viewed upon loading screen is always room1
            roomView.setVisible(true); //if the user has no rooms registered, do not display the options associated with rooms
        }
    }

    public void resetButtons(Button room1, Button room2, Button room3, Button room4){ //enable all buttons so that the new selected one is the only button seen as selected
        room1.setDisable(false);
        room2.setDisable(false);
        room3.setDisable(false);
        room4.setDisable(false);
    }
}
