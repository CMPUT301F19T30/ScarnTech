package cmput301.moodi.Objects;
/*
 * Class: Mood
 * Version 1: Create a mood object along with its properties and getters & setters
 * 11/04/2019
 */

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Date;
import java.util.HashMap;

public class Mood implements Comparable<Mood> {

    private String date;
    private final static String TAG = "Mood class";
    private EmotionalState emotionalState = new EmotionalState();

    private String reason = "";
    private String socialSituation = "";
    private GeoPoint location;
    private Number emotionalIndex = 0;
    private String image = "";
    private String UID;
    private String username;

    // Unique ID of a post assigned by FireBase
    private String keyID;

    /*
     * Constructor for creation of a mood from a post which is sent to FireBase with an image
     * (POSTS)
     */
    public Mood(int index, String reason, String SocialSituation, String date, String image, String username) {
        this.emotionalState.setEmotionalState(index);
        this.reason = reason;
        this.socialSituation = SocialSituation;
        this.date = date;
        this.image = image;
        this.username = username;
    }
    /*
     * Constructor for creation of a mood from a post which is sent to FireBase without an image
     * (POSTS)
     */
    public Mood(int index, String reason, String SocialSituation, String date, String username) {
        this.emotionalState.setEmotionalState(index);
        this.reason = reason;
        this.socialSituation = SocialSituation;
        this.date = date;
        this.image = "No Photo Available";
        this.username = username;
    }

    // TODO: Clean up constructors and make sure it doesnt make a new date when pulling a post from database
    /*
    * Constructor for receiving a post from the FireBase and constructing a new mood
    * (HOME & PROFILE)
     */

    public Mood(String reason, String date, String socialSituation, String keyID, int index, String image, String username) {
        this.emotionalState.setEmotionalState(index);
        this.reason = reason;
        this.date = date;
        this.socialSituation = socialSituation;
        this.keyID = keyID;
        this.image = image;
        this.getEmotionalState().setIndex((index));
        this.username = username;}

    public Mood(String reason, String date, String socialSituation, String keyID, int index, String image) {
        this.emotionalState.setEmotionalState(index);
        this.reason = reason;
        this.date = date;
        this.socialSituation = socialSituation;
        this.keyID = keyID;
        this.image = image;
        this.getEmotionalState().setIndex((index));
    }
    public Mood(String reason, String date, String socialSituation, String keyID, int index) {
        this.emotionalState.setEmotionalState(index);
        this.reason = reason;
        this.date = date;
        this.socialSituation = socialSituation;
        this.keyID = keyID;
        this.image = image;
        this.getEmotionalState().setIndex((index));
    }
    // Used in testing
    public Mood(EmotionalState emotionalState, String reason, String socialSituation, GeoPoint location ) {
        this.emotionalState = emotionalState;
        this.reason = reason;
        this.socialSituation = socialSituation;
        this.location = location;
        setDate();
    }

    /*
     * Setup notification from Firestore document.
     */
    public void setFromDocument(QueryDocumentSnapshot doc) {
        this.setGivenDate(doc.getString("Date"));
        this.emotionalState.setEmotionalState(((Number)doc.getData().get("Index")).intValue());
        this.setImage(doc.getString("Image"));
        this.setLocation(doc.getGeoPoint("Location"));
        this.setReason(doc.getString("Reason"));
        this.setSocialSituation(doc.getString("Social Situation"));
        this.setUID(doc.getString("UID"));
        this.setUsername(doc.getString("Username"));
    }

    /*
     * Set the username of the user who posted the mood
     */
    private void setUsername(String username) {
        this.username = username;
    }

    /*
     * Set the UID of the user who posted the mood
     */
    private void setUID(String UID) {
        this.UID = UID;
    }

    /*
     * This returns the username of the users post
     * @return Return the username as a string
     */
    public String getUsername() {
        return this.username;
    }

    /*
     * This gets the byte array that is the image.
     * @return Return the image as a byte array
     */
    public String getImage() {
        return image;
    }

    /*
     * This sets the byte array that is the image.
     */
    public void setImage(String image) {
        this.image = image;
    }

    /*
     * This returns the date the mood was created.
     * @return Return the reason
     */
    public String getDate() {
        return this.date;
    }

    /*
     * This instantaneously captures the date and time at runtime and converts to string
     */
    public void setDate() {
        Date instantananeousDate;
        instantananeousDate = new Date();
        date = instantananeousDate.toString();
    }

    /*
     * This instantaneously captures the date and time at runtime and converts to string
     */
    public void setGivenDate(String date) {
        this.date = date;
    }

    /*
     * This returns the Moods emotional state.
     *
     * @return Return the emotionalState
     */
    public EmotionalState getEmotionalState() {
        return this.emotionalState; // Convert to string?
    }

    /*
     * This sets the Moods emotional state.
     */
    public void setEmotionalState(EmotionalState emotionalState) {
        this.emotionalState = emotionalState;
    }

    /*
     * This returns the Moods reason.
     *
     * @return Return the reason
     */
    public String getReason() {
        return this.reason;
    }

    /*
     * This sets the Moods reason. Max size of 20 chars.
     *
     * @return Return true if reason is set and less than 20 chars. Else will return false.
     */
    public boolean setReason(String reason) {
        if (reason.length() < 20) {
            this.reason = reason;
            return true;
        }
        return false;
    }

    /*
     * This returns the Moods social situation.
     *
     * @return Return the socialSituation
     */
    public String getSocialSituation() {
        return this.socialSituation;
    }

    /*
     * This sets the Moods social situation.
     */
    public void setSocialSituation(String socialSituation) {
        this.socialSituation = socialSituation;
    }


    /*
     * This returns the Moods location.
     *
     * @return Return the location
     */
    public GeoPoint getLocation() {
        return this.location;
    }

    /*
     * This method sets the location of the mood.
     *
     * @return Return the location
     */
    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    /*
     *
     */
    public HashMap<String, Object> serializeMood() {
        this.emotionalIndex = this.getEmotionalState().getIndex();
        HashMap<String, Object> data = new HashMap<>();
        data.put("Emotional State", this.getEmotionalState().getName());
        data.put("Index", emotionalIndex);
        data.put("Reason", this.getReason());
        data.put("Social Situation", this.getSocialSituation());
        data.put("Location", this.getLocation());
        data.put("Date", this.getDate());
        data.put("Image", this.getImage());
        data.put("Username", this.getUsername());
        return data;
    }

    @Override
    public int compareTo(Mood mood) {
        return mood.getDate().compareTo(this.date);
    }

    public String getKeyID() {
        return keyID;
    }

}

