package org.example.envirobaby.Interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.example.envirobaby.Database.DatabaseControl;
import org.example.envirobaby.Entity.Room;
import org.example.envirobaby.Entity.User;
import org.example.envirobaby.Entity.UserExchanger;
import org.example.envirobaby.MQTT.MQTTSender;
import org.example.envirobaby.SensorInteractor.OverviewManager;

import java.io.IOException;
import java.sql.SQLException;

public class ViewRoomsController {
    

    @FXML
    private Button room1;

    @FXML
    private Button room2;

    @FXML
    private Button room3;

    @FXML
    private Button room4;

    @FXML
    GridPane overview;
    @FXML
    RowConstraints overviewRow;
    @FXML
    private Label noiseLabel;
    @FXML
    private TextField maxNoise;
    @FXML
    private Label tempLabel;
    @FXML
    private Label humLabel;
    @FXML
    private TextField minHumBox;
    @FXML
    private TextField maxHumBox;
    @FXML
    private TextField maxTempBox;
    @FXML
    private TextField minTempBox;
    @FXML
    private Label roomCapLabel;
    @FXML
    private GridPane dataGraphing;
    @FXML
    private AnchorPane currentDataView;
    @FXML
    private Button tempGraph;
    @FXML
    private Button humGraph;
    @FXML
    private Button loudGraph;
    @FXML
    private Button dataTable;
    @FXML
    private Button celciusButton;
    @FXML
    private Button fahrenButton;
    @FXML
    private GridPane roomView;
    @FXML
    private Button defaultThresholds;


    private OverviewManager roomOverview;
    private Room currentRoom;
    private MQTTSender sender;
    private DatabaseControl database;

    private UserExchanger instanceUser = UserExchanger.getInstance();
    private User currentUser = instanceUser.getInstanceUser();
    private SharedControllerFunctions setupFunctions = new SharedControllerFunctions();

    /** Initializes the controller and sets up the UI based on how many rooms the current user has
     *
     * @throws IOException If there is an error loading the FXML file
     */
    public void initialize () throws IOException, SQLException, MqttException, InterruptedException {
        setupFunctions.setupRoomsDisplay(room1,room2,room3,room4,roomView);

        //Loads the room overview screen for the currently selected room, if there is one
        if (!currentUser.getRooms().isEmpty()) {
            setRoomDisplay();
        }
    }

    /** Loads the room overview screen
     *
     * @param event
     * @throws IOException If there is an error loading the FXML file
     */

    public void Room1ButtonClick (ActionEvent event) throws IOException, SQLException, MqttException, InterruptedException {
        instanceUser.setCurrentRoom(currentUser.getRoom(1));
        setupFunctions.resetButtons(room1,room2,room3,room4);
        room1.setDisable(true);
        roomOverview.stopOverview();
        setRoomDisplay();
    }

    public void Room2ButtonClick () throws IOException, SQLException, MqttException, InterruptedException {
        instanceUser.setCurrentRoom(currentUser.getRoom(2));
        setupFunctions.resetButtons(room1,room2,room3,room4);
        room2.setDisable(true);
        roomOverview.stopOverview();
        setRoomDisplay();
    }

    public void Room3ButtonClick (ActionEvent event) throws IOException, SQLException, MqttException, InterruptedException {
        instanceUser.setCurrentRoom(currentUser.getRoom(3));
        setupFunctions.resetButtons(room1,room2,room3,room4);
        room3.setDisable(true);
        roomOverview.stopOverview();
        setRoomDisplay();
    }

    public void Room4ButtonClick (ActionEvent event) throws IOException, SQLException, MqttException, InterruptedException {
        instanceUser.setCurrentRoom(currentUser.getRoom(4));
        setupFunctions.resetButtons(room1,room2,room3,room4);
        room4.setDisable(true);
        roomOverview.stopOverview();
        setRoomDisplay();
    }

