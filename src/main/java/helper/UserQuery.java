package helper;

import DAO.JDBC;
import model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class for various user CRUD operations
 */
public abstract class UserQuery {

    private static final ZonedDateTime ZDT = ZonedDateTime.now();
    private static final DateTimeFormatter getZone = DateTimeFormatter.ofPattern("ZZZZZ");
    private static final String dtOffset = ZDT.format(getZone);
    public static User currentUser;

    /**
     * @return currentUser
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Checks entered username and password against database to find matching account if one exists
     * @param userName Entered username
     * @param password Entered password
     * @return rs.next() (true/false if one exists)
     * @throws SQLException SQLException
     */
    public static boolean loginCheck(String userName, String password) throws SQLException {

        String sql = "SELECT * FROM client_schedule.users WHERE User_Name=? && Password=?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, userName);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            currentUser = new User();
            currentUser.setUserID(rs.getInt("User_ID"));
            currentUser.setUserName(rs.getString("User_Name"));
            currentUser.setPassword(rs.getString("Password"));
            currentUser.setCreateDate(rs.getString("Create_Date"));
            currentUser.setCreatedBy(rs.getString("Created_By"));
            currentUser.setLastUpdate(rs.getString("Last_Update"));
            currentUser.setLastUpdatedBy(rs.getString("Last_Updated_By"));
            currentUser.setTimeZone(dtOffset);
            return true;
        }
        return rs.next();
    }
}
