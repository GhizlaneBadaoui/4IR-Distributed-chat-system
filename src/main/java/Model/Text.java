package Model;

import java.util.Date;

public class Text extends Message{
    private String content;

    public Text(String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.date = new Date();
    }
}
