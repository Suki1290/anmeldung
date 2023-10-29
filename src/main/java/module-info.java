module com.example.anmeldung {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.anmeldung to javafx.fxml;
    exports com.example.anmeldung;
}