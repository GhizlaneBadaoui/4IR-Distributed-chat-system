package Controller.Messages;

import Model.User;

import java.util.Date;
import java.sql.*;

public class File extends Message{
    private Blob content;

    public File(User sender, User receiver, Blob content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.date = new Date();
    }
}
