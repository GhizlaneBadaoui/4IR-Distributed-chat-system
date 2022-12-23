package com.example.chatsystem;

import Controller.Threads.ListenConnThread;
import Controller.Threads.ReceiverThread;
import Controller.Threads.SenderThread;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeInterface implements Initializable {

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

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox vbox_messages;

    private ReceiverThread receiverThread;
    private SenderThread senderThread;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String pseudo = agentPseudo.getText();
        Socket socket = ListenConnThread.getMap_sockets().get(pseudo);
        receiverThread = new ReceiverThread(socket, pseudo);

        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                scrollPane.setVvalue((Double) newValue);
            }
        });

        receiverThread.receiveMessage(vbox_messages);

        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String pseudo = agentPseudo.getText();
                Socket socket = ListenConnThread.getMap_sockets().get(pseudo);
                String messageToSend = messageLabel.getText();
                try {
                    senderThread = new SenderThread(socket, pseudo, messageToSend);
                } catch (IOException e) {
                    e.printStackTrace();                }

                if (!messageToSend.isEmpty()) {
                    HBox hbox = new HBox();
                    hbox.setAlignment(Pos.CENTER_RIGHT);
                    hbox.setPadding(new Insets(5,5,5,10));

                    Text text = new Text(messageToSend);
                    TextFlow textflow = new TextFlow(text);

                    textflow.setStyle("-fx-background-color: #8ED5F8;" +
                            "-fx-background-radius: 20px;");

                    textflow.setPadding(new Insets(5, 10, 5, 10));
                    text.setFill(Color.color(0.934, 0.945, 0.996));

                    hbox.getChildren().add(textflow);
                    vbox_messages.getChildren().add(hbox);

                    senderThread.sendMessage();
                    messageLabel.clear();

                }
            }
        });
    }

    public static void addLabel(Object msg, VBox vbox) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(5,5,5,10));

        Text text = new Text((String) msg);
        TextFlow textflow = new TextFlow(text);
        textflow.setStyle("-fx-background-color: #85D6CA" +
                "-fx-background-radius: 20px");
        textflow.setPadding(new Insets(5, 10, 5, 10));
        hbox.getChildren().add(textflow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vbox.getChildren().add(hbox);
            }
        });
    }
}
