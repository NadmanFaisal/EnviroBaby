package org.example.envirobaby;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NoiseScreen extends Application {


    public void start(Stage envb) throws IOException {

        // Loads the scene builder FXML file and all of its UI components
        FXMLLoader file = new FXMLLoader(getClass().getResource("noise-view.fxml"));
        Parent root = file.load();

        // The container for all the UI component
        Scene scene = new Scene(root);

        envb.setScene(scene);
        envb.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
