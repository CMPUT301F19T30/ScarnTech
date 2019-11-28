package cmput301.moodi.Objects;
/**
 * Class: UserList
 * @since 11/04/2019
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserList {

    private List<User> users = new ArrayList<>();

    /**
     * Create method to add an existing city to our list for testing
     * @param user User needed to be added
     */
    public void add(User user){
        if (this.users.contains(user))
            throw new IllegalThreadStateException();
        this.users.add(user);
    }

    /**
     * Create method to delete an existing mood or send exception if mood not in list
     * @param user User needed to be removed if existing in list already
     */
    public void removeUser(User user) {
        if (!this.users.contains(user))
            throw new IllegalThreadStateException();
        this.users.remove(user);
    }

    /**
     * This method returns a list of moods
     * @return list
     */
    public List<User> getList() {
        List<User> list = this.users;
        Collections.sort(list);
        return list;
    }

    public User get(int i) {
        return this.users.get(i);
    }

    public int size() {
        return this.users.size();
    }

    public void clear() {
        this.users.clear();
    }
}

