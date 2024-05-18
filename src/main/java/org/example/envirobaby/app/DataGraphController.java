package org.example.envirobaby.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import org.example.envirobaby.DatabaseControl;
import org.example.envirobaby.Room;
import org.example.envirobaby.UserExchanger;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataGraphController {
    @FXML
    private ComboBox<String> dateSelect;
    @FXML
    private LineChart<String, Number> recordsGraph;

    private DatabaseControl database;
    private UserExchanger currentUser;
    private Room currentRoom;


    public void initialize () throws IOException, SQLException {
        currentUser= UserExchanger.getInstance();
        database = new DatabaseControl();
        setDateSelect();

        if(currentRoom.getRecordViewDate() ==null) { //if user has yet to select a date for the room, go with most recent record
            dateSelect.getSelectionModel().select(0);
        } else {  //else display selected date's records
            dateSelect.setValue(currentRoom.getRecordViewDate());
            setRecordsGraph(dateSelect.getValue());
        }

    }

    public void setRecordsGraph(String date) throws SQLException {
        currentUser.getCurrentRoom().updateRecordList(date); //ensure recordList is most recent version for selected date
        currentRoom = currentUser.getCurrentRoom();

        XYChart.Data<String,Number> plotGraph;

        XYChart.Series graphData = new XYChart.Series();
        String selectedView = currentUser.getInstanceUser().getSelectedDataView();


            switch (selectedView) {
                case "loud" -> {  //if selectedDataView is the loud graph, set data to display noise records
                    for (int i = 0; i < currentRoom.getRecords().size(); i++) { // for each record
                        String time = currentRoom.getRecords().get(i).getRecordTime();
                        int noise = currentRoom.getRecords().get(i).getRecordNoise();
                        plotGraph = new XYChart.Data<>(time, noise);
                        graphData.getData().add(plotGraph);
                    }
                }
                case "hum" -> { //else if selectedDataView is the hum graph, set data to display hum records
                    for (int i = 0; i < currentRoom.getRecords().size(); i++) { // for each record
                        String time = currentRoom.getRecords().get(i).getRecordTime();
                        double hum = currentRoom.getRecords().get(i).getRecordHum();
                        plotGraph = new XYChart.Data<>(time, hum);
                        graphData.getData().add(plotGraph);
                    }
                }
                case "temp" -> {  //else if selectedDataView is the temp graph, set data to display temp records

                    if (currentUser.getInstanceUser().isCelsius()) { //display temp values depending on temperature unit format

                        for (int i = 0; i < currentRoom.getRecords().size(); i++) { // for each record
                            String time = currentRoom.getRecords().get(i).getRecordTime();
                            double tempC = currentRoom.getRecords().get(i).getRecordTemp();
                            plotGraph = new XYChart.Data<>(time, tempC);
                            graphData.getData().add(plotGraph);
                        }
                    } else {

                        for (int i = 0; i < currentRoom.getRecords().size(); i++) { // for each record
                            String time = currentRoom.getRecords().get(i).getRecordTime();
                            double tempF = currentRoom.getRecords().get(i).getRecordTempF();
                            plotGraph = new XYChart.Data<>(time, tempF);
                            graphData.getData().add(plotGraph);
                        }
                    }
                }
            }
            recordsGraph.getData().setAll(graphData); //plot graph
        }

    public void setDateSelect() throws SQLException {
        currentRoom = currentUser.getCurrentRoom();

        dateSelect.getItems().add("--Select Date--");
        ResultSet rs = database.recieveRecordDates(currentRoom.getUserId(), currentRoom.getRoomName());
        while (rs.next()) {
            String date = rs.getString("record_date");
            dateSelect.getItems().add(date);
        }
    } //set up combo box values //set up combo box values

    @FXML
    public void updateDate(ActionEvent actionEvent) throws SQLException {
        if (!dateSelect.getValue().equals("--Select Date--")) {
            String date = dateSelect.getValue();
            currentUser.getCurrentRoom().setRecordViewDate(date);
            setRecordsGraph(date);
        }
    } //show graph depicting data from selected date
}
