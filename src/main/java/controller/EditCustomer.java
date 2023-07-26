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
import model.Customer;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for edit customer page
 */
public class EditCustomer implements Initializable {


    @FXML
    private TableView<Customer> customerTable;
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
    private TextField editID;
    @FXML
    private TextField editName;
    @FXML
    private TextField editAddress;
    @FXML
    private TextField editPostal;
    @FXML
    private TextField editPhone;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private ComboBox<String> divisionCombo;
    @FXML
    private ComboBox<String> countryCombo;


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
     * Method to populate TextFields for the customer to be edited
     * @param event event
     * @throws SQLException SQLException
     */
    public void ButtonEdit(ActionEvent event) throws SQLException {

        //set TextFields
        Customer customer = customerTable.getSelectionModel().getSelectedItem();
        editID.setText(String.valueOf(customer.getCustomerID()));
        editName.setText(customer.getName());
        editAddress.setText(customer.getAddress());
        editPostal.setText(customer.getPostal());
        editPhone.setText(customer.getPhone());

        //math to convert division ID to division name and set ComboBox
        int divisionID = customer.getDivisionID();
        String division = "";
        String divisionSQL = "SELECT Division FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement divisionPS = JDBC.connection.prepareStatement(divisionSQL);
        divisionPS.setInt(1, divisionID);
        ResultSet divisionRS = divisionPS.executeQuery();
        while (divisionRS.next()) {
            division = divisionRS.getString("Division");
        }
        divisionCombo.setValue(division);

        //math to convert division ID to country name and set ComboBox
        String country = "";
        String countrySQL = "SELECT Country FROM countries INNER JOIN first_level_divisions " +
                "WHERE countries.Country_ID = first_level_divisions.Country_ID " +
                "AND first_level_divisions.Division_ID = ?";
        PreparedStatement countryPS = JDBC.connection.prepareStatement(countrySQL);
        countryPS.setInt(1, divisionID);
        ResultSet countryRS = countryPS.executeQuery();
        while (countryRS.next()) {
            country = countryRS.getString("Country");
        }
        countryCombo.setValue(country);

        event.consume();
    }

    /**
     * Method to delete a customer and all of their appointments when a button is pressed - opens an alert box to confirm
     * before deletion
     * @param event event
     * @throws SQLException SQLException
     */
    public void ButtonDelete(ActionEvent event) throws SQLException {

        if (customerTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No customer selected, please select a customer to delete");
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to delete this customer and all of their appointments?");
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
                Customer customer = customerTable.getSelectionModel().getSelectedItem();
                int ID = customer.getCustomerID();
                CustomerQuery.delete(ID);
                customerTable.setItems(CustomerQuery.getAllCustomers());
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setContentText("Customer: " + customer.getName() + " successfully deleted");
                confirm.show();
                event.consume();
            }
        }
    }

    /**
     * Method to save edited customer
     * @param event event
     * @throws SQLException SQLException
     * @throws IOException IOException
     */
    public void ButtonSave(ActionEvent event) throws SQLException, IOException {

        //error alert if customer to edit has not been selected
        if (editID.getText().equals("")) {
            Alert noSelection;
            noSelection = new Alert((Alert.AlertType.ERROR));
            noSelection.setContentText("Please select a customer to edit");
            noSelection.show();
        } else {

            Customer customer = customerTable.getSelectionModel().getSelectedItem();
            //set variables
            int customerID = Integer.parseInt(editID.getText());
            String name = editName.getText();
            String address = editAddress.getText();
            String postal = editPostal.getText();
            String phone = editPhone.getText();
            String createDate = customer.getCreateDate();
            String createdBy = customer.getCreatedBy();
            String lastUpdate = dateTime;
            String lastUpdatedBy = UserQuery.currentUser.getUserName();
            int divisionID = 0;

            //math to retrieve who created the customer
            String createdSQL = "SELECT Created_By FROM customers WHERE Customer_ID = ?";
            PreparedStatement createdPS = JDBC.connection.prepareStatement(createdSQL);
            createdPS.setString(1, String.valueOf(customerID));
            ResultSet createdRS = createdPS.executeQuery();
            while (createdRS.next()) {
                createdBy = createdRS.getString("Created_By");
            }

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
            }else {
                //actual update statement
                int rowsAffected = CustomerQuery.update(customerID, name, address, postal, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, divisionID);

                //update table and show alerts for successful/file updated
                Alert alert;
                if (rowsAffected > 0) {
                    customerTable.setItems(CustomerQuery.getAllCustomers());
                    alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Customer successfully edited!");
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

        editID.setDisable(true);
    }
}
