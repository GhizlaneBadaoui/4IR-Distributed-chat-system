module com.example.chatsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens View to javafx.fxml;
    opens Model to javafx.fxml;
    exports Model;
    exports View;
    exports Controller.Protocoles;
    exports com.example.chatsystem;
}