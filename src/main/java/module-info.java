module org.example.envirobaby {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.eclipse.paho.client.mqttv3;
    requires org.controlsfx.controls;
    requires java.sql;


    opens org.example.envirobaby to javafx.fxml;
    exports org.example.envirobaby;
    exports org.example.envirobaby.app;
    opens org.example.envirobaby.app to javafx.fxml;
}