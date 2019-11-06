package cmput301.moodi;
/*
 * Class: Location
 * Stores location data for use with Google Maps API.
 *
 */

public class Location {
    private String latitude;
    private String longitude;

    public Location(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /*
     * This returns latitude of the location.
     *
     * @return Return the latitude
     */
    public String getLatitude() {
        return this.latitude;
    }

    /*
     * This returns longitude of the location.
     *
     * @return Return the longitude
     */
    public String getLongitude() {
        return this.longitude;
    }

    /*
     * This sets the latitude of the location.
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /*
     * This sets the longitude of the location.
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
