module com.example.mycharacterfrequency {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.mycharacterfrequency to javafx.fxml;
    exports com.example.mycharacterfrequency;
}