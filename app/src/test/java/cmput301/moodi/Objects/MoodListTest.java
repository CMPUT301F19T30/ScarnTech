package cmput301.moodi.Objects;

import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Class: MoodListTest
 * This class test Moodlist class and functions in it.
 * @since 27/11/2019
 */

public class MoodListTest {
    /**
     * mockMoodList create a temp list to be test. In this case we add a moods into
     * the list.
     */
    private MoodList mockMoodList(){
        MoodList mylist = new MoodList();
        EmotionalState emo = new EmotionalState();
        GeoPoint mockLocation = new GeoPoint(53.123, 73.123);
        Mood emotionalState = new Mood(emo,"123","456",mockLocation);
        mylist.add(emotionalState);
        return mylist;
    }
    /*
    add another mood into the list and exam the number of item in the list.
     */
    @Test
    public void add() {
        MoodList mylist = mockMoodList();
        EmotionalState emo = new EmotionalState();
        GeoPoint mockLocation = new GeoPoint(53.123, 73.123);
        Mood emotionalState2 = new Mood(emo,"123","456",mockLocation);
        mylist.add(emotionalState2);

        assertEquals(2,mylist.moods().size());
    }
    /*
    Delete a mood in the list and exam the number of item in the list.
     */
    @Test
    public void delete() {
        MoodList mylist = mockMoodList();
        mylist.delete(mylist.moods().get(0));
        assertEquals(0,mylist.moods().size());

    }
    /*
    Delete a specific mood using moods function.
     */
    @Test
    public void moods() {
        MoodList mylist = mockMoodList();
        mylist.delete(mylist.moods().get(0));
        assertEquals(0,mylist.moods().size());
    }
    /*
    clean moods in the list and exam the number of item in the list.
     */
    @Test
    public void clear() {
        MoodList mylist = mockMoodList();
        mylist.clear();
        assertEquals(0,mylist.moods().size());
    }
    /*
    sort moods in the list and exam the number of item in the list.
     */
    @Test
    public void sortReverseChronological() {
        MoodList mylist = mockMoodList();
        mylist.sortReverseChronological();
        assertNotNull(mylist.moods());
    }
}