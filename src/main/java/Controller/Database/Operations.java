package Controller.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains all the operations to access, add, modify, delete ... from the database
 **/
public class Operations{

    static Connection cnx = null;
    static Statement st;
    static ResultSet rst;

    static String tableName = "messages";
    static String tableNameSec = "pseudos";

    /* Check if the database is connected with the code */
    public static void connect() {
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
            String query = "CREATE TABLE IF NOT EXISTS pseudos" +
                    " (pseudoID INTEGER NOT NULL PRIMARY KEY," +
                    " unPseudo TEXT NOT NULL)";

            st = cnx.createStatement();
            st.executeUpdate(query);

            query = "CREATE TABLE IF NOT EXISTS messages" +
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

    /* Return incoming and outgoing messages related to an agent */
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

    /* verify if an agent (id) already exist in the database */
    public static boolean exist(int id) {
        int res = 0;
        try {
            String query = "SELECT EXISTS(SELECT 1 FROM '" + tableNameSec + "' WHERE pseudoID=" + id + ") AS result;";
            st = cnx.createStatement();
            rst = st.executeQuery(query);
            while (rst.next()) {
                res = Integer.parseInt(rst.getString("result"));
            }
            return (res == 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* Change the pseudonym of an agent to a new one */
    public static void modifyPseudo(int id, String newPseudo) {
        try {
            String query = "UPDATE '"+ tableNameSec +"' SET unPseudo = '"+ newPseudo +"' WHERE pseudoID ="+id+";";
            st = cnx.createStatement();
            st.executeUpdate(query);
            System.out.println("\n--> An element was updated to "+newPseudo+" !\n");
        } catch(Exception ex) { ex.printStackTrace();}
    }


    /* Add a message to the DB */
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

    /* Add an agent to the database */
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

    /* Delete an agent from the database */
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

    /* Close connection with the DB if it initiated */
    public static void closeConnection() {
        try {
            if (st!=null && cnx!=null) {
                st.close();
                cnx.close();
                System.out.println("\n--> DB closed !\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("\n--> DB not closed !\n");
        }
    }
}


