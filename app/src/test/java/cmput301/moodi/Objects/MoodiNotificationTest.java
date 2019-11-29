package cmput301.moodi.Objects;

import org.junit.Test;

import static org.junit.Assert.*;
import cmput301.moodi.Objects.MoodiNotification;

/**
 * Class: MoodiNotificationTest
 * this class test the functionality of MoodiNotificaiton class
 * @since 27/11/2019
 */
public class MoodiNotificationTest {

    /**
     * this method create a temp Notification to be test
     * @return
     * a temp Notification object
     */
    private MoodiNotification cmockmoodiNotification(){
        MoodiNotification mockmoodiNotification = new MoodiNotification();
        mockmoodiNotification.setReceiver("1");
        mockmoodiNotification.setResponse(1);
        mockmoodiNotification.setSenderName("1");
        mockmoodiNotification.setSenderUID("1");
        mockmoodiNotification.setType("1");
        return mockmoodiNotification;

    }
    /*
    this method test setType and exam it using getType method.
     */
    @Test
    public void setType() {
        MoodiNotification mockmoodiNotification = cmockmoodiNotification();
        mockmoodiNotification.setType("3");
        assertEquals("3",mockmoodiNotification.getType());
    }
    /*
    this method test getType and exam it.
     */
    @Test
    public void getType() {
        MoodiNotification mockmoodiNotification = cmockmoodiNotification();
        assertEquals("1",mockmoodiNotification.getType());
    }
    /*
    this method test setSenderName and exam it using getSenderName method.
     */
    @Test
    public void setSenderName() {
        MoodiNotification mockmoodiNotification = cmockmoodiNotification();
        mockmoodiNotification.setSenderName("2");
        assertEquals("2",mockmoodiNotification.getSenderName());
    }
    /*
   this method test getSenderName and compare it with preset value.
    */
    @Test
    public void getSenderName() {
        MoodiNotification mockmoodiNotification = cmockmoodiNotification();
        assertEquals("1",mockmoodiNotification.getSenderName());

    }
    /*
    this method test setSenderUID and exam it using getSenderUID method.
     */
    @Test
    public void setSenderUID() {
        MoodiNotification mockmoodiNotification = cmockmoodiNotification();
        mockmoodiNotification.setSenderUID("2");
        assertEquals("2",mockmoodiNotification.getSenderUID());
    }

    /*
   this method test getSenderUID and compare it with preset value.
    */

    @Test
    public void getSenderUID() {
        MoodiNotification mockmoodiNotification = cmockmoodiNotification();
        assertEquals("1",mockmoodiNotification.getSenderUID());
    }

    @Test
    public void setReceiver() {
        MoodiNotification mockmoodiNotification = cmockmoodiNotification();
        mockmoodiNotification.setReceiver("2");
        assertEquals("2",mockmoodiNotification.getReceiver());
    }

    @Test
    public void getReceiver() {
        MoodiNotification mockmoodiNotification = cmockmoodiNotification();
        assertEquals("1",mockmoodiNotification.getReceiver());
    }

    @Test
    public void setResponse() {
        MoodiNotification mockmoodiNotification = cmockmoodiNotification();
        mockmoodiNotification.setResponse(1);

    }
}