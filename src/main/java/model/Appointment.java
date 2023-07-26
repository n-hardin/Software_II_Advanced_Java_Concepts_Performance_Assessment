package model;

/**
 * Appointment class
 */
public class Appointment {

    private int apptID;
    private int customerID;
    private int userID;
    private int contactID;
    private String title;
    private String description;
    private String location;
    private String type;
    private String start;
    private String end;
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdatedBy;
    private String contact;

    //months for report
    private int january;
    private int february;
    private int march;
    private int april;
    private int may;
    private int june;
    private int july;
    private int august;
    private int september;
    private int october;
    private int november;
    private int december;

    /**
     * Empty constructor
     */
    public Appointment() {

    }

    /**
     * @param apptID        appointment table primary key
     * @param title         appointment title
     * @param description   appointment description
     * @param location      appointment location
     * @param type          appointment type
     * @param start         appointment start date/time
     * @param end           appointment end date/time
     * @param createDate    date appointment was created
     * @param createdBy     who created the appointment
     * @param lastUpdate    last time appointment was updated
     * @param lastUpdatedBy who updated the appointment last
     * @param customerID    foreign key referencing customer table
     * @param userID        foreign key referencing user table
     * @param contact       name of contact based on FK contactID
     * @param contactID     foreign key referencing contact table
     */
    public Appointment(int apptID, String title, String description, String location, String type, String start, String end, String createDate, String createdBy, String lastUpdate, String lastUpdatedBy, int customerID, int userID, String contact, int contactID) {
        this.apptID = apptID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.customerID = customerID;
        this.userID = userID;
        this.contact = contact;
        this.contactID = contactID;
    }

    /**
     * Constructor made for the appointments by type report
     * @param type      appointment type
     * @param january   appointments in January
     * @param february  appointments in February
     * @param march     appointments in March
     * @param april     appointments in April
     * @param may       appointments in May
     * @param june      appointments in June
     * @param july      appointments in July
     * @param august    appointments in August
     * @param september appointments in September
     * @param october   appointments in October
     * @param november  appointments in November
     * @param december  appointments in December
     */
    public Appointment(String type, int january, int february, int march, int april, int may, int june, int july, int august, int september, int october, int november, int december) {
        this.type = type;
        this.january = january;
        this.february = february;
        this.march = march;
        this.april = april;
        this.may = may;
        this.june = june;
        this.july = july;
        this.august = august;
        this.september = september;
        this.october = october;
        this.november = november;
        this.december = december;
    }

    /**
     * @return apptID
     */
    public int getApptID() {
        return apptID;
    }

    /**
     * @param apptID primary key for appointment table
     */
    public void setApptID(int apptID) {
        this.apptID = apptID;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title appointment title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description appointment description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return getLocation
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location appointment location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type appointment type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return start
     */
    public String getStart() {
        return start;
    }

    /**
     * @param start appointment start date/time
     */
    public void setStart(String start) {
        this.start = start;
    }

    /**
     * @return end
     */
    public String getEnd() {
        return end;
    }

    /**
     * @param end appointment end date/time
     */
    public void setEnd(String end) {
        this.end = end;
    }

    /**
     * @return createDate
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate date appointment was created
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
     * @param createdBy who created the appointment
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
     * @param lastUpdate last time appointment was updated
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
     * @param lastUpdatedBy who updated the appointment last
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * @return customerID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * @param customerID foreign key referencing customer table
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * @return userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @param userID foreign key referencing user table
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * @return contact
     */
    public String getContact() {
        return contact;
    }

    /**
     * @param contact name of contact based on FK contact ID
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * @return contactID
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * @param contactID foreign key referencing contact table
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     * @return january
     */
    public int getJanuary() {
        return january;
    }

    /**
     * @param january appointments in January
     */
    public void setJanuary(int january) {
        this.january = january;
    }

    /**
     * @return february
     */
    public int getFebruary() {
        return february;
    }

    /**
     * @param february appointments in February
     */
    public void setFebruary(int february) {
        this.february = february;
    }

    /**
     * @return march
     */
    public int getMarch() {
        return march;
    }

    /**
     *
     * @param march appointments in March
     */
    public void setMarch(int march) {
        this.march = march;
    }

    /**
     * @return april
     */
    public int getApril() {
        return april;
    }

    /**
     * @param april appointments in April
     */
    public void setApril(int april) {
        this.april = april;
    }

    /**
     * @return may
     */
    public int getMay() {
        return may;
    }

    /**
     * @param may appointments in May
     */
    public void setMay(int may) {
        this.may = may;
    }

    /**
     * @return june
     */
    public int getJune() {
        return june;
    }

    /**
     * @param june appointments in June
     */
    public void setJune(int june) {
        this.june = june;
    }

    /**
     * @return july
     */
    public int getJuly() {
        return july;
    }

    /**
     * @param july appointments in July
     */
    public void setJuly(int july) {
        this.july = july;
    }

    /**
     * @return august
     */
    public int getAugust() {
        return august;
    }

    /**
     * @param august appointments in August
     */
    public void setAugust(int august) {
        this.august = august;
    }

    /**
     * @return september
     */
    public int getSeptember() {
        return september;
    }

    /**
     * @param september appointments in September
     */
    public void setSeptember(int september) {
        this.september = september;
    }

    /**
     * @return october
     */
    public int getOctober() {
        return october;
    }

    /**
     * @param october appointments in October
     */
    public void setOctober(int october) {
        this.october = october;
    }

    /**
     * @return november
     */
    public int getNovember() {
        return november;
    }

    /**
     * @param november appointments in November
     */
    public void setNovember(int november) {
        this.november = november;
    }

    /**
     * @return december
     */
    public int getDecember() {
        return december;
    }

    /**
     * @param december appointments in December
     */
    public void setDecember(int december) {
        this.december = december;
    }
}
