module com.example.chatsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens Controller.Interfaces to javafx.fxml;
    opens Model to javafx.fxml;
    exports Model;
    exports Controller.Interfaces;
    exports com.example.chatsystem;
}