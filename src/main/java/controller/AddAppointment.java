package controller;

import DAO.JDBC;
import application.Program;
import helper.AppointmentQuery;
import helper.UserQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Class to add appointment to database
 */
public class AddAppointment implements Initializable {

    @FXML
    private TableView<model.Appointment> appointmentTable;
    @FXML
    private TableColumn<Object, Object> appointmentID;
    @FXML
    private TableColumn<Object, Object> title;
    @FXML
    private TableColumn<Object, Object> description;
    @FXML
    private TableColumn<Object, Object> loc;
    @FXML
    private TableColumn<Object, Object> contact;
    @FXML
    private TableColumn<Object, Object> type;
    @FXML
    private TableColumn<Object, Object> start;
    @FXML
    private TableColumn<Object, Object> end;
    @FXML
    private TableColumn<Object, Object> customerID;
    @FXML
    private TableColumn<Object, Object> userID;
    @FXML
    private TextField addAppID;
    @FXML
    private TextField addTitle;
    @FXML
    private TextField addDesc;
    @FXML
    private TextField addLocation;
    @FXML
    private TextField addType;
    @FXML
    private TextField addStart;
    @FXML
    private TextField addEnd;
    @FXML
    private TextField addCustID;
    @FXML
    private TextField addUserID;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private RadioButton weeklyRadio;
    @FXML
    private RadioButton monthlyRadio;
    @FXML
    private ComboBox<String> addContact;

    /*  Retrieve and format current date/time

        ZDT = user's current date/time/zoneID
        utcZDT = current UTC date/time
        formatter = formatting for date/time
        dateTime = formatted UTC time
     */
    ZonedDateTime ZDT = ZonedDateTime.now();
    ZonedDateTime utcZDT = ZonedDateTime.ofInstant(ZDT.toInstant(), ZoneId.of("UTC"));
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String dateTime = utcZDT.format(formatter);

    /**
     * Method to add new appointment
     * @param event event
     * @throws SQLException SQLException
     * @throws IOException IOException
     */
    public void ButtonSave(ActionEvent event) throws SQLException, IOException {

        int apptId = 0;
        String title = addTitle.getText();
        String desc = addDesc.getText();
        String location = addLocation.getText();
        String type = addType.getText();
        String start = addStart.getText();
        String end = addEnd.getText();
        String createDate = dateTime;
        String createdBy = UserQuery.currentUser.getUserName();
        String lastUpdate = dateTime;
        String lastUpdatedBy = UserQuery.currentUser.getUserName();
        int custID = Integer.parseInt(addCustID.getText());
        int userID = Integer.parseInt(addUserID.getText());
        int contactID = 0;

        //math to convert selected contact into usable contact ID
        String contact = addContact.getSelectionModel().getSelectedItem();
        String sql = "SELECT Contact_ID FROM contacts WHERE Contact_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, contact);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            contactID = rs.getInt("Contact_ID");
        }

        //check for overlaps
        if(AppointmentQuery.overlapCheck(start, end, apptId)){
            //check to see if appointment is within business hours
            if (AppointmentQuery.hoursCheck(start, end)) {
                //actual insert statement
                int rowsAffected = AppointmentQuery.insert(apptId, title, desc, location, type, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy, custID, userID, contactID);

                //alerts
                Alert alert;
                if (rowsAffected > 0) {
                    appointmentTable.setItems(AppointmentQuery.getAllAppointments(10000));
                    weeklyRadio.setSelected(false);
                    monthlyRadio.setSelected(false);
                    alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Appointment successfully added!");
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Insert failed");
                }
                alert.showAndWait();

                //return to home screen
                Stage appointmentStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(Program.class.getResource("Appointments.fxml"));
                appointmentStage.setScene(new Scene(fxmlLoader.load()));
                appointmentStage.show();
                cancelButton.getScene().getWindow().hide();
                event.consume();
            } else {
                Alert alert;
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("The appointment you are trying to schedule is outside of business hours");
                alert.show();
            }
        }else{
            Alert alert;
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("The appointment you are trying to schedule overlaps with another appointment");
            alert.show();
        }


    }

    /**
     * Method to switch to weekly mode via radio button
     * @param event clicking In-House radio button
     * @throws SQLException SQLException
     */
    public void radioSwapWeekly(ActionEvent event) throws SQLException {
        if (weeklyRadio.isSelected()) {
            weeklyRadio.requestFocus();
            monthlyRadio.setSelected(false);
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
            appointmentTable.setItems(AppointmentQuery.getAllAppointments(30));
        }
        event.consume();
    }

    /**
     * Method to return to Appointments screen when Cancel button is pressed
     * @param event event
     * @throws IOException IOException
     */
    public void ButtonCancel(ActionEvent event) throws IOException {
        Stage appointmentStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Program.class.getResource("Appointments.fxml"));
        appointmentStage.setScene(new Scene(fxmlLoader.load()));
        appointmentStage.show();
        cancelButton.getScene().getWindow().hide();
        event.consume();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //populate contacts dropdown
        try {
            String sql = "SELECT * FROM client_schedule.contacts";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ObservableList<String> contacts = FXCollections.observableArrayList();
            while (rs.next()) {
                contacts.add(rs.getString("Contact_Name"));
            }
            addContact.setItems(contacts);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //populate table
        try {
            appointmentID.setCellValueFactory(new PropertyValueFactory<>("apptID"));
            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            loc.setCellValueFactory(new PropertyValueFactory<>("location"));
            contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
            type.setCellValueFactory(new PropertyValueFactory<>("type"));
            start.setCellValueFactory(new PropertyValueFactory<>("start"));
            end.setCellValueFactory(new PropertyValueFactory<>("end"));
            customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            userID.setCellValueFactory(new PropertyValueFactory<>("userID"));
            appointmentTable.setItems(AppointmentQuery.getAllAppointments(10000));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //disable ID field
        addAppID.setText("Auto-generated");
        addAppID.setDisable(true);
    }
}