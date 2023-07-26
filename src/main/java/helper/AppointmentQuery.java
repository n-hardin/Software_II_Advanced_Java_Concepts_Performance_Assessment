package helper;

import DAO.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class for appointment CRUD operations
 */
public abstract class AppointmentQuery {

    private static final String timeZone = UserQuery.getCurrentUser().getTimeZone();

    /**
     * Method to return a list of all appointments
     * @param days how many days out from current date the list should be
     * @return Appointments
     * @throws SQLException SQLException
     */
    public static ObservableList<Appointment> getAllAppointments(int days) throws SQLException {

        Appointment appointment;
        ObservableList<Appointment> Appointments = FXCollections.observableArrayList();
        String sql = "SELECT *, " +
                     "convert_tz(Start, '+00:00', ?) AS Converted_Start, convert_tz(End, '+00:00', ?) AS Converted_End " +
                     "FROM client_schedule.appointments INNER JOIN contacts " +
                     "ON (contacts.Contact_ID = appointments.Contact_ID) " +
                     "WHERE appointments.Start BETWEEN (NOW() - INTERVAL 1 DAY) AND (NOW() + INTERVAL ? DAY)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, timeZone);
        ps.setString(2, timeZone);
        ps.setInt(3, days);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int apptID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String desc = rs.getString("Description");
            String loc = rs.getString("Location");
            String type = rs.getString("Type");
            String start = String.valueOf(rs.getString("Converted_Start"));
            String end = rs.getString("Converted_End");
            String createDate = rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int custID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            String contact = rs.getString("Contact_Name");
            int contactID = rs.getInt("Contact_ID");

            appointment = new Appointment(apptID, title, desc, loc, type, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy, custID, userID, contact, contactID);
            Appointments.add(appointment);
        }
        return Appointments;
    }

    /**
     * Method to insert appointment into appointment table
     * @param apptID        appointment ID
     * @param title         appointment title
     * @param description   appointment description
     * @param location      appointment location
     * @param contactID     appointment contact id
     * @param type          appointment type
     * @param start         appointment start date/time
     * @param end           appointment end date/time
     * @param createDate    when the appointment was created
     * @param createdBy     who created the appointment
     * @param lastUpdate    when the appointment was updated last
     * @param lastUpdatedBy who updated the appointment last
     * @param customerID    appointment customer id
     * @param userID        appointment user id
     * @return SQL statement execution
     * @throws SQLException SQLException
     */
    public static int insert(int apptID, String title, String description, String location, String type, String start, String end, String createDate, String createdBy, String lastUpdate, String lastUpdatedBy, int customerID, int userID, int contactID) throws SQLException {

        String sql = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, " +
                     "Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                     "VALUES (?, ?, ?, ?, ?, convert_tz(?, ?, '+00:00'), convert_tz(?, ?, '+00:00'), ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, apptID);
        ps.setString(2, title);
        ps.setString(3, description);
        ps.setString(4, location);
        ps.setString(5, type);
        ps.setString(6, start);
        ps.setString(7, timeZone);
        ps.setString(8, end);
        ps.setString(9, timeZone);
        ps.setString(10, createDate);
        ps.setString(11, createdBy);
        ps.setString(12, lastUpdate);
        ps.setString(13, lastUpdatedBy);
        ps.setInt(14, customerID);
        ps.setInt(15, userID);
        ps.setInt(16, contactID);
        return ps.executeUpdate();
    }

    /**
     * Method to edit an appointment
     * @param apptID        appointment ID
     * @param title         appointment title
     * @param description   appointment description
     * @param location      appointment location
     * @param type          appointment type
     * @param start         appointment start date/time
     * @param end           appointment end date/time
     * @param createDate    appointment creation date/time
     * @param createdBy     who created the appointment
     * @param lastUpdate    appointment last update date/time
     * @param lastUpdatedBy who updated the appointment last
     * @param customerID    customer ID
     * @param userID        user ID
     * @param contactID     contact ID
     * @return SQL statement execution
     * @throws SQLException SQLException
     */
    public static int update(int apptID, String title, String description, String location, String type, String start, String end, String createDate, String createdBy, String lastUpdate, String lastUpdatedBy, int customerID, int userID, int contactID) throws SQLException {

        String sql = "UPDATE appointments SET Appointment_ID = ?, Title = ?, Description = ?, Location = ?, " +
                     "Type = ?, Start = convert_tz(?, ?, '+00:00'), End = convert_tz(?, ?, '+00:00'), Create_Date = ?, Created_By = ?, Last_Update = ?, " +
                     "Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, apptID);
        ps.setString(2, title);
        ps.setString(3, description);
        ps.setString(4, location);
        ps.setString(5, type);
        ps.setString(6, start);
        ps.setString(7, timeZone );
        ps.setString(8, end);
        ps.setString(9, timeZone);
        ps.setString(10, createDate);
        ps.setString(11, createdBy);
        ps.setString(12, lastUpdate);
        ps.setString(13, lastUpdatedBy);
        ps.setInt(14, customerID);
        ps.setInt(15, userID);
        ps.setInt(16, contactID);
        ps.setInt(17, apptID);
        return ps.executeUpdate();
    }

    /**
     * Method to delete an appointment
     * @param appointmentID ID of appointment
     * @throws SQLException SQLException
     */
    public static void delete(int appointmentID) throws SQLException {

        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointmentID);
        ps.executeUpdate();
    }

    /**
     * Method to check if there is an appointment in the next 15 minutes
     * @return appointment
     * @throws SQLException SQLException
     */
    public static Appointment reminderCheck() throws SQLException {
        Appointment appointment = new Appointment();
        String sql = "SELECT Appointment_ID, convert_tz(Start, '+00:00', ?) AS Start FROM appointments " +
                     "INNER JOIN customers on appointments.Customer_ID = customers.Customer_ID  " +
                     "AND Start BETWEEN NOW() AND NOW() + INTERVAL 15 MINUTE";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, timeZone);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            appointment.setApptID(rs.getInt("Appointment_ID"));
            appointment.setStart(rs.getString("Start"));
        }
        return appointment;
    }

    public static boolean overlapCheck(String start, String end, int ID) throws SQLException{

        boolean bool = true;

        String sql = "SELECT * FROM appointments WHERE Appointment_ID !=? AND " +
                     "(End BETWEEN convert_tz(?, ?, '+00:00') AND  convert_tz(?, ?, '+00:00') " +
                     "OR Start BETWEEN convert_tz(?, ?, '+00:00') AND convert_tz(?, ?, '+00:00'))";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, ID);
        ps.setString(2, start);
        ps.setString(3, timeZone);
        ps.setString(4, end);
        ps.setString(5, timeZone);
        ps.setString(6, start);
        ps.setString(7, timeZone);
        ps.setString(8, end);
        ps.setString(9, timeZone);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            bool = false;
        }

        return bool;
    }

    /**
     * Method to check if an appointment is between business hours
     * @param start appointment start time
     * @param end   appointment end time
     * @return bool (true/false whether appointment is between business hours)
     * @throws SQLException SQLException
     */
    public static boolean hoursCheck(String start, String end) throws SQLException {

        boolean bool = false;

        String sql = "SELECT * FROM appointments WHERE TIME (convert_tz(?, ?, '-04:00')) >= '08:00:00' " +
                     "AND TIME (convert_tz(?, ?, '-04:00')) <= '22:00:00' " +
                     "AND TIME (convert_tz(?, ?, '-04:00')) >= '08:00:00' " +
                     "AND TIME (convert_tz(?, ?, '-04:00')) <= '22:00:00'";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, start);
        ps.setString(2, timeZone);
        ps.setString(3, end);
        ps.setString(4, timeZone);
        ps.setString(5, start);
        ps.setString(6, timeZone);
        ps.setString(7, end);
        ps.setString(8, timeZone);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            bool = true;
        }

        return bool;
    }
}
