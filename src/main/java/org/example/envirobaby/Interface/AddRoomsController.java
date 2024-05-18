package org.example.envirobaby.Interface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.example.envirobaby.Entity.Room;
import org.example.envirobaby.Entity.User;
import org.example.envirobaby.Entity.UserExchanger;

import java.io.IOException;
import java.sql.SQLException;

public class AddRoomsController {

    public Label MaxCapacityLabel;
    public Label AgeGroupLabel;
    @FXML
    private ChoiceBox<String> AgeGroupPicker;

    @FXML
    private TextField MaxCapacity;

    @FXML
    private TextField NewRoomName;

    @FXML
    private Button SaveRoomButton;

    @FXML
    private TextField maxHumBox;

    @FXML
    private TextField maxNoise;

    @FXML
    private TextField maxTempBox;

    @FXML
    private TextField minHumBox;

    @FXML
    private TextField minTempBox;

    @FXML
    private Label noiseLabel;
    @FXML
    private Label tempLabel;
    @FXML
    private Label humLabel;

    private UserExchanger instanceUser;
    private User currentUser;
    private Alert alert = new Alert(Alert.AlertType.WARNING);

    /** Loads the "Add rooms" screen
     *
     * @param event
     * @throws IOException If  there is an error loading the FXML file
     */
    @FXML
    private void AddRoomsClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("addRooms.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    /** Initializes the controller, retrieves the current user and sets up age group options
     *
     */
    public void initialize () {
        instanceUser= UserExchanger.getInstance();
        currentUser = instanceUser.getInstanceUser();
        ObservableList<String> ageGroupOptions = FXCollections.observableArrayList(
                "0-6 Months",
                "6-12 Months",
                "12-24 Months"
        );
        AgeGroupPicker.setItems(ageGroupOptions);
    }

    private Room room;

    /** Creates a new room when the CreateRoom button is clicked and updates the UI
     *
     * @param event
     * @throws IOException If  there is an error loading the FXML file
     * @throws MqttException If there is an error with the MQTT connection
     * @throws SQLException If there is an error interacting with the database
     */
    public void handleCreateRoomButtonClick(ActionEvent event) throws IOException, MqttException, SQLException {
        if (currentUser.getRooms().size()<4) {
            //Gets the user input for the new rooms details
            String roomName = NewRoomName.getText();
            int capacity = Integer.parseInt(MaxCapacity.getText());
            String ageGroup = (String) AgeGroupPicker.getValue();
            int maxNoiseLevel = Integer.parseInt(maxNoise.getText());
            double maxHum = Double.parseDouble(maxHumBox.getText());
            double minHum = Double.parseDouble(minHumBox.getText());
            double maxTemp = Double.parseDouble(maxTempBox.getText());
            double minTemp = Double.parseDouble(minTempBox.getText());

           //Gets the current users ID
            UserExchanger userExchanger = UserExchanger.getInstance();
            User currentUser = userExchanger.getInstanceUser();
            String userId = currentUser.getUserID();

            //Creates the new room
            Room room = currentUser.createRoom(roomName, userId, capacity, ageGroup, maxNoiseLevel, maxTemp, minTemp, maxHum, minHum, currentUser.isCelsius());
            userExchanger.setCurrentRoom(room);

            //Loads the "View rooms" screen
            Parent root = FXMLLoader.load(getClass().getResource("viewRooms.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } else {
            alert.setContentText("You can only have four rooms at a time");
            alert.show();
        }
    }

    /** Loads the Home screen
     *
     * @param event
     * @throws IOException If  there is an error loading the FXML file
     */
    public void homeButtonClick (ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("homeScreen.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void setDefaultThresholds(ActionEvent event) {
        if(AgeGroupPicker.getValue().equals("0-6 Months")) {
            this.maxHumBox.setText("60");
            this.minHumBox.setText("45");
            this.minTempBox.setText("30");
            this.maxTempBox.setText("36");
            this.maxNoise.setText("65");
        } else if(AgeGroupPicker.getValue().equals("6-12 Months")) {
            this.maxHumBox.setText("60");
            this.minHumBox.setText("45");
            this.minTempBox.setText("30");
            this.maxTempBox.setText("40");
            this.maxNoise.setText("65");
        } else {
            this.maxHumBox.setText("60");
            this.minHumBox.setText("35");
            this.minTempBox.setText("20");
            this.maxTempBox.setText("42");
            this.maxNoise.setText("65");
        }
    }
}
