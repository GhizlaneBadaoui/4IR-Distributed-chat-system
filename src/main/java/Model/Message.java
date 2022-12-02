package Model;

import Model.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Message {
    public User sender;
    public User receiver;
    public Date date;
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
}
