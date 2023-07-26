package controller;

import DAO.JDBC;
import application.Program;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controller for reports page
 */
public class Reports implements Initializable {

    private final ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();
    @FXML
    private TableView<model.Appointment> totalTable;
    @FXML
    private TableView<model.Appointment> scheduleTable;
    @FXML
    private TableView<model.Customer> demoTable;
    @FXML
    private TableColumn<Object, Object> totalTypeCol;
    @FXML
    private TableColumn<Object, Object> janCol;
    @FXML
    private TableColumn<Object, Object> febCol;
    @FXML
    private TableColumn<Object, Object> marCol;
    @FXML
    private TableColumn<Object, Object> aprCol;
    @FXML
    private TableColumn<Object, Object> mayCol;
    @FXML
    private TableColumn<Object, Object> junCol;
    @FXML
    private TableColumn<Object, Object> julCol;
    @FXML
    private TableColumn<Object, Object> augCol;
    @FXML
    private TableColumn<Object, Object> sepCol;
    @FXML
    private TableColumn<Object, Object> octCol;
    @FXML
    private TableColumn<Object, Object> novCol;
    @FXML
    private TableColumn<Object, Object> decCol;
    @FXML
    private TableColumn<Object, Object> idCol;
    @FXML
    private TableColumn<Object, Object> titleCol;
    @FXML
    private TableColumn<Object, Object> scheduleTypeCol;
    @FXML
    private TableColumn<Object, Object> descCol;
    @FXML
    private TableColumn<Object, Object> startCol;
    @FXML
    private TableColumn<Object, Object> endCol;
    @FXML
    private TableColumn<Object, Object> custIDCol;
    @FXML
    private TableColumn<Object, Object> divisionCol;
    @FXML
    private TableColumn<Object, Object> numberCol;
    @FXML
    private Button exitButton;
    @FXML
    private ComboBox<String> contactCombo;

