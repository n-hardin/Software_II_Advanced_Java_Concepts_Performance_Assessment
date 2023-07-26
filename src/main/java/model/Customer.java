package model;

/**
 * Customer class
 */
public class Customer {

    private int customerID;
    private String name;
    private String address;
    private String postal;
    private String phone;
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdatedBy;
    private int divisionID;
    private int number;
    private String division;

    /**
     * @param customerID    customer table primary key
     * @param name          customer name
     * @param address       customer address
     * @param postal        customer postal code
     * @param phone         customer phone number
     * @param createDate    date customer account was created
     * @param createdBy     who created the customer
     * @param lastUpdate    last time customer account was updated
     * @param lastUpdatedBy who created the customer account
     * @param divisionID    first_level_division foreign key
     * @param division      first_level_division name
     */
    public Customer(int customerID, String name, String address, String postal, String phone, String createDate, String createdBy, String lastUpdate, String lastUpdatedBy, int divisionID, String division) {
        this.customerID = customerID;
        this.name = name;
        this.address = address;
        this.postal = postal;
        this.phone = phone;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionID = divisionID;
        this.division = division;
    }

    /**
     * Constructor for demographic report
     * @param division division
     * @param number   number of customers in division
     */
    public Customer(String division, int number) {
        this.division = division;
        this.number = number;
    }

    /**
     * @return customerID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * @param customerID customer account ID
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name customer's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address customer's address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return postal
     */
    public String getPostal() {
        return postal;
    }

    /**
     * @param postal customer's postal code
     */
    public void setPostal(String postal) {
        this.postal = postal;
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone customer's phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return createDate
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate customer account date of creation
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /**
     * @return createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy who created the customer account
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return lastUpdate
     */
    public String getLastUpdate() {
        return lastUpdate;
    }

    /**
     * @param lastUpdate when the customer account was updated last
     */
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * @return lastUpdatedBy
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * @param lastUpdatedBy last time customer account was updated
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * @return divisionID
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * @param divisionID foreign key for division table
     */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /**
     * @return number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number number of people in division
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * @return division
     */
    public String getDivision() {
        return division;
    }

    /**
     * @param division name of division
     */
    public void setDivision(String division) {
        this.division = division;
    }
}
