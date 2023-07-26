module com.example.c195project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.base;

    exports DAO;
    exports model;
    exports application;
    exports controller;

    opens DAO to javafx.fxml;
    opens model to javafx.fxml;
    opens application to javafx.fxml;
    opens controller to javafx.fxml;
}