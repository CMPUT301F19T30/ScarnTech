package cmput301.moodi;
/*
 * Class: User
 *
 * Still need to connect User class to firestore.
 *
 */

import java.util.ArrayList;

public class User {

    private String username;
    private String firstName;
    private String lastName;

    private ArrayList<User> permissionList;
    private ArrayList<Mood> moodHistory;

    //FirebaseFirestore db;
    //final CollectionReference collectionReference = db.collection("users");

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

    /*
     * Add follower permissions for a user with userID.
     */
    public void addPermission(String userID) {
        // TO-DO
    }


    /*
     * Remove follower permissions for a user with userID.
     */
    public void removePermission(String userID) {
        // TO-DO
    }

    /*
     * Add a mood to the users moodlist.
     */
    public void addMood(Mood mood) {
        // TO-DO
    }

    /*
     * Remove a mood from the users moodlist.
     */
    public void removeMood(Mood mood) {
        // TO-DO
    }
}
