package Controller.ConnectDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Operations{

    static Connection cnx = null;
    static Statement st;
    static ResultSet rst;

    /* Check if the database is connected with the code */
    public static void connect() {
        Connexion connexion = new Connexion();
        try {
            cnx = Connexion.getConnection();
            System.out.println("\n--> DB connected !\n");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("\n--> DB not connected !\n");
        }
    }

    /* Initiate the database */
    public static void initiate() {
        try {
            String query = "CREATE TABLE Message" +
                    " (massageID INT NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                    " content BLOB NOT NULL," +
                    " date DATE NOT NULL," +
                    " operation ENUM('R', 'S')," +
                    " pseudo VARCHAR(100) NOT NULL)";

            st = cnx.createStatement();
            st.executeUpdate(query);

            System.out.println("\n--> DB is initiated  !\n");
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /* Display elements from a DB table */
    public static void display(String id, String name, String tableName){

        try {
            System.out.println("\n------- Display table content --------");

            st = cnx.createStatement();
            rst = st.executeQuery("SELECT * FROM "+ tableName );

            while (rst.next()) {
                System.out.print(rst.getInt(id) + "\t");
                System.out.print(rst.getString(name) + "\t");
                System.out.println();
            }
        } catch(Exception ex) { ex.printStackTrace();}
    }



    /* Add an element in a DB table  */
    public static void 	add(String tableName, String column, String value){

        try {
            String query = "INSERT INTO `chatsystem`."+ tableName +" ("+ column + ") VALUES ('"+ value +"');";
            st = cnx.createStatement();
            st.executeUpdate(query);
            System.out.println("\n--> An element is added to the DB  !\n");
        } catch (Exception ex) {
            ex.printStackTrace();;
        }
    }


    /* Delete an element in a DB table */
    public static void delete (String tableName, String column, int value){

        try {
            cnx = Connexion.getConnection();
            String query = "DELETE FROM `chatsystem`."+ tableName +" WHERE ("+ column +" = '"+ value +"')";
            st = cnx.createStatement();
            st.executeUpdate(query);
            System.out.println("\n--> An element is deleted in the DB !\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void closeConnection() {
        try {
            cnx.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}


