package org.example.envirobaby.app;

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
import org.eclipse.paho.client.mqttv3.MqttException;
import org.example.envirobaby.DatabaseControl;
import org.example.envirobaby.User;
import org.example.envirobaby.UserExchanger;

import java.io.IOException;
import java.sql.SQLException;

public class LogInController {

    @FXML
    private Button logInConfirm;

    @FXML
    private TextField password;

    @FXML
    private Button signUpButton;

    @FXML
    private TextField username;

    private DatabaseControl database;
    private User user;
    private UserExchanger transferable; //transfers user object between classes
    private Alert alert = new Alert(Alert.AlertType.WARNING);

    public void logIn(ActionEvent actionEvent) throws IOException, SQLException, MqttException {
        String userId = username.getText();
        String userPass = password.getText();

        //check if log in conditions are met
        if (!userId.isBlank() && !userPass.isBlank()) {
            if(userId.length()<=15) {
                database = new DatabaseControl();

                if (database.attemptLogIn(userId, userPass)) { //checks if log in is successful
                    user = new User(userId);
                    transferable = UserExchanger.getInstance(); //initialize instance
                    transferable.setInstanceUser(user); // store user in instance to be accessed by all classes
                    homeScreenRedirect(actionEvent);
                } else {
                    alert.setContentText("Incorrect username or password");
                    alert.show();
                }
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
    public void signUpRedirect(ActionEvent event) throws IOException { //switch to sighn up screen
        Parent root = FXMLLoader.load(getClass().getResource("signUp.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    public void homeScreenRedirect(ActionEvent event) throws IOException { //forward to homeScreen
        Parent root = FXMLLoader.load(getClass().getResource("homeScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setResizable(true); //allow changing application size
        stage.setScene(scene);
    }
}

