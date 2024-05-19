module org.example.envirobaby {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.eclipse.paho.client.mqttv3;
    requires org.controlsfx.controls;
    requires java.sql;
    requires javafx.media;


    exports org.example.envirobaby.Interface;
    opens org.example.envirobaby.Interface to javafx.fxml;
    exports org.example.envirobaby.Database;
    opens org.example.envirobaby.Database to javafx.fxml;
    exports org.example.envirobaby.MQTT;
    opens org.example.envirobaby.MQTT to javafx.fxml;
    exports org.example.envirobaby.Entity;
    opens org.example.envirobaby.Entity to javafx.fxml;
    exports org.example.envirobaby.SensorInteractor;
    opens org.example.envirobaby.SensorInteractor to javafx.fxml;
    exports org.example.envirobaby.Notification;
    opens org.example.envirobaby.Notification to javafx.fxml;
}