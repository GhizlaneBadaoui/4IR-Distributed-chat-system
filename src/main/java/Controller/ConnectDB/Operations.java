package Controller.ConnectDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Operations{

    static Connection conx = null;
    static Statement st;
    static ResultSet rst;


    /* Tester la connection de la base de donnees */

    public static void connect() throws Exception {

        connexion conx = new connexion();
        connexion.getConnection();
        if ( conx != null) {
            System.out.println("--> La BDD s'est connectée !\n");
        }
    }



    /* Afficher tous les elements d'une table de la base de donnees */

    public static void display() throws Exception {

        try {

            conx = connexion.getConnection();
            System.out.println("------- Affichage de la table de BDD --------");

            st = conx.createStatement();
            rst = st.executeQuery("SELECT * FROM studentdb.student" );

            while (rst.next()) {
                System.out.print(rst.getInt("studentID") + "\t");
                System.out.print(rst.getString("studentName") + "\t");
                System.out.println();
            }
        } catch(Exception ex) { ex.printStackTrace();}
    }



    /* Ajouter un �l�ment d'une table */

    public static void 	add (String name) throws Exception {

        try {
            conx = connexion.getConnection();
            String query = "INSERT INTO `studentdb`.`student` (`StudentName`) VALUES ('"+ name +"');";
            st = conx.createStatement();
            st.executeUpdate(query);
            System.out.println("--> Un élément est ajouté à la BDD !\n");
            conx.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


    /* Supprimer un �l�ment d'une table */

    public static void delete (int id) throws Exception {

        try {
            conx = connexion.getConnection();
            String query = "DELETE FROM `studentdb`.`student` WHERE (`StudentID` = '"+id+"')";
            st = conx.createStatement();
            st.executeUpdate(query);
            System.out.println("--> Un élément est supprimé de la BDD !\n");
            conx.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

}
