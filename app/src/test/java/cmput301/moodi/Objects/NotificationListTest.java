package cmput301.moodi.Objects;

import android.app.Notification;

import org.junit.Test;

import static org.junit.Assert.*;

public class NotificationListTest {

    private NotificationList mockNotiList(){
        NotificationList mylist = new NotificationList();
        MoodiNotification not1 = new MoodiNotification();
        not1.setType("1");
        not1.setResponse(2);
        not1.setReceiver("3");
        not1.setSenderUID("4");
        not1.setSenderName("5");
        mylist.add(not1);
        return mylist;
    }

    @Test
    public void add() {
        NotificationList mylist = mockNotiList();
        MoodiNotification not2 = new MoodiNotification();
        mylist.add(not2);
        assertEquals(2,mylist.size());
    }

    @Test
    public void removeNotification() {
        NotificationList mylist = mockNotiList();
        mylist.removeNotification(mylist.get(0));
        assertEquals(0,mylist.size());

    }

    @Test
    public void getList() {
        NotificationList mylist = mockNotiList();
        assertNotNull(mylist.getList());

    }

    @Test
    public void get() {
        NotificationList mylist = mockNotiList();
        assertEquals("5",mylist.get(0).getSenderName());
    }

    @Test
    public void size() {
        NotificationList mylist = mockNotiList();
        MoodiNotification not2 = new MoodiNotification();
        mylist.add(not2);
        assertEquals(2,mylist.size());

    }

    @Test
    public void clear() {
        NotificationList mylist = mockNotiList();
        mylist.clear();
        assertEquals(0,mylist.size());
    }
}