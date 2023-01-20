package Controller.Database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Operations{

    static Connection cnx = null;
    static Statement st;
    static ResultSet rst;

    static String tableName = "messages";
    static String tableNameSec = "pseudos";

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
            String query = "CREATE TABLE pseudos" +
                    " (pseudoID INTEGER NOT NULL PRIMARY KEY," +
                    " unPseudo TEXT NOT NULL," +
                    " UNIQUE(pseudoID))";

            st = cnx.createStatement();
            st.executeUpdate(query);

            query = "CREATE TABLE messages" +
                    " (messageID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    " content BLOB NOT NULL," +
                    " date TEXT NOT NULL," +
                    " operation TEXT CHECK( operation IN ('R','S') ) NOT NULL DEFAULT 'R'," +
                    " id INTEGER NOT NULL," +
                    " FOREIGN KEY(id) REFERENCES pseudos(pseudoID))";

            st = cnx.createStatement();
            st.executeUpdate(query);

            System.out.println("\n--> DB is initiated  !\n");
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /* Display all elements from a DB table */
    public static void displayAll(){

        try {
            System.out.println("\n------- Display table content --------");

            st = cnx.createStatement();
            rst = st.executeQuery("SELECT * FROM "+ tableName );

            while (rst.next()) {
                System.out.print(rst.getInt("messageID") + "\t");
                System.out.print(rst.getString("content") + "\t");
                System.out.print(rst.getString("date") + "\t");
                System.out.print(rst.getString("operation") + "\t");
                System.out.print(rst.getString("pseudo") + "\t");
                System.out.println();
            }
        } catch(Exception ex) { ex.printStackTrace();}
    }


    public static List<String[]> displayMessagesWithAgent(String agentPseudo){
        List<String[]> tab = new ArrayList<>();
        try {
            st = cnx.createStatement();
            rst = st.executeQuery("SELECT * FROM '"+ tableName +"' where id = (SELECT pseudoID FROM '"+ tableNameSec +"' WHERE unPseudo = '"+agentPseudo+"') order by messageID");
            while (rst.next()) {
                tab.add(new String[]{rst.getString("content"), rst.getString("date"), rst.getString("operation")});
            }
        } catch(Exception ex) { ex.printStackTrace();}

        return tab;
    }


    public static void modifyPseudo(int id, String newPseudo) {
        try {
            st = cnx.createStatement();
            rst = st.executeQuery("UPDATE '"+ tableNameSec +"' SET unPseudo = '"+ newPseudo +"' WHERE pseudoID ='"+id);
            System.out.println("\n--> An element was updated to "+newPseudo+" !\n");
        } catch(Exception ex) { ex.printStackTrace();}
    }


    /* Add an element in a DB table  */
    public static void 	add(String content, String date, char operation, int id){

        try {
            String query = "INSERT INTO "+ tableName +" (content, date, operation, id) "+
                    "VALUES ('"+ content +"','"+ date +"','"+ operation +",'"+ id +"');";
            st = cnx.createStatement();
            st.executeUpdate(query);
            System.out.println("\n--> An element ( msg = "+ content +") is added to the DB  !\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static int addPseudo(String pseudo, int id){
        try {
            String query = "INSERT INTO "+ tableNameSec +" (pseudoID, unPseudo) "+
                    "VALUES ('"+ id +"','"+ pseudo +"');";
            st = cnx.createStatement();
            st.executeUpdate(query);
            System.out.println("\n--> An element ( pseudo = "+ pseudo +") was added to the DB  !\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /* Delete an element in a DB table */
    public static void delete (int messageID){

        try {
            cnx = Connexion.getConnection();
            String query = "DELETE FROM "+ tableName +" WHERE (messageID = '"+ messageID +"')";
            st = cnx.createStatement();
            st.executeUpdate(query);
            System.out.println("\n--> An element is deleted in the DB !\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void closeConnection() {
        try {
            st.close();
            cnx.close();
            System.out.println("\n--> DB closed !\n");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("\n--> DB not closed !\n");
        }
    }
}


