package com.example.chatsystem;

import Model.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomeInterface implements Initializable {

    @FXML
    private Button EditButton;

    @FXML
    private Button disconnectButton;

    @FXML
    private TextField name;

    @FXML
    private ImageView imgProfile;

    @FXML
    private Label ipAddress;

    @FXML
    private Label port;

    @FXML
    private Label pseudo;

    @FXML
    private ScrollPane contactsScrollPane;

    @FXML
    private VBox vboxListAgent;

    @FXML
    private ScrollPane groupsScrollPane;

    @FXML
    private VBox vboxListGroups;

    @FXML
    private Button createGroup;

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
    void editInformation(ActionEvent event) {
        //User.setName(name.getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<AgentItemController> aicList = new ArrayList<>();
//        pseudo.setText(User.getPseudo());
//        ipAddress.setText(User.getIP());
//        port.setText(User.getPort());

        //List<User> agentsList = new ArrayList<>(User.getActive_agents());
        List<User> agentsList = new ArrayList<>(Agents());
        for (User user : agentsList) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("AgentItem.fxml"));

            try {
                HBox hbox = fxmlLoader.load();
                AgentItemController aic = fxmlLoader.getController();
                aic.setData(user);
                aicList.add(aic);
                vboxListAgent.getChildren().add(hbox);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        createGroup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                List <String> group = new ArrayList<>();
                for (AgentItemController agc : aicList) {
                    if (agc.checkBoxGroup.isSelected()) {
                        group.add(String.valueOf(agc.pseudo.getText()));
                    }
                }

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("GroupItem.fxml"));

                try {
                    HBox hbox = fxmlLoader.load();
                    GroupItemController gic = fxmlLoader.getController();
                    gic.setData(group);
                    vboxListGroups.getChildren().add(hbox);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // think how to save group messages in the database
            }
        });
    }

    private List<User> Agents() {
        List<User> ls = new ArrayList<>();
        User user = new User();

        user.setPseudo("tata");
        user.setImgSrc("file:src/main/resources/Images/person.png");
        ls.add(user);

        user = new User();
        user.setPseudo("titi");
        user.setImgSrc("file:src/main/resources/Images/person2.png");
        ls.add(user);

        user = new User();
        user.setPseudo("toto");
        user.setImgSrc("file:src/main/resources/Images/person1.png");
        ls.add(user);

        user.setPseudo("tutu");
        user.setImgSrc("file:src/main/resources/Images/person1.png");
        ls.add(user);

        return ls;
    }
}
