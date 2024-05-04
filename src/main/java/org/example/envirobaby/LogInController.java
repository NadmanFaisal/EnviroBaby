package org.example.envirobaby;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInController {

    @FXML
    private Button logInConfirm;

    @FXML
    private TextField password;

    @FXML
    private Button signUpButton;

    @FXML
    private TextField username;
    private Alert alert = new Alert(Alert.AlertType.WARNING);

    public void logIn(ActionEvent actionEvent) throws IOException {
        String userId = username.getText();
        String userPass = password.getText();

        if (!userId.isBlank() && !userPass.isBlank()) {
            if(userId.length()<=15) {
                homeScreenRedirect(actionEvent);
            } else {
                alert.setContentText("Username cannot be more than 15 characters");
                alert.show();
            }
        } else {
            alert.setContentText("Please enter your username and password");
            alert.show();
        }
    }

    @FXML
    public void signUpRedirect(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("signUp.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    public void homeScreenRedirect(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("homeScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setResizable(true);
        stage.setScene(scene);
    }
}

