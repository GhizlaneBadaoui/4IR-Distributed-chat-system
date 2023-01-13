package com.example.chatsystem;

import Controller.Database.DataSingleton;
import Model.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AgentItemController implements Initializable {

    @FXML
    private Label incomingMessages;

    @FXML
    private Button openDiscussionButton;

    @FXML
    private ImageView photo;

    @FXML
    private Label pseudo;

    DataSingleton data = DataSingleton.getInstance();

    public void setData (User agent) {
        //photo.setImage(new Image(agent.getImgSrc()));
        pseudo.setText(agent.getPseudo());
        incomingMessages.setText("0");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        openDiscussionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {

                    data.setPseudo(pseudo.getText());
                    data.setImg(photo.getImage());

                    Stage secondStage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Discussion.fxml"));
                    secondStage.setTitle("Chat System");
                    secondStage.getIcons().add(new Image("file:src/main/resources/Images/logo.png"));
                    secondStage.initModality(Modality.APPLICATION_MODAL);
                    secondStage.setScene(new Scene(fxmlLoader.load(),443, 600));
                    secondStage.resizableProperty().setValue(false);
                    secondStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
