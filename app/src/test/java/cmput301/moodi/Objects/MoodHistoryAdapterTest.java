package cmput301.moodi.Objects;

import android.content.Context;

import com.google.firebase.firestore.GeoPoint;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import cmput301.moodi.Objects.MoodHistoryAdapter;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class MoodHistoryAdapterTest {


    //ArrayList<Mood> testlist = list();
    //private MoodHistoryAdapter ;

    //private ArrayList<Mood> moodsarr;
    private ArrayList<Mood> moodsListFiltered;
    //private MoodHistoryAdapter.MoodFilter customFilter;
    private Context context;



    MoodHistoryAdapter mockadapter(){
        //ArrayList<Mood> moodsarr;
        GeoPoint location1 = new GeoPoint(53.18923, -113.12312);
        EmotionalState emotionalState1= new EmotionalState();
        emotionalState1.setName("Happy");
        Mood mood1 = new Mood(emotionalState1,"1","1",location1);
        GeoPoint location2 = new GeoPoint(53.18923, -113.12312);
        EmotionalState emotionalState2= new EmotionalState();
        emotionalState1.setName("Sad");
        Mood mood2 = new Mood(emotionalState2,"2","2",location2);
        ArrayList<Mood> moodsarr = new ArrayList<Mood>();
        moodsarr.add(mood1);
        moodsarr.add(mood2);

        MoodHistoryAdapter mymockadapter = new MoodHistoryAdapter(context,moodsarr);

        return mymockadapter;
    }

    @Test
    public void getCount() {
        MoodHistoryAdapter mymockadapter = mockadapter();
        assertEquals(2,mymockadapter.getCount());
    }

    @Test
    public void getItem() {
        MoodHistoryAdapter mymockadapter = mockadapter();
        GeoPoint location1 = new GeoPoint(53.18923, -113.12312);
        assertEquals(location1,mymockadapter.getItem(0).getLocation());
    }

    @Test
    public void getItemId() {
        MoodHistoryAdapter mymockadapter = mockadapter();
        GeoPoint location1 = new GeoPoint(53.18923, -113.12312);
        assertEquals(0,mymockadapter.getItemId(0));
        //doesn't make sense


    }

    @Test
    public void getFilter() {
        MoodHistoryAdapter mymockadapter = mockadapter();
        assertNotNull(mymockadapter.getFilter());


    }
}