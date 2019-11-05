package cmput301.moodi;

import java.util.ArrayList;

public class User {
    private String username;
    private String firstName;
    private String lastName;

    private ArrayList<User> permissionList;
    private ArrayList<Mood> moodHistory;

    public User() {

    }

    public String getUserName() {
        return this.username;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public void setLastName(String name) {

    }
}
