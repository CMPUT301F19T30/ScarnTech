package cmput301.moodi.Objects;
/*
 * Class: MoodList
 * Version 1: Creating and maintaining a list of user posts (Moods)
 * that will be used to fill said users database (NOT AN ACTIVITY)
 * 11/04/2019
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoodList {

    private List<Mood> moods = new ArrayList<>();

    /**
     * Create method to add an existing city to our list for testing
     * @param emotionalState Mood needed to be added
     */
    public void add(Mood emotionalState){
        if (moods.contains(emotionalState))
            throw new IllegalThreadStateException();
        moods.add(emotionalState);
    }

    /**
     * Create method to delete an existing mood or send exception if mood not in list
     * @param mood Mood needed to be removed if existing in list already
     */
    public void delete(Mood mood) {
        if (!moods.contains(mood))
            throw new IllegalThreadStateException();
        moods.remove(mood);
    }

    /**
     * This method returns a list of moods
     * @return list
     */
    public List<Mood> moods() {
        List<Mood> list = moods;
        Collections.sort(list);
        return list;
    }

    /*
     * Clear list.
     */
    public void clear(){
        this.moods.clear();
    }
    
    /*
     * Sort mood objectss by reverse chronological order.
     */
    public List<Mood> sortReverseChronological(){
        Collections.sort(this.moods);
        return this.moods;
    }

    public Mood getLast() {
        return sortReverseChronological().get(0);
    }
}

