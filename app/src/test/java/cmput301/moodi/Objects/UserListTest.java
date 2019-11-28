package cmput301.moodi.Objects;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserListTest {


    private UserList mocklist(){
        UserList mylist = new UserList();
        //List<User> mylist = new ArrayList<>();
        User mockUser1 = new User("jdoe", "John", "Doe");
        User mockUser2 = new User("1","2","3");
        mylist.add(mockUser1);
        mylist.add(mockUser2);
        return mylist;
    }





    @Test
    void add() {
        UserList list = mocklist();
        User mockUser3 = new User("jdoe", "John", "Doe");
        list.add(mockUser3);
        assertEquals(3,list.size());

    }

    @Test
    void removeUser() {
        UserList list = mocklist();
        list.removeUser(list.get(1));
        assertEquals(1,list.size());

    }

    @Test
    void getList() {
        UserList list = mocklist();
        list.getList().clear();
        assertEquals(0,list.size());
    }

    @Test
    void get() {
        UserList list = mocklist();
        assertEquals("John",list.get(0).getFirstName());
    }

    @Test
    void size() {
        UserList list = mocklist();
        assertEquals(2,list.size());

    }

    @Test
    void clear() {
        UserList list = mocklist();
        list.clear();
        assertEquals(0,list.size());
    }
}