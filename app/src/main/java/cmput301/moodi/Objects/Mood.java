package cmput301.moodi.Objects;
/*
 * Class: Mood
 * Version 1: Create a mood object along with its properties and getters & setters
 * 11/04/2019
 */

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;
import java.util.HashMap;

import cmput301.moodi.ui.LoggedIn.post.DateFragment;

public class Mood implements Comparable<Mood> {

    private String date;
    private EmotionalState emotionalState = new EmotionalState();

    private String reason = "";
    private String socialSituation = "";
    private GeoPoint location;
    private String moodID = "";
    private Number emotionalIndex = 0;

    // Unique ID of a post assigned by FireBase
    private String keyID;


    /*
    * Constructor for creation of a mood from a post which is sent to FireBase
    * (Used in case of no custom date entry)
     */
    public Mood(int index, String reason, String SocialSituation) {
        this.emotionalState.setEmotionalState(index);
        this.reason = reason;
        this.socialSituation = SocialSituation;
        setDate();
    }

    /*
     * Constructor for creation of a mood from a post which is sent to FireBase
     * (Used in case of a custom date entry)
     */
    public Mood(int index, String reason, String SocialSituation, String date) {
        this.emotionalState.setEmotionalState(index);
        this.reason = reason;
        this.socialSituation = SocialSituation;
        this.date = date;
    }

    // TODO: Clean up constructors and make sure it doesnt make a new date when pulling a post from database
    /*
    * Constructor for receiving a post from the FireBase and constructing a new mood
     */
    public Mood(String emotionalState, String reason, String date, String socialSituation, String keyID, int index) {
        this.emotionalState.setEmotionalState(index);
        this.emotionalState.setName(emotionalState);
        this.reason = reason;
        this.date = date;
        this.socialSituation = socialSituation;
        this.keyID = keyID;
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
     * This returns the date the mood was created.
     *
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
     * This returns the id of the mood.
     */
    public String getID() {
        return this.moodID;
    }

    /*
     *
     */
    public HashMap<String, Object> serializeMood() {
        this.emotionalIndex = this.getEmotionalState().getIndex();
        HashMap<String, Object> data = new HashMap<>();
        data.put("Emotional State", this.getEmotionalState().getName());
        data.put("Index", this.getEmotionalState().getIndex());
        data.put("Reason", this.getReason());
        data.put("Social Situation", this.getSocialSituation());
        data.put("Location", this.getLocation());
        data.put("Date", this.getDate());
        return data;
    }

    @Override
    public int compareTo(Mood mood) {
        return this.moodID.compareTo(mood.getID());
    }

    public String getKeyID() {
        return keyID;
    }

}

