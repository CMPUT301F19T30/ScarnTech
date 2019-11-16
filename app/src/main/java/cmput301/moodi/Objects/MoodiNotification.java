package cmput301.moodi.Objects;

public class MoodiNotification implements Comparable<MoodiNotification> {
    private String type;
    private String notificationSender;
    private String notificationReceiver;

    public MoodiNotification() {

    }

    /*
     * Set type for notification.
     */
    public void setType(String type) {
        this.type = type;
    }

    /*
     * Return type for notification.
     */
    public String getType() {
        return this.type;
    }

    /*
     * Set the sender of the notification request.
     */
    public void setNotificationSender(String sender) {
        this.notificationSender = sender;
    }

    /*
     * Return sender for notification.
     */
    public String getSender() {
        return this.notificationSender;
    }

    /*
     * Set the reciever of the notification request.
     */
    public void setNotificationReceiver(String receiver) {
        this.notificationReceiver = receiver;
    }

    /*
     * Return receiver for notification.
     */
    public String getReceiver() {
        return this.notificationReceiver;
    }


    @Override
    public int compareTo(MoodiNotification moodiNotification) {
        return 0;
    }
}
