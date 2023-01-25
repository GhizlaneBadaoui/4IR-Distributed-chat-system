package com.example.chatsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

import static Controller.Database.Operations.connect;
import static Controller.Database.Operations.initiate;

public class Main extends Application {

    public static Stage stage;
    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        stage.setTitle("Chat System");
        stage.getIcons().add(new Image("file:src/main/resources/Images/logo.png"));
        stage.setScene(scene);
        stage.resizableProperty().setValue(false);
        stage.show();
    }

    /* called to change the scene */
    public static void changeScene(String fxml) throws IOException{
        Parent BorderPane = FXMLLoader.load(Main.class.getResource(fxml));
        stage.getScene().setRoot(BorderPane);
    }

    public static void main(String[] args) {
        connect();
        initiate();
        launch();
    }

}


