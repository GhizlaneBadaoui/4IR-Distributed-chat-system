package com.example.chatsystem;

import Controller.Threads.*;
import Model.User;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static Controller.Database.Operations.*;

public class HomeInterface implements Initializable {
    @FXML
    private Label agentPseudo;
    @FXML
    private ImageView agentImg;
    @FXML
    private Button disconnectButton;
    @FXML
    private TextField messageLabel;
    @FXML
    private Button parametersButton;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextField searchField;
    @FXML
    private Button sendButton;
    @FXML
    private Button sendFile;
    @FXML
    private Button sendPicture;
    @FXML
    private VBox vbox_messages;
    @FXML
    private Label myPseudo;
    @FXML
    private TableView<User> agentsTable;
    @FXML
    private TableColumn<User, ImageView> photoColumn;
    @FXML
    private TableColumn<User, String> pseudoColumn;
    @FXML
    private TableColumn<User, Integer> msgColumn;

    ObservableList<User> agentsList = FXCollections.observableArrayList();
    public static HomeInterface currentHomeInter;
    Integer index;
    private SenderThread senderThread;
    Main objetMain = new Main();

    @FXML
    void disconnect (ActionEvent event){
        try {
            ConnectivityThread.setFlag(true);
            ConnectivityThread.getInstance().deconnection();
            objetMain.changeScene("Login.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshTable(){
        agentsList.clear();
        agentsList.addAll(User.getActive_agents());
        agentsTable.setItems(agentsList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connect();
        currentHomeInter = this;
        User u = ConnectivityThread.getInstance().getUser();
        myPseudo.setText("My pseudo : " + u.getPseudo());
//        ipAddress.setText(u.getIP().getHostAddress());
//        port.setText(String.valueOf(u.getPort()));

        photoColumn.setCellValueFactory(new PropertyValueFactory<>("imgSrc"));
        pseudoColumn.setCellValueFactory(new PropertyValueFactory<>("pseudo"));
        msgColumn.setCellValueFactory(new PropertyValueFactory<>("incomingMsg"));
        refreshTable();
        agentsTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                index = agentsTable.getSelectionModel().getSelectedIndex();
                if (index>-1) {
                    vbox_messages.getChildren().removeAll(vbox_messages.getChildren());
                    setConversationData();
                }
            }
        });

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
                String messageToSend = messageLabel.getText();
                Socket socket = null;
                SenderThread senderThread = null;
                if(SenderHandler.getInstance().isEtablished(pseudo)){
                    try {
                        socket = ListenConnThread.getInstance().getSock(pseudo);
                        senderThread = new SenderThread(socket, pseudo, messageToSend);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    socket = SenderHandler.getInstance().startConnection(pseudo);
                    if(socket.isConnected()){
                        try {
                            System.out.println("connected ");
                            senderThread = new SenderThread(socket, pseudo, messageToSend);
                            ReceiverThread newReceiverThread = new ReceiverThread(socket,pseudo);
                            ReceiverThread.receivers.add(newReceiverThread);
                            newReceiverThread.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (!messageToSend.isEmpty() && socket.isConnected()) {
                    //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    Date msgDate = Date.valueOf(dtf.format(LocalDateTime.now()));
                    add(messageToSend, msgDate, 'S', pseudo);
                    addLabelForOutgoingMessage(messageToSend, String.valueOf(msgDate), vbox_messages);
                    senderThread.start();
                    messageLabel.clear();
                }
            }
        });

        Search();
//        closeConnection();
    }
    public void setConversationData() {
        agentPseudo.setText(pseudoColumn.getCellData(index));
        agentImg.setImage(photoColumn.getCellData(index).getImage());

        List<String[]> tab = displayMessagesWithAgent(pseudoColumn.getCellData(index).toString());
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


    private void Search() {

        FilteredList<User> filteredData = new FilteredList<>(agentsList, b -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (user.getPseudo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<User> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(agentsTable.comparatorProperty());
        agentsTable.setItems(sortedData);
    }

    private List<User> Agents() {
        List<User> ls = new ArrayList<>();
        User user = new User();

        user.setPseudo("tata");
        user.setImgSrc(new ImageView(new Image("file:src/main/resources/Images/person.png")));
        user.setIncomingMsg(0);
        ls.add(user);

        user = new User();
        user.setPseudo("titi");
        user.setImgSrc(new ImageView(new Image("file:src/main/resources/Images/person2.png")));
        user.setIncomingMsg(0);
        ls.add(user);

        user = new User();
        user.setPseudo("toto");
        user.setImgSrc(new ImageView(new Image("file:src/main/resources/Images/person1.png")));
        user.setIncomingMsg(0);
        ls.add(user);

        user.setPseudo("tutu");
        user.setImgSrc(new ImageView(new Image("file:src/main/resources/Images/person1.png")));
        user.setIncomingMsg(0);
        ls.add(user);

        return ls;
    }

}
