package cmput301.moodi;
/*
 * Class: MoodsActivity
 * Version 1: Shows a list of user moods (posts) and calls "Post" fragment to make a post
 * 11/04/2019
 */
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MoodsActivity extends AppCompatActivity implements PostMoodFragment.OnFragmentInteractionListener{

    // Variables that are used to build the list of moods (User Posts)
    ListView moodList;
    ArrayAdapter<Mood> moodAdapter;
    ArrayList<Mood> moodDataList;

    // Variables that are used to connect and reference Firebase
    FirebaseFirestore db;
    String TAG = "Sample";

    // Variable used to detect a request to post!
    Button PostMood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moods);

        // Get a reference to the ListView and create an object for the mood list.
        moodList = findViewById(R.id.mood_list);
        moodDataList = new ArrayList<>();

        // Set the adapter for the listView to the CustomAdapter that we created in Lab 3.
        moodAdapter = new CustomList(MoodsActivity.this, moodDataList);
        moodList.setAdapter(moodAdapter);

        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();

        // Get a top-level reference to the collection.
        final CollectionReference collectionReference = db.collection("users");

        // Setting OnClickListeners and responses for buttons
        configurePostMoodButton();

        // Adding an onItemLongClickListener to the list for deletion.
        moodList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Mood clickedMood = moodDataList.get(i);
                moodAdapter.remove(clickedMood);
                return false;
            }
        });
    }

    private void configurePostMoodButton() {

        // Pointing variable to post button (Pulls up fragment)
        PostMood = findViewById(R.id.post_mood_button);

        // Set onclick listener for creation of new account
        PostMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PostMoodFragment().show(getSupportFragmentManager(), "ADD_POST");
            }
        });
    }

    // From fragment, receives user input and posts mood
    @Override
    public void addNewPost(Mood mood) {
        moodAdapter.add(mood);
    }
}
