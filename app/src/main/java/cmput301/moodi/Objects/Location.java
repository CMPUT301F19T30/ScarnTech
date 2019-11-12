package cmput301.moodi.Objects;
/*
 * Class: Location
 * Stores location data for use with Google Maps API.
 * 11/09/2019
 */

public class Location {
    private double latitude;
    private double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /*
     * This returns latitude of the location.
     *
     * @return Return the latitude
     */
    public double getLatitude() {
        return this.latitude;
    }

    /*
     * This returns longitude of the location.
     *
     * @return Return the longitude
     */
    public double getLongitude() {
        return this.longitude;
    }

    /*
     * This sets the latitude of the location.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /*
     * This sets the longitude of the location.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /*
     * Returns an array of location [longitude, latitude].
     */
    public double[] toArray(){
        double[] location = {this.latitude, this.longitude};
        return location;
    }

    /*
     * Serializes location object to string.
     */
    public String serializeLocation() {
        return "[" + latitude + ", " + longitude + "]";
    }
}
