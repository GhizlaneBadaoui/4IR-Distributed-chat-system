package com.example.chatsystem;

import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.InetAddress;

public class LoginInterface {

    @FXML
    private TextField pseudoLabel;

    @FXML
    private Label connectionError;

    @FXML
    private Button logInButton;

    public static User user;

    Main objetMain = new Main();


    /* Connection : check pseudo and connect */
    @FXML
    void LogIn (ActionEvent event){
        try {
            user = new User(InetAddress.getLocalHost(),5009);
            if (user.choose_pseudo(pseudoLabel.getText())) {
                user.setPseudo(pseudoLabel.getText());
                connectionError.setText("Succes !");
                objetMain.changeScene("Home.fxml");
            }
            else {
                connectionError.setText("Error !");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
