package Controller.Messages;

import Model.User;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Text extends Message{
    private String content;

    public Text(User sender, User receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.date = new Date();
    }
}
