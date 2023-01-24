module com.example.chatsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens Model to javafx.fxml;
    opens View to javafx.fxml;
    exports Model;
    exports com.example.chatsystem;
    exports View;
}