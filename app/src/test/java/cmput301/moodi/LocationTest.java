package cmput301.moodi;

import org.junit.jupiter.api.Test;

import cmput301.moodi.Objects.Location;
import cmput301.moodi.Objects.User;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class:LocationTest
 * this class test method in Location
 */
class LocationTest {

    private User mockUser() {
        User mockUser = new User("jdoe", "John", "Doe");
        return mockUser;
    }

    /**
     * this method create  a temp location to be test
     * @return
     * temp location object
     */
    private Location mockLocation() {
        Location location = new Location(53.18923, -113.12312);
        return location;
    }

    @Test
    void testGetLatitude() {
        Location mockLocation = mockLocation();
        assertEquals(53.18923, mockLocation.getLatitude());
    }

    @Test
    void testGetLongitude() {
        Location mockLocation = mockLocation();
        assertEquals(-113.12312, mockLocation.getLongitude());
    }

    @Test
    void testSetLatitude() {
        Location mockLocation = mockLocation();
        mockLocation.setLatitude(57.123);
        assertEquals(57.123, mockLocation.getLatitude());
    }

    @Test
    void testSetLongitude() {
        Location mockLocation = mockLocation();
        mockLocation.setLongitude(124.123);
        assertEquals(124.123, mockLocation.getLongitude());
    }
}
