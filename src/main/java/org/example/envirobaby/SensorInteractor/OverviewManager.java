package org.example.envirobaby.SensorInteractor;

import javafx.application.Platform;
import javafx.scene.control.Label;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.example.envirobaby.Entity.Room;
import org.example.envirobaby.Entity.UserExchanger;
import org.example.envirobaby.MQTT.MQTTSender;
import org.example.envirobaby.Notification.Notification;

import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OverviewManager implements Runnable{
    private Label noiseLabel;
    private Label tempLabel;
    private Label humLabel;
    private Label roomCapLabel;

    private Notification alerts;
    private Room room;
    private MQTTSender sender;
    private UserExchanger instanceUser = UserExchanger.getInstance();



    DecimalFormat df = new DecimalFormat("#.00");


    public OverviewManager(Label noiseLabel, Label tempLabel, Label humLabel, Room userRoom) throws MqttException {

        this.room = userRoom;
        this.alerts= new Notification();
        this.sender = new MQTTSender();


        this.noiseLabel=noiseLabel;
        this.tempLabel=tempLabel;
        this.humLabel=humLabel;
    }


    private void updateOverview() { //show sensor data on room overview page

        String tempMsg;
        if (!instanceUser.getInstanceUser().isCelsius()) {
            tempMsg = df.format(((room.getSensorReading().getTempValue() * (9/5)) + 32)) + "F";
        } else {
            tempMsg = df.format(room.getSensorReading().getTempValue()) + "C";
        }

        String noiseMsg = room.getSensorReading().getLoudValue() + " db";
        String humMsg = df.format(room.getSensorReading().getHumValue()) +"%";

        updateLabel(noiseLabel, noiseMsg);
        updateLabel(tempLabel,tempMsg);
        updateLabel(humLabel,humMsg);
    }




    private void updateLabel(Label label, String message) {
        if (label != null && message != null) {
            Platform.runLater(() -> label.setText(message));
        }
    }

    public Room getUserRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public void run() {
        // Create a ScheduledExecutorService for overviews
        ScheduledExecutorService overviewScheduler = Executors.newSingleThreadScheduledExecutor();

        // Schedule updateOverview with a delay
        overviewScheduler.scheduleAtFixedRate(this::updateOverview, 0, 100, TimeUnit.MILLISECONDS);
    }
}
