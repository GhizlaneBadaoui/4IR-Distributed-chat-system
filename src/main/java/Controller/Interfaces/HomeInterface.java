package Controller.Interfaces;

import Controller.Threads.*;
import Model.User;
import com.example.chatsystem.Main;
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
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static Controller.Database.Operations.connect;
import static Controller.Database.Operations.displayMessagesWithAgent;
import static Model.User.getUser;
import static com.example.chatsystem.Main.stage;

public class HomeInterface implements Initializable {
    @FXML
    public Button reduceButton;
    @FXML
    public VBox All;
    @FXML
    private Label agentPseudo;
    @FXML
    private ImageView agentImg;
    @FXML
    private Button disconnectButton;
    @FXML
    public Button editButton;
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
    private VBox vbox_messages;
    @FXML
    private TextField myPseudo;
    @FXML
    private TableView<User> agentsTable;
    @FXML
    private TableColumn<User, ImageView> photoColumn;
    @FXML
    private TableColumn<User, String> pseudoColumn;

    ObservableList<User> agentsList = FXCollections.observableArrayList();
    public static HomeInterface currentHomeInter;
    int index = -1;
    Main objetMain = new Main();

    void disconnect (){
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

    public void restrictConversation() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                agentImg.setImage(null);
                agentPseudo.setText(null);
                vbox_messages.getChildren().removeAll(vbox_messages.getChildren());
                agentsTable.getSelectionModel().clearSelection();
                index = -1;
                All.setDisable(true);
            }
        });
    }

    public void refreshConversation(String pseudo, int id) {
        if (pseudoColumn.getCellData(agentsTable.getSelectionModel().getSelectedIndex()).equals(pseudo)) {
            vbox_messages.getChildren().removeAll(vbox_messages.getChildren());
            List<String[]> tab = displayMessagesWithAgent(id);
            if (tab.isEmpty()) {
                return;
            }
            for (String[] element : tab) {
                if (element[2].equals("R")) {
                    addLabelForIncomingMessage(element[0], element[1], vbox_messages);
                }
                if (element[2].equals("S")) {
                    addLabelForOutgoingMessage(element[0], element[1], vbox_messages);
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /* Disable conversation space */
        All.setDisable(true);

        /* Connect to the database */
        connect();

        /* Set the user actually connected int the App */
        currentHomeInter = this;

        /* Set user information */
        myPseudo.setText(ConnectivityThread.getInstance().getUser().getPseudo());

        /* Configure active agents list */
        photoColumn.setCellValueFactory(new PropertyValueFactory<>("imgSrc"));
        pseudoColumn.setCellValueFactory(new PropertyValueFactory<>("pseudo"));
        refreshTable();

        agentsTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                index = agentsTable.getSelectionModel().getSelectedIndex();
                if (index>-1) {
                    All.setDisable(false);
                    agentPseudo.setText(pseudoColumn.getCellData(index));
                    agentImg.setImage(photoColumn.getCellData(index).getImage());
                    refreshConversation(pseudoColumn.getCellData(index), getUser(pseudoColumn.getCellData(index)).getDbID());
                }
            }
        });

        agentsTable.skinProperty().addListener((obsVal, oldSkin, newSkin) -> {
            TableHeaderRow header = (TableHeaderRow) agentsTable.lookup("TableHeaderRow");
            header.reorderingProperty()
                    .addListener((obs, oldVal, newVal) -> header.setReordering(false));

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
                ReceiverThread newReceiverThread = null;
                if(SenderHandler.getInstance().isEtablished(pseudo)){
                    System.out.println("old connection");
                    try {
                        socket = ListenConnThread.getInstance().getSock(pseudo);
                        senderThread = new SenderThread(socket, pseudo, messageToSend);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    System.out.println("new connection");
                    socket = SenderHandler.getInstance().startConnection(pseudo);
                    if(socket.isConnected()){
                        try {
                            System.out.println("connected ");
                            senderThread = new SenderThread(socket, pseudo, messageToSend);
                            System.out.println("the sender has been created");
                            newReceiverThread = new ReceiverThread(socket,pseudo);
                            System.out.println("the reciever has been created");
                            ReceiverThread.receivers.add(newReceiverThread);
                            newReceiverThread.start();
                            System.out.println("the reciever has been started");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (!messageToSend.isEmpty() && socket.isConnected()) {
                    senderThread.start();
                    messageLabel.clear();
                }
            }
        });

        reduceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                restrictConversation();
            }
        });

        disconnectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                disconnect();
            }
        });

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                disconnect();
                Platform.exit();
                System.exit(0);
            }
        });

        parametersButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Your personal information");
                dialog.setHeaderText("* Your full name is : "+ConnectivityThread.getInstance().getUser().getFullName()
                        +"\n* Your pseudo is : "+ConnectivityThread.getInstance().getUser().getPseudo()
                        +"\n* Your address is : "+ConnectivityThread.getInstance().getUser().getIP().getHostAddress()
                        +"\n* Your port is : "+ConnectivityThread.getInstance().getUser().getPort());
                dialog.setGraphic(ConnectivityThread.getInstance().getUser().getImgSrc());
                dialog.setContentText("If you want to change your name, please enter the new one : ");
                ((Stage) dialog.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:src/main/resources/Images/logo.png"));
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(ConnectivityThread.getInstance().getUser()::setFullName);
            }
        });

        editButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Change your pseudonym");
                dialog.setHeaderText(null);
                dialog.setContentText("Please enter your new pseudonym :");
                ((Stage) dialog.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:src/main/resources/Images/logo.png"));

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(newPseudo -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:src/main/resources/Images/logo.png"));
                    try {
                        if (newPseudo.equals(ConnectivityThread.getInstance().getUser().getPseudo())) {
                            alert.setContentText("You did not change your pseudonym !");
                            alert.showAndWait();
                        } else if (!newPseudo.contains("@") && !newPseudo.contains(":")) {
                            if (ConnectivityThread.getInstance().getUser().modifyPseudo(newPseudo)){
                                alert.setContentText("Your pseudonym was successfully changed !\n" +
                                        "Your new pseudonym is : "+ newPseudo);
                                alert.showAndWait();
                                ConnectivityThread.getInstance().getUser().setPseudo(newPseudo);
                                myPseudo.setText(newPseudo);
                            } else {
                                alert.setContentText("Invalid pseudonym !");
                                alert.showAndWait();
                            }
                        } else {
                            alert.setContentText("Invalid pseudonym ! You can use only letters or/and numbers.");
                            alert.showAndWait();
                        }
                    } catch (IOException e) {
                       e.printStackTrace();
                    }
                });
            }
        });

        /* Search for active agent in the list */
        Search();
//        closeConnection();
    }

    public void addLabelForIncomingMessage(Object msg, String date, VBox vbox) {
        VBox primaryVbox = new VBox();
        primaryVbox.setAlignment(Pos.CENTER_LEFT);

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(5,5,5,10));

        Label dateLabel = new Label(date);
        dateLabel.setPadding(new Insets(5, 5, 5, 5));
        dateLabel.setStyle("-fx-font-size: 10px;");
        dateLabel.setStyle("-fx-text-fill: #636566;");

        Text text = new Text((String) msg);
        text.setStyle("-fx-font-size: 15px;");
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

    public void addLabelForOutgoingMessage(Object msg, String date, VBox vbox) {
        VBox primaryVbox = new VBox();
        primaryVbox.setAlignment(Pos.CENTER_RIGHT);

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPadding(new Insets(5,5,5,10));

        Label dateLabel = new Label(date);
        dateLabel.setPadding(new Insets(5, 5, 5, 5));
        dateLabel.setStyle("-fx-font-size: 10px;");
        dateLabel.setStyle("-fx-text-fill: #636566;");

        Text text = new Text((String) msg);
        text.setStyle("-fx-font-size: 15px;");
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

    public String getAgentPseudo() {
        return agentPseudo.getText();
    }
}
