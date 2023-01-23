package Controller.Database;

import java.sql.Connection;
import java.sql.DriverManager;


public class Connexion {
        public static Connection getConnection()  throws Exception{

        Connection cnx = null;

        try {
            //cnx = DriverManager.getConnection(url, username, pwd);
            Class.forName("org.sqlite.JDBC");
            cnx = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return cnx;
    }

}