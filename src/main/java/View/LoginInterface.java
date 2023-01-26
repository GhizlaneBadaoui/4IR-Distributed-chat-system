package View;

import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.InetAddress;

import static com.example.chatsystem.Main.changeScene;

/**
 * interface to log in
 */
public class LoginInterface {

    @FXML
    private Button logInButton;
    @FXML
    private TextField idLabel;
    @FXML
    private TextField pseudoLabel;
    @FXML
    private Label connectionError;


    public static User user;

    /* Connection : check pseudo and connect */
    @FXML
    void LogIn (ActionEvent event){
        try {
            user = new User(InetAddress.getLocalHost(),5009);
            if (pseudoLabel.getText().isEmpty() || idLabel.getText().isEmpty()){
                connectionError.setText("Enter your pseudonym and your id, use only numbers or letters for pseudonym and only numbers for id.");
            } else {
                if (pseudoLabel.getText().contains("@") || pseudoLabel.getText().contains(":")) {
                    connectionError.setText("Attention : use only numbers or letters for the pseudonym ! ");
                } else {
                    try {
                        int id = Integer.parseInt(idLabel.getText());
                        if (user.choose_pseudo(pseudoLabel.getText(),idLabel.getText())) {
                            user.setPseudo(pseudoLabel.getText());
                            user.setDbID(id);
                            connectionError.setText("Success !");
                            changeScene("Home.fxml");
                        }
                        else {
                            connectionError.setText("This pseudo already exists, choose another one.");
                        }
                    } catch (NumberFormatException e){
                        connectionError.setText("Attention : your id must be a number !");
                    }

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
