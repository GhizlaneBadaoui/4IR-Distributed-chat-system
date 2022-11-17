package Controller.Messages;

import Model.User;

import java.util.Date;

public class File extends Message{
    private String content;

    public File(User sender, User receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.date = new Date();
    }
}
