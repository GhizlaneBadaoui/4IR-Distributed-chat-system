package com.example.chatsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage stage;
    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        stage.setTitle("Chat System");
        stage.setScene(scene);
        stage.show();
    }

    public void changeScene(String fxml) throws IOException{
        Parent BorderPane = FXMLLoader.load(getClass().getResource(fxml));
        stage.getScene().setRoot(BorderPane);
    }

    public static void main(String[] args) {
        launch();
    }

}


