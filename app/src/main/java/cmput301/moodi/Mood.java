package cmput301.moodi;
/*
 * Class: Mood
 * Version 1: Create a mood object along with its properties and getters & setters
 * 11/04/2019
 */

import java.util.Date;

public class Mood implements Comparable<Mood>{

    // Add location and image variables once there classes have been worked out
    private EmotionalState emotionalState;
    private String dummyEmotionalState; // Replace once list of emotions is made!

    private String reason; // (OPTIONAL INPUT)
    private String socialSituiation; // (OPTIONAL INPUT) to be implemented later
    private Date theDate = new Date(); // NOT SURE IF CORRECTLY IMPLEMENTED
    private String date;

    public String getdummyEmotionalState() {
        return dummyEmotionalState;
    }

    public void setdummyEmotionalState(String dummyEmotionalState) {
        this.dummyEmotionalState = dummyEmotionalState;
    }

    // Returns a string with wrong time zone I believe!
    public String getDate() {
        return this.date; // Returns date and time of when setDate was called
    }
    public void setDate() {
        date = theDate.toString(); // Gets date at time of call
    }

    public String getReason() {
        return reason;
    }

    public String getSocialSituiation() {
        return socialSituiation;
    }

    public EmotionalState getEmotionalState() {
        return emotionalState;
    }

    public void setEmotionalState(EmotionalState emotionalState) {
        this.emotionalState = emotionalState;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setSocialSituiation(String socialSituiation) {
        this.socialSituiation = socialSituiation;
    }

    // Constructor that creates a Mood! (Temporary, needs to be improved once emotional state is created)
    Mood(String EmotionalState, String Reason) {
        this.dummyEmotionalState = EmotionalState;
        this.reason = Reason;
        setDate();
    }

    // Creates a Mood! (Temporary, needs to be improved once emotional state is created)
    // For the case where no reason is entered
    Mood(String EmotionalState) {
        this.dummyEmotionalState = EmotionalState;
        setDate();
    }
    Mood() {
        setDate();
    }

    @Override
    public int compareTo(Mood mood) {
        return this.dummyEmotionalState.compareTo(getdummyEmotionalState());
    }
}

