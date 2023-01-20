package Controller.View;

import Model.User;
import com.example.chatsystem.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.InetAddress;

public class LoginInterface {

    public Button logInButton;
    @FXML
    private TextField pseudoLabel;
    @FXML
    private Label connectionError;


    public static User user;

    Main objetMain = new Main();


    /* Connection : check pseudo and connect */
    @FXML
    void LogIn (ActionEvent event){
        try {
            user = new User(InetAddress.getLocalHost(),5009);
            if (pseudoLabel.getText().isEmpty()){
                connectionError.setText("Enter your pseudonym, use only numbers or letters.");
            } else {
                if (pseudoLabel.getText().contains("@") || pseudoLabel.getText().contains(":")) {
                    connectionError.setText("Attention : use only numbers or letters ! ");
                } else {
                    if (user.choose_pseudo(pseudoLabel.getText())) {
                        user.setPseudo(pseudoLabel.getText());
                        connectionError.setText("Success !");
                        objetMain.changeScene("Home.fxml");
                    } else {
                        connectionError.setText("This pseudo already exists, choose another one.");
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
