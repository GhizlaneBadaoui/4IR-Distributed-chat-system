package com.example.chatsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.Socket;

public class HomeInterface {

    @FXML
     private Button disconnectButton;

    @FXML
    private Button parametersButton;

    @FXML
    private Button sendButton;

    @FXML
    private Button sendPicture;

    @FXML
    private Button sendFile;

    @FXML
    private Button sendVoice;

    @FXML
    private Button reduceButton;

    @FXML
    private TextField searchField;

    @FXML
    private TextField messageLabel;

    @FXML
    private Label agentPseudo;


    Main objetMain = new Main();

    @FXML
    void disconnect (ActionEvent event){
        try {
            objetMain.changeScene("Login.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void displayParameters (ActionEvent event){
        try {
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void send (ActionEvent event){
        if (messageLabel.getText().isEmpty()) {
            System.out.println(agentPseudo.getText());
            System.out.println("message empty");
        } else {
            System.out.print(messageLabel.getText());

        }
        try {
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void reduceUser (ActionEvent event){
        try {
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void search () {
        try {
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
