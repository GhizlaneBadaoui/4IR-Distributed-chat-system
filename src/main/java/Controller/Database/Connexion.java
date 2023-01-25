package Controller.Database;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * is used to establish a connection with the database server
 **/
public class Connexion {
        public static Connection getConnection()  throws Exception{
        Connection cnx = null;
        try {
            Class.forName("org.sqlite.JDBC");
            cnx = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return cnx;
    }
}