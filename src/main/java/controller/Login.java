package controller;

import application.Program;
import application.logWriter;
import helper.AppointmentQuery;
import helper.UserQuery;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controller for login page
 */

public class Login implements Initializable {

    @FXML
    private TextField userField;
    @FXML
    private PasswordField passField;
    @FXML
    private Button loginButton;
    @FXML
    private Button exitButton;
    @FXML
    private Label title;
    @FXML
    private Label enterUserText;
    @FXML
    private Label enterPassText;
    @FXML
    private Label locText;
    @FXML
    private Label loc;


    /*  Retrieve and format current date/time

        ZDT = user's current date/time/zoneID
        formatter = formatting for date/time
        dateTime = formatted UTC time
   */
    ZonedDateTime ZDT = ZonedDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss ");
    String dateTime = ZDT.format(formatter) + ZoneId.systemDefault();

    /**
     * Opens appointments screen if a correct username and password are entered, displays an alert in user's computer
     * language if information is incorrect, also displays an alert saying whether there is an appointment
     * within 15 minutes
     * @param event event
     * @throws IOException IOException
     * @throws SQLException SQLException
     */
    public void ButtonLogin(ActionEvent event) throws IOException, SQLException {

        ResourceBundle rb = ResourceBundle.getBundle("Language", Locale.getDefault());

        if (UserQuery.loginCheck(userField.getText(), passField.getText())) {
            Stage appointments = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Program.class.getResource("Appointments.fxml"));
            appointments.setScene(new Scene(fxmlLoader.load()));
            appointments.show();
            loginButton.getScene().getWindow().hide();
            logWriter.writeSuccess(dateTime);

            //show alert saying whether an appointment is happening within 15 minutes and make it the top window
            try {
                Appointment appointment = AppointmentQuery.reminderCheck();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                if (appointment.getStart() != null) {
                    alert.setContentText("Appointment " + appointment.getApptID() + " starting soon at " + appointment.getStart());
                } else {
                    alert.setContentText("No upcoming appointments");
                }
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.setAlwaysOnTop(true);
                stage.toFront();
                alert.show();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            event.consume();

        } else if (Locale.getDefault().getLanguage().equals("fr")) {
            logWriter.writeFail(dateTime);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(rb.getString("error"));
            alert.showAndWait();
        } else {
            logWriter.writeFail(dateTime);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Incorrect username or password, please try again");
            alert.showAndWait();
        }
    }

    /**
     * Method to close application if exit button is pressed
     * @param event event
     */
    public void ButtonExit(ActionEvent event) {
        Platform.exit();
        System.out.println("Application closed");
        event.consume();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Locale locale = Locale.getDefault();
        ResourceBundle rb = ResourceBundle.getBundle("Language", locale);

        //sets location label based on users location
        loc.setText(String.valueOf(ZoneId.systemDefault()));

        //set labels to French based on user's language
        if (Locale.getDefault().getLanguage().equals("fr")) {
            title.setText(rb.getString("userLogin"));
            enterUserText.setText(rb.getString("enterUserName"));
            enterPassText.setText(rb.getString("enterPassword"));
            loginButton.setText(rb.getString("login"));
            exitButton.setText(rb.getString("exit"));
            locText.setText(rb.getString("location"));
            loc.setText(String.valueOf(ZoneId.systemDefault()));
        }
    }
}