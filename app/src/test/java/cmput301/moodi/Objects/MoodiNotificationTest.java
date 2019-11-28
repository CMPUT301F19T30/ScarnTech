package cmput301.moodi.Objects;

import org.junit.Test;

import static org.junit.Assert.*;
import cmput301.moodi.Objects.MoodiNotification;
public class MoodiNotificationTest {

    private MoodiNotification cmockmoodiNotification(){
        MoodiNotification mockmoodiNotification = new MoodiNotification();
        mockmoodiNotification.setReceiver("1");
        mockmoodiNotification.setResponse(1);
        mockmoodiNotification.setSenderName("1");
        mockmoodiNotification.setSenderUID("1");
        mockmoodiNotification.setType("1");
        return mockmoodiNotification;

    }

    @Test
    public void setType() {
        MoodiNotification mockmoodiNotification = cmockmoodiNotification();
        mockmoodiNotification.setType("3");
        assertEquals("3",mockmoodiNotification.getType());
    }

    @Test
    public void getType() {
        MoodiNotification mockmoodiNotification = cmockmoodiNotification();
        assertEquals("1",mockmoodiNotification.getType());
    }

    @Test
    public void setSenderName() {
        MoodiNotification mockmoodiNotification = cmockmoodiNotification();
        mockmoodiNotification.setSenderName("2");
        assertEquals("2",mockmoodiNotification.getSenderName());
    }

    @Test
    public void getSenderName() {
        MoodiNotification mockmoodiNotification = cmockmoodiNotification();
        assertEquals("1",mockmoodiNotification.getSenderName());

    }

    @Test
    public void setSenderUID() {
        MoodiNotification mockmoodiNotification = cmockmoodiNotification();
        mockmoodiNotification.setSenderUID("2");
        assertEquals("2",mockmoodiNotification.getSenderUID());
    }

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