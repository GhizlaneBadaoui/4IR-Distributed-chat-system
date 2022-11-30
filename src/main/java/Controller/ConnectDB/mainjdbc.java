package Controller.ConnectDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static Controller.ConnectDB.Operations.*;

public class mainjdbc {
    public static void main(String[] args) {
        try {
            connect();
            add("Walid");
            display();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}