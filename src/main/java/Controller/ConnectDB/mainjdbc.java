package Controller.ConnectDB;

import static Controller.ConnectDB.Operations.*;

public class mainjdbc {
    public static void main(String[] args) {
        try {
            connect();

            /*-------------------- operations -------------------*/
            initiate();
            //display("id", "name", "User");
            //add("User", "name", "toto");
            //delete("User", "id", 2);
            //display("id", "name", "User");
            //initiate();
            /*----------------------------------------------------*/

            closeConnection();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}