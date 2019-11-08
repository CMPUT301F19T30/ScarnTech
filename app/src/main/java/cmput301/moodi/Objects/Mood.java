package cmput301.moodi.Objects;
/*
 * Class: Mood
 * Version 1: Create a mood object along with its properties and getters & setters
 * 11/04/2019
 */

import java.util.Date;
import java.util.HashMap;

public class Mood implements Comparable<Mood>{
    private Date instantananeousDate;
    private String date;
    private EmotionalState emotionalState;

    private String reason = "";
    private Image reasonImage;
    private String socialSituation = "";
    private Location location;
    private String moodID = "";
    
    // Temporary variables for testing posting
    private String dummyEmotionalState;
    
    public String getDummyEmotionalState() {
        return dummyEmotionalState;
    }
    Mood() {setDate();}
    public Mood(String dummyEmotionalState, String reason) {
        this.dummyEmotionalState = dummyEmotionalState;
        this.reason = reason;
        setDate();
    }
    // End of test methods that need to be updated on completion of EmotionalState

    // Note once emotional state is done, the color and mood must be pulled from it! (Not an input)
    // Ex. this.reasonImage = emotionalState.getReasonImage() 
    // Where getReasonImage() is defined to return the image associated to the preset emotional state
    public Mood(EmotionalState emotionalState, String reason, Image reasonImage) {
        this.emotionalState = emotionalState;
        this.reason = reason;
        this.reasonImage = reasonImage;
        setDate();
    }

    public Mood(EmotionalState emotionalState, String reason, Image reasonImage, String socialSituation ) {
        this.emotionalState = emotionalState;
        this.reason = reason;
        this.reasonImage = reasonImage;
        this.socialSituation = socialSituation;
        setDate();
    }
  
    public Mood(EmotionalState emotionalState, String reason, Image reasonImage, String socialSituation, Location location ) {
        this.emotionalState = emotionalState;
        this.reason = reason;
        this.reasonImage = reasonImage;
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
     * This returns the Moods reason.
     *
     * @return Return the reason
     */
    public Image getReasonImage() {
        return this.reasonImage;
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
    public Location getLocation() {
        return this.location;
    }

    /*
     * This method sets the location of the mood.
     *
     * @return Return the location
     */
    public void setLocation(Location location) {
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
        HashMap<String, Object> data = new HashMap<>();
        data.put("Emotional State", this.getDummyEmotionalState());
        data.put("Reason", this.getReason());
        data.put("Social Situation", this.getSocialSituation());
        data.put("Latitude", this.getLocation().getLatitude());
        data.put("Longitude", this.getLocation().getLongitude());
        return data;
    }

    @Override
    public int compareTo(Mood mood) {
        return this.moodID.compareTo(mood.getID());
    }

}

