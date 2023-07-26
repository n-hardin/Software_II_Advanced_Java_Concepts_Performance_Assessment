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
import model.Appointment;
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
 * Controller for edit appointments page
 */
public class EditAppointment implements Initializable {

    @FXML
    private TableView<Appointment> appointmentTable;
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
    private TextField editAppID;
    @FXML
    private TextField editTitle;
    @FXML
    private TextField editDesc;
    @FXML
    private TextField editLocation;
    @FXML
    private TextField editType;
    @FXML
    private TextField editStart;
    @FXML
    private TextField editEnd;
    @FXML
    private TextField editCustID;
    @FXML
    private TextField editUserID;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    public Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private RadioButton weeklyRadio;
    @FXML
    private RadioButton monthlyRadio;
    @FXML
    private ComboBox<String> editContact;


    /* Retrieve and format current date/time

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
     * Method to populate TextFields for the appointment to be edited
     * @param event event
     */
    public void ButtonEdit(ActionEvent event) {

        //set TextFields
        Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
        editAppID.setText(String.valueOf(appointment.getApptID()));
        editTitle.setText(appointment.getTitle());
        editDesc.setText(appointment.getDescription());
        editLocation.setText(appointment.getLocation());
        editContact.setValue(appointment.getContact());
        editType.setText(appointment.getType());
        editStart.setText(appointment.getStart());
        editEnd.setText(appointment.getEnd());
        editCustID.setText(String.valueOf(appointment.getCustomerID()));
        editUserID.setText(String.valueOf(appointment.getUserID()));

        event.consume();
    }

    /**
     * Method to delete an appointment when a button is pressed - opens an alert box to confirm before deletion
     * ----------------------------------------------------------------------------------------------------------------
     * This confirmation alert when deleting an appointment has been made into a lambda to make the code
     * (specifically the if statement) more clear and concise
     * @param event event
     * @throws SQLException SQLException
     */
    public void ButtonDelete(ActionEvent event) throws SQLException {

        if (appointmentTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No appointment selected, please select an appointment to delete");
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to delete this appointment?");
            alert.showAndWait().ifPresent((response) -> {
                if (response == ButtonType.OK) {
                    try{
                        Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
                        int ID = appointment.getApptID();
                        AppointmentQuery.delete(ID);
                        appointmentTable.setItems(AppointmentQuery.getAllAppointments(10000));
                        weeklyRadio.setSelected(false);
                        monthlyRadio.setSelected(false);
                        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                        confirm.setContentText(appointment.getType() + " appointment with ID: " + appointment.getApptID() + " has been successfully canceled");
                        confirm.show();
                        event.consume();
                    }catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    /**
     * Method to update appointment when save button is pressed
     * @param event event
     * @throws SQLException SQLException
     * @throws IOException IOException
     */
    public void ButtonSave(ActionEvent event) throws SQLException, IOException {

        //error alert if appointment to edit has not been selected
        if (editAppID.getText().equals("")) {
            Alert noSelection;
            noSelection = new Alert((Alert.AlertType.ERROR));
            noSelection.setContentText("Please select an appointment to edit");
            noSelection.show();
        } else {
            Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
            //set variables
            int appID = Integer.parseInt(editAppID.getText());
            String title = editTitle.getText();
            String description = editDesc.getText();
            String location = editLocation.getText();
            String type = editType.getText();
            String start = editStart.getText();
            String end = editEnd.getText();
            String createDate = appointment.getCreateDate();
            String createdBy = appointment.getCreatedBy();
            String lastUpdate = dateTime;
            String lastUpdatedBy = UserQuery.currentUser.getUserName();
            int customerID = Integer.parseInt(editCustID.getText());
            int userID = Integer.parseInt(editUserID.getText());
            int contactID = 0;

            //math to convert selected contact into usable contact ID
            String contact = editContact.getSelectionModel().getSelectedItem();
            String sql = "SELECT Contact_ID FROM contacts WHERE Contact_Name = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, contact);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                contactID = rs.getInt("Contact_ID");
            }

            //math to retrieve who created the customer
            String createdSQL = "SELECT Created_By FROM customers WHERE Customer_ID = ?";
            PreparedStatement createdPS = JDBC.connection.prepareStatement(createdSQL);
            createdPS.setString(1, String.valueOf(customerID));
            ResultSet createdRS = createdPS.executeQuery();
            while (createdRS.next()) {
                createdBy = createdRS.getString("Created_By");
            }

            if(AppointmentQuery.overlapCheck(start, end, appID)){
                if(AppointmentQuery.hoursCheck(start, end)){
                    //actual update statement
                    int rowsAffected = AppointmentQuery.update(appID, title, description, location, type, start, end,
                            createDate, createdBy, lastUpdate, lastUpdatedBy, customerID, userID, contactID);

                    //update table and show alerts for successful/file updated and reset radio buttons
                    Alert alert;
                    if (rowsAffected > 0) {
                        appointmentTable.setItems(AppointmentQuery.getAllAppointments(10000));
                        weeklyRadio.setSelected(false);
                        monthlyRadio.setSelected(false);
                        alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("Appointment successfully edited!");
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
                }else{
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
            editContact.setItems(contacts);
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

        editAppID.setDisable(true);
    }
}