    public void homeButtonClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("homeScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void addRoomClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("addRooms.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void setRoomDisplay() throws SQLException, MqttException, IOException {
        database = new DatabaseControl();
        currentRoom = instanceUser.getCurrentRoom();
        sender = new MQTTSender();
        roomOverview = new OverviewManager(noiseLabel,tempLabel,humLabel, currentRoom); //initialise room object which implements runnable
        roomOverview.runOverview();

        //initialize the data threshold boxes with stored room threshold data
        maxNoise.setText(String.valueOf(roomOverview.getUserRoom().getThresholds().getLoudThreshold()));
        minHumBox.setText(String.valueOf(roomOverview.getUserRoom().getThresholds().getHumLowerBound()));
        maxHumBox.setText(String.valueOf(roomOverview.getUserRoom().getThresholds().getHumUpperBound()));
        maxTempBox.setText(String.valueOf(roomOverview.getUserRoom().getThresholds().getTempUpperBound()));
        minTempBox.setText(String.valueOf(roomOverview.getUserRoom().getThresholds().getTempLowerBound()));
        roomCapLabel.setText("Capacity: " + currentRoom.getCapacity());

        if(currentUser.isCelsius()) {
            celciusButton.setDisable(true);
        } else {
            fahrenButton.setDisable(true);
        }


        if(currentUser.getSelectedDataView().equals("table")){ //display table view for all rooms if that's what the user has last selected
            AnchorPane newTablePane = FXMLLoader.load(getClass().getResource("dataTable.fxml"));
            dataGraphing.getChildren().remove(currentDataView);
            dataGraphing.add(newTablePane, 1, 0);
            currentDataView = newTablePane;
            dataTable.setDisable(true);
        } else { //otherwise display whichever graph the user has last selected
            AnchorPane newGraphPane = FXMLLoader.load(getClass().getResource("dataGraph.fxml"));
            dataGraphing.getChildren().remove(currentDataView);
            dataGraphing.add(newGraphPane, 1, 0);
            currentDataView = newGraphPane;
            switch (currentUser.getSelectedDataView()) {
                case "temp"->{
                    tempGraph.setDisable(true);
                }
                case "hum"->{
                    humGraph.setDisable(true);
                }
                case "loud"->{
                    loudGraph.setDisable(true);
                }
            }
        }
    }
    @FXML
    public void updateNoiseUpperBound(ActionEvent actionEvent) throws SQLException { // controller class method used in FXML file that handles and action event
        roomOverview.getUserRoom().updateThreshold(maxNoise);
    }
    @FXML
    public void updateTempUbound(ActionEvent actionEvent) throws SQLException {
        roomOverview.getUserRoom().updateThreshold(maxTempBox);;
    }
    @FXML
    public void updateTempLbound(ActionEvent actionEvent) throws SQLException {
        roomOverview.getUserRoom().updateThreshold(minTempBox);;
    }

    @FXML
    public void updateMinHum(ActionEvent actionEvent) throws SQLException {
        roomOverview.getUserRoom().updateThreshold(minHumBox);
    }

    @FXML
    public void updateMaxHum (ActionEvent actionEvent) throws SQLException {
        roomOverview.getUserRoom().updateThreshold(maxHumBox);
    }

    @FXML
    public void convertToCelsius (ActionEvent actionEvent) throws MqttException, InterruptedException, SQLException, IOException {
        sender.sendMessage("C", "/envirobaby/tempunit");
        currentUser.setCelsius(true);
        database.updateTempUnit(currentUser.getUserID(),true);
        celciusButton.setDisable(true);
        fahrenButton.setDisable(false);


        if(currentUser.getSelectedDataView().equals("temp")){ //set temp units for graph view
            displayTempGraph(actionEvent);
        } else if (currentUser.getSelectedDataView().equals("table")) { //else, set for table view
            displayDataTable(actionEvent);
        }
    }

    @FXML
    public void convertToFahrenheit (ActionEvent actionEvent) throws MqttException, InterruptedException, SQLException, IOException {
        sender.sendMessage("F", "/envirobaby/tempunit");
        currentUser.setCelsius(false);
        database.updateTempUnit(currentUser.getUserID(),false);
        fahrenButton.setDisable(true);
        celciusButton.setDisable(false);

        if(currentUser.getSelectedDataView().equals("temp")){ //set temp units for graph view
            displayTempGraph(actionEvent);
        } else if (currentUser.getSelectedDataView().equals("table")) { //else set for table view
            displayDataTable(actionEvent);
        }
    }

    @FXML
    public void displayDataTable(ActionEvent actionEvent) throws IOException {
        currentUser.setSelectedDataView("table"); // set for consistency on data view when switching rooms
        tempGraph.setDisable(false);
        loudGraph.setDisable(false);
        humGraph.setDisable(false);
        dataTable.setDisable(true);
        AnchorPane newTablePane = FXMLLoader.load(getClass().getResource("dataTable.fxml"));
        dataGraphing.getChildren().remove(currentDataView);
        dataGraphing.add(newTablePane, 1, 0);
        currentDataView=newTablePane;
    } //show table view

    @FXML
    public void displayLoudGraph(ActionEvent actionEvent) throws IOException {
        instanceUser.getInstanceUser().setSelectedDataView("loud");
        tempGraph.setDisable(false);
        loudGraph.setDisable(true);
        humGraph.setDisable(false);
        dataTable.setDisable(false);
        AnchorPane newPane = FXMLLoader.load(getClass().getResource("dataGraph.fxml"));
        dataGraphing.getChildren().remove(currentDataView);
        dataGraphing.add(newPane, 1, 0);
        currentDataView=newPane;
    } //show loudness graph view
    @FXML
    public void displayHumGraph(ActionEvent actionEvent) throws IOException {
        instanceUser.getInstanceUser().setSelectedDataView("hum");
        tempGraph.setDisable(false);
        loudGraph.setDisable(false);
        humGraph.setDisable(true);
        dataTable.setDisable(false);
        AnchorPane newPane = FXMLLoader.load(getClass().getResource("dataGraph.fxml"));
        dataGraphing.getChildren().remove(currentDataView);
        dataGraphing.add(newPane, 1, 0);
        currentDataView=newPane;
    } // show hum graph view
    @FXML
    public void displayTempGraph(ActionEvent actionEvent) throws IOException {
        instanceUser.getInstanceUser().setSelectedDataView("temp");
        tempGraph.setDisable(true);
        loudGraph.setDisable(false);
        humGraph.setDisable(false);
        dataTable.setDisable(false);
        AnchorPane newPane = FXMLLoader.load(getClass().getResource("dataGraph.fxml"));
        dataGraphing.getChildren().remove(currentDataView);
        dataGraphing.add(newPane, 1, 0);
        currentDataView=newPane;
    } // show temp graph view

    @FXML
    public void setDefaultThresholds(ActionEvent actionEvent) {
        currentRoom.getThresholds().resetThresholds();
        maxNoise.setText(String.valueOf(currentRoom.getThresholds().getLoudThreshold()));
        minHumBox.setText(String.valueOf(currentRoom.getThresholds().getHumLowerBound()));
        maxHumBox.setText(String.valueOf(currentRoom.getThresholds().getHumUpperBound()));
        maxTempBox.setText(String.valueOf(currentRoom.getThresholds().getTempUpperBound()));
        minTempBox.setText(String.valueOf(currentRoom.getThresholds().getTempLowerBound()));
    }
}


