package model;

/**
 * Country class
 */
public class Country {

    private int countryID;
    private String countryName;

    /**
     * @param countryID   country ID
     * @param countryName name of Country
     */
    public Country(int countryID, String countryName) {
        this.countryID = countryID;
        this.countryName = countryName;
    }

    /**
     * @return countryID
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * @param countryID country ID
     */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /**
     * @return countryName
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * @param countryName name of Country
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}

