module org.example.envirobaby {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.eclipse.paho.client.mqttv3;
    requires org.controlsfx.controls;


    opens org.example.envirobaby to javafx.fxml;
    exports org.example.envirobaby;
}