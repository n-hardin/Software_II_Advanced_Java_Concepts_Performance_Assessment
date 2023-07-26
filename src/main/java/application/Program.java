package application;

import DAO.JDBC;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Runs application
 */
public class Program extends javafx.application.Application {

    /**
     * Main method
     * @param args args
     */
    public static void main(String[] args) {
        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
    }

    /**
     * Opens JavaFX Login page
     * @param startStage JavaFX Stage
     * @throws Exception Exception
     */
    @Override
    public void start(Stage startStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Program.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        startStage.setScene(scene);
        startStage.show();
    }
}