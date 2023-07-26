package model;

/**
 * User class
 */
public class User {

    private int userID;
    private String userName;
    private String password;
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdatedBy;
    private String timeZone;

    /**
     * @return the user's id
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @param userID the ID of the user
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * @return the user's name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the user's name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the creation date of the user's account
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the date the user's account was created
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /**
     * @return who or what created the user's account
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy who or what created the user's account
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the last time the user's account was updated
     */
    public String getLastUpdate() {
        return lastUpdate;
    }

    /**
     * @param lastUpdate the last time the user's account was updated
     */
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * @return who or what updated the user's account last
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * @param lastUpdatedBy who or what updated the user's account last
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * @return timeZone
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * @param timeZone user's timezone offset from UTC
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
