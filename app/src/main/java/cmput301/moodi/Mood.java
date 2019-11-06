package cmput301.moodi;
/*
 * Class: Mood
 * Version 1: Create a mood object along with its properties and getters & setters
 * 11/04/2019
 */

import java.util.Date;

public class Mood implements Comparable<Mood>{
    private Date createdDate = new Date();
    private String createdTime;
    private EmotionalState emotionalState;
    private String reason;
    private Image reasonImage;
    private String socialSituation;
    private Location location;
    private String moodID;

    public Mood(EmotionalState emotionalState, String reason, Image reasonImage) {
        this.emotionalState = emotionalState;
        this.reason = reason;
        this.reasonImage = reasonImage;
    }

    public Mood(EmotionalState emotionalState, String reason, Image reasonImage, String socialSituation ) {
        this.emotionalState = emotionalState;
        this.reason = reason;
        this.reasonImage = reasonImage;
        this.socialSituation = socialSituation;
    }

    public Mood(EmotionalState emotionalState, String reason, Image reasonImage, String socialSituation, Location location ) {
        this.emotionalState = emotionalState;
        this.reason = reason;
        this.reasonImage = reasonImage;
        this.socialSituation = socialSituation;
        this.location = location;
    }


    /*
     * This returns the date the mood was created.
     *
     * @return Return the reason
     */
    public String getDate() {
        return this.createdDate.toString();
    }

    /*
     * This returns the time the mood was created.
     *
     * @return Return the reason
     */
    public String getTime() {
        return this.createdTime;
    }

    /*
     * This returns the Moods emotional state.
     *
     * @return Return the emotionalState
     */
    public EmotionalState getEmotionalState() {
        return this.emotionalState;
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
     * This returns the id of the mood.
     */
    public String getID() {
        return this.moodID;
    }


    @Override
    public int compareTo(Mood mood) {
        return this.moodID.compareTo(mood.getID());
    }

}

