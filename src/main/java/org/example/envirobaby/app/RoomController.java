package org.example.envirobaby.app;
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
import javafx.stage.Stage;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.example.envirobaby.*;

import java.io.IOException;
import java.sql.SQLException;

public class RoomController {

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

    private OverviewManager roomOverview;
    private UserExchanger instanceUser;
    private Room currentRoom;
    private MQTTSender sender;
    private DatabaseControl database;
    private User currentUser;


    @FXML
    public void initialize() throws MqttException, SQLException, IOException { //Creates new subscriber object
        database = new DatabaseControl();
        instanceUser= UserExchanger.getInstance();
        currentRoom = instanceUser.getCurrentRoom();
        currentUser = instanceUser.getInstanceUser();
        sender = new MQTTSender();
        roomOverview = new OverviewManager(noiseLabel,tempLabel,humLabel, currentRoom); //initialise room object which implements runnable

        Thread thread = new Thread(roomOverview); //connect runnable to thread

        //initialize the data threshold boxes with stored room threshold data
        maxNoise.setText(String.valueOf(roomOverview.getUserRoom().getThresholds().getLoudThreshold()));
        minHumBox.setText(String.valueOf(roomOverview.getUserRoom().getThresholds().getHumLowerBound()));
        maxHumBox.setText(String.valueOf(roomOverview.getUserRoom().getThresholds().getHumUpperBound()));
        maxTempBox.setText(String.valueOf(roomOverview.getUserRoom().getThresholds().getTempUpperBound()));
        minTempBox.setText(String.valueOf(roomOverview.getUserRoom().getThresholds().getTempLowerBound()));
        roomCapLabel.setText("Capacity: " + currentRoom.getCapacity());


        if(currentUser.getSelectedDataView().equals("table")){ //display table view for all rooms if that's what the user has last selected
            AnchorPane newTablePane = FXMLLoader.load(getClass().getResource("dataTable.fxml"));
            dataGraphing.add(newTablePane, 1, 0);
            currentDataView = newTablePane;
        } else { //otherwise display whichever graph the user has last selected
            AnchorPane newGraphPane = FXMLLoader.load(getClass().getResource("dataGraph.fxml"));
            dataGraphing.add(newGraphPane, 1, 0);
            currentDataView = newGraphPane;
        }

        thread.start(); //start thread
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

    public void homeButtonClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("homeScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    public void convertToCelsius (ActionEvent actionEvent) throws MqttException, InterruptedException, SQLException, IOException {
        sender.sendMessage("C", "/envirobaby/tempunit");
        currentUser.setCelsius(true);
        database.updateTempUnit(currentUser.getUserID(),true);

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

        if(currentUser.getSelectedDataView().equals("temp")){ //set temp units for graph view
            displayTempGraph(actionEvent);
        } else if (currentUser.getSelectedDataView().equals("table")) { //else set for table view
            displayDataTable(actionEvent);
        }
    }

    @FXML
    public void displayDataTable(ActionEvent actionEvent) throws IOException {
        currentUser.setSelectedDataView("table"); // set for consistency on data view when switching rooms
        AnchorPane newTablePane = FXMLLoader.load(getClass().getResource("dataTable.fxml"));
        dataGraphing.getChildren().remove(currentDataView);
        dataGraphing.add(newTablePane, 1, 0);
        currentDataView=newTablePane;
    } //show table view

    @FXML
    public void displayLoudGraph(ActionEvent actionEvent) throws IOException {
        instanceUser.getInstanceUser().setSelectedDataView("loud");
        AnchorPane newPane = FXMLLoader.load(getClass().getResource("dataGraph.fxml"));
        dataGraphing.getChildren().remove(currentDataView);
        dataGraphing.add(newPane, 1, 0);
        currentDataView=newPane;
    } //show loudness graph view
    @FXML
    public void displayHumGraph(ActionEvent actionEvent) throws IOException {
        instanceUser.getInstanceUser().setSelectedDataView("hum");
        AnchorPane newPane = FXMLLoader.load(getClass().getResource("dataGraph.fxml"));
        dataGraphing.getChildren().remove(currentDataView);
        dataGraphing.add(newPane, 1, 0);
        currentDataView=newPane;
    } // show hum graph view
    @FXML
    public void displayTempGraph(ActionEvent actionEvent) throws IOException {
        instanceUser.getInstanceUser().setSelectedDataView("temp");
        AnchorPane newPane = FXMLLoader.load(getClass().getResource("dataGraph.fxml"));
        dataGraphing.getChildren().remove(currentDataView);
        dataGraphing.add(newPane, 1, 0);
        currentDataView=newPane;
    } // show temp graph view
}

