module com.example.chatsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.chatsystem to javafx.fxml;
    exports com.example.chatsystem;
}