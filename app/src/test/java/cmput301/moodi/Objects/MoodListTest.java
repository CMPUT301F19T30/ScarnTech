package cmput301.moodi.Objects;

import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;

import static org.junit.Assert.*;

public class MoodListTest {

    private MoodList mockMoodList(){
        MoodList mylist = new MoodList();
        EmotionalState emo = new EmotionalState();
        GeoPoint mockLocation = new GeoPoint(53.123, 73.123);
        Mood emotionalState = new Mood(emo,"123","456",mockLocation);
        mylist.add(emotionalState);
        return mylist;
    }

    @Test
    public void add() {
        MoodList mylist = mockMoodList();
        EmotionalState emo = new EmotionalState();
        GeoPoint mockLocation = new GeoPoint(53.123, 73.123);
        Mood emotionalState2 = new Mood(emo,"123","456",mockLocation);
        mylist.add(emotionalState2);

        assertEquals(2,mylist.moods().size());
    }

    @Test
    public void delete() {
        MoodList mylist = mockMoodList();
        mylist.delete(mylist.moods().get(0));
        assertEquals(0,mylist.moods().size());

    }

    @Test
    public void moods() {
        MoodList mylist = mockMoodList();
        mylist.delete(mylist.moods().get(0));
        assertEquals(0,mylist.moods().size());
    }

    @Test
    public void clear() {
        MoodList mylist = mockMoodList();
        mylist.clear();
        assertEquals(0,mylist.moods().size());
    }

    @Test
    public void sortReverseChronological() {
        MoodList mylist = mockMoodList();
        mylist.sortReverseChronological();
        assertNotNull(mylist.moods());
    }
}