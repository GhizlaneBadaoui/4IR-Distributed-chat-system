package com.example.chatsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage stage;
    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Chat System");
        stage.getIcons().add(new Image("file:src/main/resources/Images/logo.png"));
        stage.setScene(scene);
        stage.resizableProperty().setValue(false);
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


