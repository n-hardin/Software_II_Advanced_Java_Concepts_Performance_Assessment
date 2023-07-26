package controller;

import DAO.JDBC;
import application.Program;
import helper.CustomerQuery;
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
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Class to add a customer to database
 */
public class AddCustomer implements Initializable {

    @FXML
    private TableView<model.Customer> customerTable;
    @FXML
    private TableColumn<Object, Object> customerID;
    @FXML
    private TableColumn<Object, Object> name;
    @FXML
    private TableColumn<Object, Object> address;
    @FXML
    private TableColumn<Object, Object> postal;
    @FXML
    private TableColumn<Object, Object> division;
    @FXML
    private TableColumn<Object, Object> phone;
    @FXML
    private TextField addID;
    @FXML
    private TextField addName;
    @FXML
    private TextField addAddress;
    @FXML
    private TextField addPostal;
    @FXML
    private TextField addPhone;
    @FXML
    private ComboBox<String> divisionCombo;
    @FXML
    private ComboBox<String> countryCombo;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

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
     * Displays corresponding divisions in the dropdown when a country is selected
     * @param event event
     * @throws SQLException SQLException
     */
    public void CountrySelect(ActionEvent event) throws SQLException {

        int country = countryCombo.getSelectionModel().getSelectedIndex() + 1;
        String sql = "SELECT Division FROM client_schedule.first_level_divisions WHERE Country_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, country);
        ResultSet rs = ps.executeQuery();
        ObservableList<String> divisions = FXCollections.observableArrayList();
        while (rs.next()) {
            divisions.add(rs.getString("Division"));
        }
        divisionCombo.setItems(divisions);
        event.consume();
    }

    /**
     * Method to add customer
     * @param event event
     * @throws SQLException SQLException
     * @throws IOException IOException
     */
    public void ButtonSave(ActionEvent event) throws SQLException, IOException {

        int customerID = 0;
        String name = addName.getText();
        String address = addAddress.getText();
        String postal = addPostal.getText();
        String phone = addPhone.getText();
        String createDate = dateTime;
        String createdBy = UserQuery.currentUser.getUserName();
        String lastUpdate = dateTime;
        String lastUpdatedBy = UserQuery.currentUser.getUserName();
        int divisionID = 0;

        //math to convert selected division into usable division ID
        String division = divisionCombo.getSelectionModel().getSelectedItem();
        String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, division);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            divisionID = rs.getInt("Division_ID");
        }

        //check to ensure no fields are blank
        if(Objects.equals(name, "") || Objects.equals(address, "") || Objects.equals(postal, "")
                || Objects.equals(phone, "") || divisionID == 0){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Information missing, please ensure all fields have data entered in them");
            alert.show();
        }else{
            //actual insert statement
            int rowsAffected = CustomerQuery.insert(customerID, name, address, postal, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, divisionID);
            //Alerts for successful/failed insert
            Alert alert;
            if (rowsAffected > 0) {
                customerTable.setItems(CustomerQuery.getAllCustomers());
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Customer successfully added!");
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

        //populate divisions dropdown
        try {
            String sql = "SELECT * FROM client_schedule.first_level_divisions";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ObservableList<String> divisions = FXCollections.observableArrayList();
            while (rs.next()) {
                divisions.add(rs.getString("Division"));
            }
            divisionCombo.setItems(divisions);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //populate country dropdown
        try {
            String sql = "SELECT * FROM client_schedule.countries";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ObservableList<String> countries = FXCollections.observableArrayList();
            while (rs.next()) {
                countries.add(rs.getString("Country"));
            }
            countryCombo.setItems(countries);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //populate table
        try {
            customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            name.setCellValueFactory(new PropertyValueFactory<>("name"));
            address.setCellValueFactory(new PropertyValueFactory<>("address"));
            postal.setCellValueFactory(new PropertyValueFactory<>("postal"));
            division.setCellValueFactory(new PropertyValueFactory<>("division"));
            phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
            customerTable.setItems(CustomerQuery.getAllCustomers());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //disable ID field
        addID.setText("Auto-Generated");
        addID.setDisable(true);
    }
}
