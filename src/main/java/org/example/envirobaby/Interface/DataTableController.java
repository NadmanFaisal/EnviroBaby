package org.example.envirobaby.Interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.envirobaby.Database.DatabaseControl;
import org.example.envirobaby.Entity.Record;
import org.example.envirobaby.Entity.Room;
import org.example.envirobaby.Entity.UserExchanger;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataTableController {

    @FXML
    private TableView<Record> records;
    @FXML
    private TableColumn<Record, String> recordTime;
    @FXML
    private TableColumn<Record, Integer> recordNoise;
    @FXML
    private TableColumn<Record, Double> recordTemp;
    @FXML
    private TableColumn<Record, Double> recordHum;
    @FXML
    private ComboBox<String> dateSelect;

    private DatabaseControl database;
    private UserExchanger instanceUser;
    private Room currentRoom;

    public void initialize () throws IOException, SQLException {
        database = new DatabaseControl();
        instanceUser = UserExchanger.getInstance();
        currentRoom = instanceUser.getCurrentRoom();

        setDateSelect();

        if(currentRoom.getRecordViewDate() ==null) { // if the user has yet to select a view date for the room, pick the most recent option available
            dateSelect.getSelectionModel().select(0);
        } else { //otherwise, display the selected date
            dateSelect.setValue(currentRoom.getRecordViewDate());
            setTable(dateSelect.getValue());
        }
    }

    public void setDateSelect() throws SQLException {
        currentRoom = instanceUser.getCurrentRoom();

        dateSelect.getItems().add("--Select Date--");
        ResultSet rs = database.recieveRecordDates(currentRoom.getUserId(), currentRoom.getRoomName());
        while (rs.next()) {
            String date = rs.getString("record_date");
            dateSelect.getItems().add(date);
        }
    } //set up combo box values

    public void setTable(String date) throws SQLException{
        instanceUser.getCurrentRoom().updateRecordList(date); //ensure most recent version of recordList

        recordTime.setCellValueFactory(new PropertyValueFactory<Record,String>("recordTime")); //set based on observable list column name
        recordNoise.setCellValueFactory(new PropertyValueFactory<Record,Integer>("recordNoise"));
        if (instanceUser.getInstanceUser().isCelsius()) { //display temp values depending on the users preferences
            recordTemp.setCellValueFactory(new PropertyValueFactory<Record, Double>("recordTemp"));
        } else {
            recordTemp.setCellValueFactory(new PropertyValueFactory<Record, Double>("recordTempF"));
        }
        recordHum.setCellValueFactory(new PropertyValueFactory<Record,Double>("recordHum"));

    records.setItems(instanceUser.getCurrentRoom().getRecords()); //display table using items from the observable array
    }

    public void updateDate(ActionEvent actionEvent) throws SQLException { //when user selects a date from the combobox
        if(!dateSelect.getValue().equals("--Select Date--")) {
            String date = dateSelect.getValue();
            instanceUser.getCurrentRoom().setRecordViewDate(date);
            instanceUser.getCurrentRoom().updateRecordList(date); //update observable array to contain records from that date
            records.setItems(instanceUser.currentRoom.getRecords()); //display records from that date
        }
    }
}
