package cmput301.moodi;

import org.junit.jupiter.api.Test;

import cmput301.moodi.Objects.User;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class: UserTest
 * This class test method in the User class
 */
class UserTest {

    /**
     * Create a temp User object to be test
     * @return
     * a User object
     */
    private User mockUser() {
        User mockUser = new User("jdoe", "John", "Doe");
        return mockUser;
    }

    @Test
    void testGetUserName() {
        User mockUser = mockUser();
        assertEquals("jdoe", mockUser.getUsername());
    }

    @Test
    void testGetFirstName() {
        User mockUser = mockUser();
        assertEquals("John", mockUser.getFirstName());
    }

    @Test
    void testGetLastName() {
        User mockUser = mockUser();
        assertEquals("Doe", mockUser.getLastName());
    }

    @Test
    void testSetUserName() {
        User mockUser = mockUser();
        mockUser.setUsername("johnny_doe");
        assertEquals("johnny_doe", mockUser.getUsername());
    }

    @Test
    void testSetFirstName() {
        User mockUser = mockUser();
        mockUser.setFirstName("Steve");
        assertEquals("Steve", mockUser.getFirstName());
    }

    @Test
    void testSetLastName() {
        User mockUser = mockUser();
        mockUser.setLastName("Smith");
        assertEquals("Smith", mockUser.getLastName());
    }
}
