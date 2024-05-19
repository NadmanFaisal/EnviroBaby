package org.example.envirobaby.Interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.example.envirobaby.MQTT.MQTTSender;
import org.example.envirobaby.Entity.User;
import org.example.envirobaby.Entity.UserExchanger;

import java.io.IOException;

public class BuzzerScreenController {
    @FXML
    private Button playSoundButton;

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


    private MQTTSender sender;

    private UserExchanger instanceUser = UserExchanger.getInstance();
    private User currentUser = instanceUser.getInstanceUser();
    private SharedControllerFunctions setupFunctions = new SharedControllerFunctions();



    public void initialize () throws IOException, MqttException {

        this.sender = new MQTTSender();

        setupFunctions.setupRoomsDisplay(room1,room2,room3,room4,roomView);
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
        stage.resizableProperty().setValue(false);
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
        instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRoom(1));
        setupFunctions.resetButtons(room1,room2,room3,room4);
        room1.setDisable(true);
    }

    /**
     * Upon clicking with a mouse on the button "Room 2",
     * the screen triggeers room2 flag to be true,
     * indicating that the user is in room 2
     * @param event
     */

    public void moveToRoom2(ActionEvent event) {
        instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRoom(2));
        setupFunctions.resetButtons(room1,room2,room3,room4);
        room2.setDisable(true);
    }

    /**
     * Upon clicking with a mouse on the button "Room 3",
     * the screen triggeers room 3 flag to be true,
     * indicating that the user is in room 3.
     * @param event
     */

    public void moveToRoom3(ActionEvent event) {
        instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRoom(3));
        setupFunctions.resetButtons(room1,room2,room3,room4);
        room3.setDisable(true);
    }

    /**
     * Upon clicking with a mouse on the button "Room 4",
     * the screen triggeers room4 flag to be true,
     * indicating that the user is in room 4.
     * @param event
     */

    public void moveToRoom4(ActionEvent event) {
        instanceUser.setCurrentRoom(instanceUser.getInstanceUser().getRoom(4));
        setupFunctions.resetButtons(room1,room2,room3,room4);
        room4.setDisable(true);
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
