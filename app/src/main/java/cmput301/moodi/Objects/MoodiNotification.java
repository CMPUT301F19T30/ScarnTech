package cmput301.moodi.Objects;

import com.google.firebase.firestore.QueryDocumentSnapshot;

/**
 * Class: MoodiNotification
 * This is a class that allow user to get notification from other user or
 * send notification to other user
 * @see MoodiNotificationsAdapter
 */
public class MoodiNotification implements Comparable<MoodiNotification> {
    private String type;
    private String senderUID;
    private String senderName;
    private String receiverUID;
    private String documentID;
    private Integer response;

    public MoodiNotification() {

    }

    /**
     * Setup notification from Firestore document.
     */
    public void setFromDocument(QueryDocumentSnapshot doc) {
        this.setType(doc.getString("type"));
        this.setReceiver(doc.getString("receiver"));
        this.setSenderUID(doc.getString("sender"));
        this.documentID = doc.getId();
    }

    public String getDocumentID() {
        return this.documentID;
    }

    /*
     * Set type for notification.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @Return type for notification.
     */
    public String getType() {
        return this.type;
    }

    /*
     * Set the sender of the notification request.
     */
    public void setSenderName(String name) {
        this.senderName = name;
    }

    /*
     * Get the sender of the notification request.
     */
    public String getSenderName() {
        return this.senderName;
    }

    /**
     * @Return sender for notification.
     */
    public void setSenderUID(String uid) {
        this.senderUID = uid;
    }

    /*
     * Return sender for notification.
     */
    public String getSenderUID() {
        return this.senderUID;
    }

    /*
     * Set the reciever of the notification request.
     */
    public void setReceiver(String receiver) {
        this.receiverUID = receiver;
    }

    /**
     * @Return receiver for notification.
     */
    public String getReceiver() {
        return this.receiverUID;
    }

    /*
     * Set the response from user.
     */
    public void setResponse(int response) {
        this.response = response;
    }


    @Override
    public int compareTo(MoodiNotification moodiNotification) {
        return 0;
    }
}
