package cmput301.moodi;
/*
 * Class: MoodList
 * Version 1: Creating and maintaining a list of user posts (Moods)
 * that will be used to fill said users database
 * 11/04/2019
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is a class that keeps track of a list of city objects
 */
public class MoodList{
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
     * This method returns a list of moods
     * @return
     */
    public List<Mood> moods() {
        List<Mood> list = moods;
        Collections.sort(list);
        return list;
    }

}

