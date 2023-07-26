package helper;

import DAO.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class for various customer CRUD operations
 */
public abstract class CustomerQuery {

    /**
     * @return a list of all customers
     * @throws SQLException SQLException
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {

        Customer customer;
        ObservableList<Customer> Customers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM client_schedule.customers INNER JOIN first_level_divisions " +
                     "ON (first_level_divisions.Division_ID = customers.Division_ID)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int customerID = rs.getInt("Customer_ID");
            String name = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postal = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            String createDate = rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int divisionID = rs.getInt("Division_ID");
            String division = rs.getString("Division");

            customer = new Customer(customerID, name, address, postal, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, divisionID, division);
            Customers.add(customer);
        }
        return Customers;
    }

    /**
     * Method to insert a customer the customer table
     * @param customerID    customer ID
     * @param name          customer name
     * @param address       customer address
     * @param postal        customer postal code
     * @param phone         customer phone number
     * @param createDate    customer account creation date
     * @param createdBy     who created the customer account
     * @param lastUpdate    last time customer account was updated
     * @param lastUpdatedBy who updated the customer account last
     * @param divisionID    division ID
     * @return SQL statement execution
     * @throws SQLException SQLException
     */
    public static int insert(int customerID, String name, String address, String postal, String phone, String createDate, String createdBy, String lastUpdate, String lastUpdatedBy, int divisionID) throws SQLException {

        String sql = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, " +
                     "Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerID);
        ps.setString(2, name);
        ps.setString(3, address);
        ps.setString(4, postal);
        ps.setString(5, phone);
        ps.setString(6, createDate);
        ps.setString(7, createdBy);
        ps.setString(8, lastUpdate);
        ps.setString(9, lastUpdatedBy);
        ps.setInt(10, divisionID);
        return ps.executeUpdate();
    }

    /**
     * Method to edit a customer
     * @param customerID    customer ID
     * @param name          customer name
     * @param address       customer address
     * @param postal        customer postal code
     * @param phone         customer phone number
     * @param createDate    customer account creation date/time
     * @param createdBy     who created the customer account
     * @param lastUpdate    customer account last update date/time
     * @param lastUpdatedBy who updated customer account last
     * @param divisionID    division of the customer's address
     * @return SQL statement execution
     * @throws SQLException SQLException
     */
    public static int update(int customerID, String name, String address, String postal, String phone, String createDate, String createdBy, String lastUpdate, String lastUpdatedBy, int divisionID) throws SQLException {

        String sql = "UPDATE customers SET Customer_ID = ?, Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerID);
        ps.setString(2, name);
        ps.setString(3, address);
        ps.setString(4, postal);
        ps.setString(5, phone);
        ps.setString(6, createDate);
        ps.setString(7, createdBy);
        ps.setString(8, lastUpdate);
        ps.setString(9, lastUpdatedBy);
        ps.setInt(10, divisionID);
        ps.setInt(11, customerID);
        return ps.executeUpdate();
    }

    /**
     * Method to delete a customer (deletes any appointments held by the customer first
     * @param customerID ID of customer account
     * @throws SQLException SQLException
     */
    public static void delete(int customerID) throws SQLException {

        //delete appointments held by the customer
        String appSQL = "DELETE FROM appointments WHERE Customer_ID = ?";
        PreparedStatement appPS = JDBC.connection.prepareStatement(appSQL);
        appPS.setInt(1, customerID);
        appPS.executeUpdate();

        //delete the customer
        String custSQL = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement custPS = JDBC.connection.prepareStatement(custSQL);
        custPS.setInt(1, customerID);
        custPS.executeUpdate();
    }
}
