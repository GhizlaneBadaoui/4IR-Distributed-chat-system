package Controller.ConnectDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Operations{

    static Connection cnx = null;
    static Statement st;
    static ResultSet rst;


    /* Check if the database is connected with the code */
    public static void connect() throws Exception {

        Connexion connexion = new Connexion();
        Connexion.getConnection();
        if ( connexion != null) {
            System.out.println("--> DB connected !\n");
        }
    }



    /* Display elements from a table */
    public static void display() throws Exception {

        try {

            cnx = Connexion.getConnection();
            System.out.println("------- Affichage de la table de BDD --------");

            st = cnx.createStatement();
            rst = st.executeQuery("SELECT * FROM chatsystem.User" );

            while (rst.next()) {
                System.out.print(rst.getInt("id") + "\t");
                System.out.print(rst.getString("name") + "\t");
                System.out.println();
            }
        } catch(Exception ex) { ex.printStackTrace();}
    }



    /* Ajouter un element d'une table */

    public static void 	add (String name) throws Exception {

        try {
            cnx = Connexion.getConnection();
            String query = "INSERT INTO `chatsystem`.`User` (`name`) VALUES ('"+ name +"');";
            st = cnx.createStatement();
            st.executeUpdate(query);
            System.out.println("--> Un élément est ajouté à la BDD !\n");
            cnx.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


    /* Supprimer un element d'une table */

    public static void delete (int id) throws Exception {

        try {
            cnx = Connexion.getConnection();
            String query = "DELETE FROM `studentdb`.`student` WHERE (`StudentID` = '"+id+"')";
            st = cnx.createStatement();
            st.executeUpdate(query);
            System.out.println("--> Un élément est supprimé de la BDD !\n");
            cnx.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

}


