package cmput301.moodi.Objects;
/**
 * Class: NotificationList
 * Allocate multiple notification into a list and allow app to add or
 * remove any notification in that list.
 * @since 11/04/2019
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationList {

    private List<MoodiNotification> notifications = new ArrayList<>();

    /**
     * add a notification into the list
     * @param notification
     * Notification needed to be added
     */
    public void add(MoodiNotification notification){
        if (this.notifications.contains(notification))
            throw new IllegalThreadStateException();
        this.notifications.add(notification);
    }

    /**
     * remove a notification from the list.
     * @param notification
     * Notification needed to be removed if existing in list already
     */
    public void removeNotification(MoodiNotification notification) {
        if (!this.notifications.contains(notification))
            throw new IllegalThreadStateException();
        this.notifications.remove(notification);
    }

    /**
     * This method returns a list of moods
     * @return list
     */
    public List<MoodiNotification> getList() {
        List<MoodiNotification> list = this.notifications;
        Collections.sort(list);
        return list;
    }

    public MoodiNotification get(int i) {
        return this.notifications.get(i);
    }

    /*
    get the size of the notification
     */
    public int size() {
        return this.notifications.size();
    }

    /*
    clean a notification
     */
    public void clear() {
        this.notifications.clear();
    }
}

