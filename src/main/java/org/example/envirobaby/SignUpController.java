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

public class SignUpController {

    @FXML
    private TextField createPassword;

    @FXML
    private TextField createUsername;

    @FXML
    private Button logInButton;

    @FXML
    private Button signUpConfirm;
    private Alert alert = new Alert(Alert.AlertType.WARNING);

    public void signUp(ActionEvent actionEvent) throws IOException {
        String userId = createUsername.getText();
        String userPass = createPassword.getText();

        if (!userId.isBlank() && !userPass.isBlank()) {
            if (userId.length() <= 15) {
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
    public void logInRedirect(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("logIn.fxml"));
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
