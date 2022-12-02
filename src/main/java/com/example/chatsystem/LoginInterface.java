package com.example.chatsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginInterface {

    @FXML
    private TextField pseudoLabel;

    @FXML
    private Label connectionError;

    @FXML
    private Button logInButton;

    Main objetMain = new Main();


    /* Connection : check pseudo and connect */
    @FXML
    void LogIn (ActionEvent event){
        try {
            if (pseudoLabel.getText().contentEquals("Ghizlane")) {
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
