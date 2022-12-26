package com.example.chatsystem;

import Controller.Database.DataSingleton;
import Controller.Threads.ListenConnThread;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static Controller.Database.Operations.*;

public class DiscussionController implements Initializable {

    @FXML
    private Label agentPseudo;

    @FXML
    private ImageView agentImg;

    @FXML
    private TextField messageLabel;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button sendButton;

    @FXML
    private Button sendFile;

    @FXML
    private Button sendPicture;

    @FXML
    private VBox vbox_messages;

    private SenderThread senderThread;

    DataSingleton data = DataSingleton.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connect();
        setAgentData();

        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                scrollPane.setVvalue((Double) newValue);
            }
        });

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

                    // add msg to db with actual date
                    setAgentData();
                    senderThread.start();
                    messageLabel.clear();

                }
            }
        });
        closeConnection();
    }

    private void setAgentData() {
        agentPseudo.setText(data.getPseudo());
        agentImg.setImage(data.getImg());

        List<String[]> tab = displayMessagesWithAgent(data.getPseudo());
        for (String[] element : tab){
            if (element[2].equals("R")) {
                addLabelForIncomingMessage(element[0], element[1], vbox_messages);
            }
            if (element[2].equals("S")){
                addLabelForOutgoingMessage(element[0], element[1], vbox_messages);
            }
        }
    }

    public static void addLabelForIncomingMessage(Object msg, String date, VBox vbox) {
        VBox primaryVbox = new VBox();
        primaryVbox.setAlignment(Pos.CENTER_LEFT);

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(5,5,5,10));

        Label dateLabel = new Label(date);
        dateLabel.setPadding(new Insets(5, 5, 5, 5));

        Text text = new Text((String) msg);
        TextFlow textflow = new TextFlow(text);
        textflow.setStyle("-fx-background-color: #85D6CA ;" +
                "-fx-background-radius: 0 20 20 20 ;");
        textflow.setPadding(new Insets(5, 10, 5, 10));

        hbox.getChildren().add(textflow);
        primaryVbox.getChildren().add(hbox);
        primaryVbox.getChildren().add(dateLabel);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vbox.getChildren().add(primaryVbox);
            }
        });
    }

    public static void addLabelForOutgoingMessage(Object msg, String date, VBox vbox) {
        VBox primaryVbox = new VBox();
        primaryVbox.setAlignment(Pos.CENTER_RIGHT);

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPadding(new Insets(5,5,5,10));

        Label dateLabel = new Label(date);
        dateLabel.setPadding(new Insets(5, 5, 5, 5));

        Text text = new Text((String) msg);
        TextFlow textflow = new TextFlow(text);

        textflow.setStyle("-fx-background-color: #8ED5F8;" +
                "-fx-background-radius: 20 0 20 20 ;");

        textflow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(0.934, 0.945, 0.996));

        hbox.getChildren().add(textflow);
        primaryVbox.getChildren().add(hbox);
        primaryVbox.getChildren().add(dateLabel);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vbox.getChildren().add(primaryVbox);
            }
        });
    }
}
