package Controller.Messages;

import Model.User;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Texte extends Message{
    private String content;

    public Texte(User sender, User receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.date = new Date();
    }
}
