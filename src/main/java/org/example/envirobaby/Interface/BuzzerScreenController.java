package org.example.envirobaby.Interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.example.envirobaby.MQTT.MQTTSender;
import org.example.envirobaby.Entity.User;
import org.example.envirobaby.Entity.UserExchanger;

import java.io.IOException;

public class BuzzerScreenController {
    public Button room1;
    public Button room2;
    public Button room4;
    public Button room3;
    public Button playSoundButton;
    public Button stopSoundButton;
    private MQTTSender sender;

    private UserExchanger instanceUser = UserExchanger.getInstance();
    private User currentUser = instanceUser.getInstanceUser();

    private static final int FIRST_ROOM = 1;
    private static final int SECOND_ROOM = 2;
    private static final int THIRD_ROOM = 3;
    private static final int FOURTH_ROOM = 4;


    public void initialize () throws IOException, MqttException {

        this.sender = new MQTTSender();

        room1.setVisible(false);
        room2.setVisible(false);
        room3.setVisible(false);
        room4.setVisible(false);

        playSoundButton.setVisible(false);
        stopSoundButton.setVisible(false);

        // Display the room buttons based on how many rooms the current user has
        switch (currentUser.getRooms().size()) {
            case 1 -> {
                room1.setText(currentUser.getRoom(FIRST_ROOM).getRoomName());
                room1.setVisible(true);
                playSoundButton.setVisible(true);
                stopSoundButton.setVisible(true);

                instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRoom(FIRST_ROOM));
            }
            case 2 -> {
                room1.setText(currentUser.getRoom(FIRST_ROOM).getRoomName());
                room1.setVisible(true);
                room2.setText(currentUser.getRoom(SECOND_ROOM).getRoomName());
                room2.setVisible(true);
                playSoundButton.setVisible(true);
                stopSoundButton.setVisible(true);

                instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRoom(FIRST_ROOM));
            }
            case 3 -> {
                room1.setText(currentUser.getRoom(FIRST_ROOM).getRoomName());
                room1.setVisible(true);
                room2.setText(currentUser.getRoom(SECOND_ROOM).getRoomName());
                room2.setVisible(true);
                room3.setText(currentUser.getRoom(THIRD_ROOM).getRoomName());
                room3.setVisible(true);
                playSoundButton.setVisible(true);
                stopSoundButton.setVisible(true);

                instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRoom(FIRST_ROOM));
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
                playSoundButton.setVisible(true);
                stopSoundButton.setVisible(true);

                instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRoom(FIRST_ROOM));
            }
        }
    }

    /**
     * Upon clicking with a mouse on the button "Room 1",
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
     * Upon clicking with a mouse on the button "Room 2",
     * the screen triggeers room1 flag to be true,
     * indicating that the user is in room 1
     * @param event
     */
    public void moveToRoom1(ActionEvent event) {
        instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRoom(FIRST_ROOM));
    }

    /**
     * Upon clicking with a mouse on the button "Room 2",
     * the screen triggeers room2 flag to be true,
     * indicating that the user is in room 2
     * @param event
     */

    public void moveToRoom2(ActionEvent event) {
        instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRoom(SECOND_ROOM));
    }

    /**
     * Upon clicking with a mouse on the button "Room 3",
     * the screen triggeers room 3 flag to be true,
     * indicating that the user is in room 3.
     * @param event
     */

    public void moveToRoom3(ActionEvent event) {
        instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRoom(THIRD_ROOM));
    }

    /**
     * Upon clicking with a mouse on the button "Room 4",
     * the screen triggeers room4 flag to be true,
     * indicating that the user is in room 4.
     * @param event
     */

    public void moveToRoom4(ActionEvent event) {
        instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRoom(FOURTH_ROOM));
    }

    /**
     * Sends messages to certain topic defined
     * inside the body of the method.
     */
    public void sendMQTTMessage(String message) throws MqttException, InterruptedException {
        String topicSection = "/envirobaby/room";
        String topic = topicSection + instanceUser.getCurrentRoom().getTerminalTopic() + "/buzzer";
        sender.sendMessage(message, topic);
    }

    /**
     * When the "Play sound" button is clicked, it publishes
     * message "BUZZ" message to certain topic so that the
     * WIO terminal starts buzzing.
     * @param actionEvent
     * @throws MqttException
     * @throws InterruptedException
     */
    public void playSound(ActionEvent actionEvent) throws MqttException, InterruptedException {
        sendMQTTMessage("BUZZ");
    }

    /**
     * When the "Play sound" button is clicked, it publishes
     * message "BUZZ" message to certain topic so that the
     * WIO terminal starts buzzing.
     * @param actionEvent
     * @throws MqttException
     * @throws InterruptedException
     */
    public void stopSound(ActionEvent actionEvent) throws MqttException, InterruptedException {
        sendMQTTMessage("STOP");
    }

}
