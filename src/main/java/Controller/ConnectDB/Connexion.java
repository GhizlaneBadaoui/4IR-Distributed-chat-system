package Controller.ConnectDB;

import java.sql.Connection;
import java.sql.DriverManager;


public class Connexion {

    private static String url = "jdbc:mysql://localhost:3306/chatsystem";
    private static String username = "root";
    private static String pwd = "";


    public static Connection getConnection()  throws Exception{

        Connection cnx = null;

        try {
            cnx = DriverManager.getConnection(url, username, pwd);

        } catch (Exception e) {
            System.out.println(e.toString());}

        return cnx;
    }

}