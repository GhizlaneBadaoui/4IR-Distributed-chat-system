package Controller.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                    " unPseudo TEXT NOT NULL)";

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


    public static List<String[]> displayMessagesWithAgent(int agentID){
        List<String[]> tab = new ArrayList<>();
        try {
            st = cnx.createStatement();
            rst = st.executeQuery("SELECT * FROM '"+ tableName +"' where id = "+ agentID +" order by messageID;");
            while (rst.next()) {
                tab.add(new String[]{rst.getString("content"), rst.getString("date"), rst.getString("operation")});
            }
        } catch(Exception ex) { ex.printStackTrace();}

        return tab;
    }

    public static boolean exist(int id) {
        try {
            String query = "SELECT EXISTS(SELECT 1 FROM '" + tableNameSec + "' WHERE pseudoID=" + id + ");";
            st = cnx.createStatement();
            rst = st.executeQuery(query);
            return Integer.parseInt(rst.toString()) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void modifyPseudo(int id, String newPseudo) {
        try {
            String query = "UPDATE '"+ tableNameSec +"' SET unPseudo = '"+ newPseudo +"' WHERE pseudoID ="+id+";";
            st = cnx.createStatement();
            rst = st.executeQuery(query);
            System.out.println("\n--> An element was updated to "+newPseudo+" !\n");
        } catch(Exception ex) { ex.printStackTrace();}
    }


    /* Add an element in a DB table  */
    public static void 	add(String content, String date, char operation, int id){

        try {
            String query = "INSERT INTO "+ tableName +" (content, date, operation, id) "+
                    "VALUES ('"+ content +"','"+ date +"','"+ operation +"',"+ id +");";
            st = cnx.createStatement();
            st.executeUpdate(query);
            System.out.println("\n--> An element ( msg = "+ content +") is added to the DB  !\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void addPseudo(String pseudo, int id){
        try {
            String query = "INSERT INTO "+ tableNameSec +" (pseudoID, unPseudo) "+
                    "VALUES ("+ id +",'"+ pseudo +"');";
            st = cnx.createStatement();
            st.executeUpdate(query);
            System.out.println("\n--> An element ( pseudo = "+ pseudo +") was added to the DB  !\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /* Delete an element in a DB table */
    public static void deleteAgent (int id){
        try {
            cnx = Connexion.getConnection();
            String query = "DELETE FROM "+ tableNameSec +" WHERE pseudoID = "+ id +";";
            st = cnx.createStatement();
            st.executeUpdate(query);
            System.out.println("\n--> An element (id = "+ id +") was deleted in the DB !\n");
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


