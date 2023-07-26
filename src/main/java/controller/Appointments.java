package controller;

import application.Program;
import helper.AppointmentQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Appointments page controller
 */
public class Appointments implements Initializable {

    @FXML
    private TableView<model.Appointment> appointmentTable;
    @FXML
    private TableColumn<Object, Object> appointmentID;
    @FXML
    private TableColumn<Object, Object> title;
    @FXML
    private TableColumn<Object, Object> description;
    @FXML
    private TableColumn<Object, Object> location;
    @FXML
    private TableColumn<Object, Object> contact;
    @FXML
    private TableColumn<Object, Object> type;
    @FXML
    private TableColumn<Object, Object> start;
    @FXML
    private TableColumn<Object, Object> end;
    @FXML
    private TableColumn<Object, Object> appCustomerID;
    @FXML
    private TableColumn<Object, Object> userID;
    @FXML
    private Button logOutButton;
    @FXML
    private Button addCustomer;
    @FXML
    private Button editCustomer;
    @FXML
    private Button addAppointment;
    @FXML
    private Button editAppointment;
    @FXML
    private Button viewReports;
    @FXML
    private RadioButton weeklyRadio;
    @FXML
    private RadioButton monthlyRadio;
    @FXML
    private RadioButton allRadio;

    /**
     * Method to switch to weekly mode via radio button
     * @param event clicking In-House radio button
     * @throws SQLException SQLException
     */
    public void radioSwapWeekly(ActionEvent event) throws SQLException {
        if (weeklyRadio.isSelected()) {
            weeklyRadio.requestFocus();
            monthlyRadio.setSelected(false);
            allRadio.setSelected(false);
            appointmentTable.setItems(AppointmentQuery.getAllAppointments(7));
        }
        event.consume();
    }

    /**
     * Method to switch to monthly mode via radio button
     * @param event clicking outsourced radio button
     * @throws SQLException SQLException
     */
    public void radioSwapMonthly(ActionEvent event) throws SQLException {
        if (monthlyRadio.isSelected()) {
            monthlyRadio.requestFocus();
            weeklyRadio.setSelected(false);
            allRadio.setSelected(false);
            appointmentTable.setItems(AppointmentQuery.getAllAppointments(30));
        }
        event.consume();
    }

    public void radioSwapAll(ActionEvent event) throws SQLException {
        if (allRadio.isSelected()) {
            allRadio.requestFocus();
            weeklyRadio.setSelected(false);
            monthlyRadio.setSelected(false);
            appointmentTable.setItems(AppointmentQuery.getAllAppointments(10000));
        }
        event.consume();
    }

    /**
     * Method to open Add Customer screen when button is clicked
     * @param event event
     * @throws IOException IOException
     */
    public void openAddCustomer(ActionEvent event) throws IOException {
        Stage addCustomerStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Program.class.getResource("AddCustomer.fxml"));
        addCustomerStage.setScene(new Scene(fxmlLoader.load()));
        addCustomerStage.show();
        addCustomer.getScene().getWindow().hide();
        event.consume();
    }

    /**
     * Method to open Edit Customer screen when button is clicked
     * @param event event
     * @throws IOException IOException
     */
    public void openEditCustomer(ActionEvent event) throws IOException {
        Stage editCustomerStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Program.class.getResource("EditCustomer.fxml"));
        editCustomerStage.setScene(new Scene(fxmlLoader.load()));
        editCustomerStage.show();
        editCustomer.getScene().getWindow().hide();
        event.consume();
    }

    /**
     * Method to open Add Appointment screen when button is clicked
     * @param event event
     * @throws IOException IOException
     */
    public void openAddAppointment(ActionEvent event) throws IOException {
        Stage addAppointmentStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Program.class.getResource("AddAppointment.fxml"));
        addAppointmentStage.setScene(new Scene(fxmlLoader.load()));
        addAppointmentStage.show();
        addAppointment.getScene().getWindow().hide();
        event.consume();
    }

    /**
     * Method to open Edit Appointment screen when button is clicked
     * @param event event
     * @throws IOException IOException
     */
    public void openEditAppointment(ActionEvent event) throws IOException {
        Stage editAppointmentStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Program.class.getResource("EditAppointment.fxml"));
        editAppointmentStage.setScene(new Scene(fxmlLoader.load()));
        editAppointmentStage.show();
        editAppointment.getScene().getWindow().hide();
        event.consume();
    }

    /**
     * Method to open Reports screen when button is clicked
     * @param event event
     * @throws IOException IOException
     */
    public void openReports(ActionEvent event) throws IOException {
        Stage reportsStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Program.class.getResource("Reports.fxml"));
        reportsStage.setScene(new Scene(fxmlLoader.load()));
        reportsStage.show();
        viewReports.getScene().getWindow().hide();
        event.consume();
    }

    /**
     * Returns to login screen when pressing Log Out button
     * @param event event
     * @throws IOException IOException
     */
    public void ButtonLogOut(ActionEvent event) throws IOException {
        Stage loginStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Program.class.getResource("Login.fxml"));
        loginStage.setScene(new Scene(fxmlLoader.load()));
        loginStage.show();
        logOutButton.getScene().getWindow().hide();
        event.consume();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //populate appointments table
        try {
            appointmentID.setCellValueFactory(new PropertyValueFactory<>("apptID"));
            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            location.setCellValueFactory(new PropertyValueFactory<>("location"));
            contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
            type.setCellValueFactory(new PropertyValueFactory<>("type"));
            start.setCellValueFactory(new PropertyValueFactory<>("start"));
            end.setCellValueFactory(new PropertyValueFactory<>("end"));
            appCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            userID.setCellValueFactory(new PropertyValueFactory<>("userID"));
            appointmentTable.setItems(AppointmentQuery.getAllAppointments(10000));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