    /**
     * Method to return to Appointments screen when Exit button is clicked
     * ----------------------------------------------------------------------------------------------------------------
     * This has been made into a lambda expression because it simplifies the code by removing the need for an OnAction
     * method to be set in SceneBuilder/FXML
     */
    public EventHandler<ActionEvent> ButtonExit = (event) -> {
        try {
            Stage appointmentStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Program.class.getResource("Appointments.fxml"));
            appointmentStage.setScene(new Scene(fxmlLoader.load()));
            appointmentStage.show();
            exitButton.getScene().getWindow().hide();
            event.consume();
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    /**
     * Handles contact ComboBox actions
     * @param event event
     */
    public void contactCombo(ActionEvent event) {

        scheduleTable.getItems().clear();

        //populate contact schedules table
        try {
            String contact = contactCombo.getSelectionModel().getSelectedItem();
            String sql = "SELECT * FROM appointments INNER JOIN contacts " +
                         "WHERE appointments.Contact_ID = contacts.Contact_ID " +
                         "AND contacts.Contact_Name = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, contact);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setApptID(rs.getInt("Appointment_ID"));
                appointment.setTitle(rs.getString("Title"));
                appointment.setType(rs.getString("Type"));
                appointment.setDescription(rs.getString("Description"));
                appointment.setStart(rs.getString("Start"));
                appointment.setEnd(rs.getString("End"));
                appointment.setCustomerID(rs.getInt("Customer_ID"));
                appointments.add(appointment);
            }
            idCol.setCellValueFactory(new PropertyValueFactory<>("apptID"));
            titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            scheduleTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
            endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
            custIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            scheduleTable.setItems(appointments);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        event.consume();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //set On Action method from ButtonExit lambda expression
        exitButton.setOnAction(ButtonExit);

        //populate total appointments table
        try {
            String typeSQL = "SELECT Type, " +
                    "SUM(CASE WHEN MONTH(Start) = 1 THEN 1 ELSE 0 END) AS January, " +
                    "SUM(CASE WHEN MONTH(Start) = 2 THEN 1 ELSE 0 END) AS February, " +
                    "SUM(CASE WHEN MONTH(Start) = 3 THEN 1 ELSE 0 END) AS March, " +
                    "SUM(CASE WHEN MONTH(Start) = 4 THEN 1 ELSE 0 END) AS April, " +
                    "SUM(CASE WHEN MONTH(Start) = 5 THEN 1 ELSE 0 END) AS May, " +
                    "SUM(CASE WHEN MONTH(Start) = 6 THEN 1 ELSE 0 END) AS June, " +
                    "SUM(CASE WHEN MONTH(Start) = 7 THEN 1 ELSE 0 END) AS July, " +
                    "SUM(CASE WHEN MONTH(Start) = 8 THEN 1 ELSE 0 END) AS August, " +
                    "SUM(CASE WHEN MONTH(Start) = 9 THEN 1 ELSE 0 END) AS September, " +
                    "SUM(CASE WHEN MONTH(Start) = 10 THEN 1 ELSE 0 END) AS October, " +
                    "SUM(CASE WHEN MONTH(Start) = 11 THEN 1 ELSE 0 END) AS November, " +
                    "SUM(CASE WHEN MONTH(Start) = 12 THEN 1 ELSE 0 END) AS December " +
                    "FROM appointments " +
                    "GROUP BY Type ";
            PreparedStatement typePS = JDBC.connection.prepareStatement(typeSQL);
            ResultSet typeRS = typePS.executeQuery();
            while (typeRS.next()) {
                String type = typeRS.getString("Type");
                int january = typeRS.getInt("January");
                int february = typeRS.getInt("February");
                int march = typeRS.getInt("March");
                int april = typeRS.getInt("April");
                int may = typeRS.getInt("May");
                int june = typeRS.getInt("June");
                int july = typeRS.getInt("July");
                int august = typeRS.getInt("August");
                int september = typeRS.getInt("September");
                int october = typeRS.getInt("October");
                int november = typeRS.getInt("November");
                int december = typeRS.getInt("December");

                Appointment appointment = new Appointment(type, january, february, march, april, may, june, july, august, september, october, november, december);
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        totalTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        janCol.setCellValueFactory(new PropertyValueFactory<>("january"));
        febCol.setCellValueFactory(new PropertyValueFactory<>("february"));
        marCol.setCellValueFactory(new PropertyValueFactory<>("march"));
        aprCol.setCellValueFactory(new PropertyValueFactory<>("april"));
        mayCol.setCellValueFactory(new PropertyValueFactory<>("may"));
        junCol.setCellValueFactory(new PropertyValueFactory<>("june"));
        julCol.setCellValueFactory(new PropertyValueFactory<>("july"));
        augCol.setCellValueFactory(new PropertyValueFactory<>("august"));
        sepCol.setCellValueFactory(new PropertyValueFactory<>("september"));
        octCol.setCellValueFactory(new PropertyValueFactory<>("october"));
        novCol.setCellValueFactory(new PropertyValueFactory<>("november"));
        decCol.setCellValueFactory(new PropertyValueFactory<>("december"));
        totalTable.setItems(appointments);

        //populate contacts dropdown
        try {
            String sql = "SELECT * FROM client_schedule.contacts";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ObservableList<String> contacts = FXCollections.observableArrayList();
            while (rs.next()) {
                contacts.add(rs.getString("Contact_Name"));
            }
            contactCombo.setItems(contacts);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //populate demographics table
        try {
            String demoSQL = "SELECT first_level_divisions.Division, COUNT(customers.Customer_ID) AS Number " +
                    "FROM first_level_divisions " +
                    "LEFT JOIN customers ON first_level_divisions.Division_ID = customers.Division_ID " +
                    "GROUP BY first_level_divisions.Division";
            PreparedStatement demoPS = JDBC.connection.prepareStatement(demoSQL);
            ResultSet demoRS = demoPS.executeQuery();
            while (demoRS.next()) {
                String division = demoRS.getString("Division");
                int number = demoRS.getInt("Number");
                Customer customer = new Customer(division, number);
                customers.add(customer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        divisionCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        numberCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        demoTable.setItems(customers);
    }
}
