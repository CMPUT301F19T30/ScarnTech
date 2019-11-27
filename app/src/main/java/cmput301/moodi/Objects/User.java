package cmput301.moodi.Objects;
/*
 * Class: User
 *
 */

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;

public class User implements Comparable<User>{

    private String username;
    private String firstName;
    private String lastName;
    private String UID;
    private GeoPoint lastLocation;

    private ArrayList<Mood> moodHistory;
    private ArrayList<User> followers;
    private ArrayList<User> following;


    public User() {

    }

    public User(String username, String firstName, String lastName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /*
     * This returns the Users username.
     *
     * @return Return the username.
     */
    public String getUsername() {
        return this.username;
    }

    /*
     * This sets the Users username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /*
     * This returns the Users firstName.
     *
     * @return Return the firstName.
     */
    public String getFirstName() {
        return this.firstName;
    }

    /*
     * This sets the Users firstName.
     */
    public void setFirstName(String name) {
        this.firstName = name;
    }

    /*
     * This returns the Users firstName.
     *
     * @return Return the firstName.
     */
    public String getLastName() {
        return this.lastName;
    }

    /*
     * This sets the Users lastName.
     */
    public void setLastName(String name) {
        this.lastName = name;
    }

    public GeoPoint getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(GeoPoint lastLocation) {
        this.lastLocation = lastLocation;
    }

    public HashMap<String, Object> serializeUser() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("username", this.username);
        data.put("first_name", this.firstName);
        data.put("last_name", this.lastName);
        data.put("last_location", this.lastLocation);
        return data;
    }

    /*
     * Add follower permissions for a user with userID.
     */
    public void addFollowingPermission(String userID) {
        // Todo: add follower permissions.
    }


    /*
     * Remove follower permissions for a user with userID.
     */
    public void removeFollower(String userID) {
        // Todo: remove follower permissions.
    }

    /*
     * Add a mood to the users moodlist.
     */
    public void addMood(Mood mood) {
        // Todo: add a mood to the users moodlist.
    }

    /*
     * Remove a mood from the users moodlist.
     */
    public void removeMood(Mood mood) {
        // Todo: remove mood from users moodlist.
    }

    public String getUID() {
        return this.UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    @Override
    public int compareTo(User user) {
        return this.lastName.compareTo(user.getLastName());
    }
}
