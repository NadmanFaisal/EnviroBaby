package org.example.envirobaby.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EnviroBabyApp extends Application {


    public void start(Stage envb) throws IOException {
        // Loads the scene builder FXML file and all of its UI components
        FXMLLoader file = new FXMLLoader(getClass().getResource("logIn.fxml"));

        Parent root = file.load();

        // The container for all the UI component
        Scene scene = new Scene(root);

        envb.setScene(scene);
        envb.setResizable(false); //force screen to stay the same size for log in
        envb.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
