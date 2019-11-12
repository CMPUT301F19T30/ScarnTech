package cmput301.moodi.Ui.LoggedIn.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import cmput301.moodi.Objects.Mood;
import cmput301.moodi.Objects.MoodListAdapter;
import cmput301.moodi.Objects.MoodiStorage;
import cmput301.moodi.R;
/*
 * Class: HomeFragment
 * Main page where a user may view there own personal posts or see "followers" posts
 * 11/09/2019
 */
public class HomeFragment extends Fragment {

    // Variables that are used to build the list of moods (User Posts)
    ListView moodList;
    ArrayAdapter<Mood> moodAdapter;
    ArrayList<Mood> moodDataList;

    // Variables that are used to connect and reference Firebase
    FirebaseFirestore db;
    String TAG = "Sample";
    MoodiStorage moodiStorage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Get a reference to the ListView and create an object for the mood list.
        moodList = root.findViewById(R.id.mood_list);
        moodDataList = new ArrayList<>();

        // Set the adapter for the listView to the CustomAdapter that we created in Lab 3.
        moodAdapter = new MoodListAdapter(container.getContext(), moodDataList);
        moodList.setAdapter(moodAdapter);

        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
        this.moodiStorage = new MoodiStorage();

        // Get a top-level reference to the collection.
        final CollectionReference collectionReference = db.collection("posts");

        // TODO: Try to put most of this guy in moodistorage for the firebase side + filtering by userid associated with each post
        // Now listening to all the changes in the database and get notified, note that offline support is enabled by default.
        // Note: The data stored in Firestore is sorted alphabetically and per their ASCII values. Therefore, adding a new city will not be appended to the list.
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                // clear the old list
                moodDataList.clear();

                // Point at database, receive any changes and append them to our list of posts
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots){

                    // Log for debugging and reference
                    Log.d(TAG, String.valueOf(doc.getData().get("Emotional State")));

                    // Easy enough to pull more information from database!
                    String postID = doc.getId();
                    String emotionalStateText = (String) doc.getData().get("Emotional State");
                    String reasonText = (String) doc.getData().get("Reason");
                    String date = (String) doc.getData().get("Date");

                    // TODO: Change it to receive the FULL mood + move firebase code to MoodiStorage
                    // Mood mood = new Mood();
                    moodDataList.add(new Mood(emotionalStateText, reasonText, date, postID)); // Adding the cities and provinces from FireStore.
                }
                moodAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud.
            }
        });

        // TODO: Make this pull up an edit screen / pop-up fragment where they can delete from there
        // TODO: also find a way to implement this in MoodiStorage
        // Adding an onItemLongClickListener to the list for deletion and removes post from both app and database sides.
        moodList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                db.collection("posts")
                        .document(moodAdapter.getItem(i).getKeyID())
                        .delete();
                return false;
            }
        });

        return root;
    }
}