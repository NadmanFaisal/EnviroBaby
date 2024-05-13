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

public class SignUpController {

    @FXML
    private TextField createPassword;

    @FXML
    private TextField createUsername;

    @FXML
    private Button logInButton;

    @FXML
    private Button signUpConfirm;

    private UserExchanger transferable; //transfers user object between classes
    private User user;
    private DatabaseControl database;
    private Alert alert = new Alert(Alert.AlertType.WARNING);

    public void signUp(ActionEvent actionEvent) throws IOException, SQLException, MqttException {
        String userId = createUsername.getText();
        String userPass = createPassword.getText();
        database= new DatabaseControl();

        // check if sign up conditions are met
        if (!userId.isBlank() && !userPass.isBlank()) {
            if (userId.length() <= 15) {
                boolean signUpSuccessful = database.signUpUser(userId,userPass);
                if (signUpSuccessful) {  //checks if username is available and creates new User row in database if it is
                    user = new User(userId);
                    transferable = UserExchanger.getInstance(); //initialize instance
                    transferable.setInstanceUser(user); // store user in instance to be accessed by all classes
                    homeScreenRedirect(actionEvent);
                } else {
                    alert.setContentText("This username is already in use");
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
    public void logInRedirect(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("logIn.fxml")); // switch to log in screen
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    public void homeScreenRedirect(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("homeScreen.fxml")); // forward to homescreen
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setResizable(true); //allow screen to be resized post sign in
        stage.setScene(scene);
    }
}
