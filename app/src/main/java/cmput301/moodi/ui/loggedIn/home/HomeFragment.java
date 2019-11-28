package cmput301.moodi.ui.loggedIn.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

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

    // MoodList objects
    private ListView moodList;
    private ArrayAdapter<Mood> moodAdapter;
    private ArrayList<Mood> moodDataList;

    // Firestore and reference objects
    private FirebaseFirestore db;
    private CollectionReference postReference;
    private MoodiStorage moodiStorage;
    private String TAG = "HomeFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Get a reference to the ListView and create an object for the mood list.
        moodList = view.findViewById(R.id.mood_list);
        moodDataList = new ArrayList<>();

        // Set the adapter for the listView to the CustomAdapter that we created in Lab 3.
        moodAdapter = new MoodListAdapter(container.getContext(), moodDataList);
        moodList.setAdapter(moodAdapter);

        // Init firestore link and reference object
        db = FirebaseFirestore.getInstance();
        postReference = db.collection("posts");
        moodiStorage = new MoodiStorage();

        // TODO: Try to put most of this guy in moodistorage for the firebase side + filtering by userid associated with each post
        // Now listening to all the changes in the database and get notified, note that offline support is enabled by default.
        postReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                // clear old mood list and re-create from updated firestore data
                moodDataList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                    // for each mood
                    String postID = doc.getId();
                    String reasonText = (String) doc.getData().get("Reason");
                    String date = (String) doc.getData().get("Date");
                    String socialSituation = (String) doc.getData().get("Social Situation");
                    Number index = (Number) doc.getData().get("Index");
                    String path = (String) doc.getData().get("Image");
                    String uniqueID = (String) doc.getData().get("Username");

                    if (index != null) {
                        int i = index.intValue();
                        // TODO: Implement image and serialize as a list

                        //  moodDataList.add(new Mood(reasonText, date, socialSituation, postID, i, image));
//                        moodDataList.add(new Mood(reasonText, date, socialSituation, postID, i, path));
                        moodDataList.add(new Mood(reasonText, date, socialSituation, postID, i, path, uniqueID));
                    }

                    // TODO: Change it to receive the FULL mood + move firebase code to MoodiStorage
                    // Mood mood = new Mood();
//                    moodDataList.add(new Mood(emotionalStateText, reasonText, date, postID, i)); // Adding the posts from firestore
                }
                Collections.sort(moodDataList);
                moodAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud.
            }
        });

        // TODO: Make this pull up an edit screen / pop-up fragment where they can delete from there
        // TODO: also find a way to implement this in MoodiStorage
        // Adding an onItemLongClickListener to the list for deletion and removes post from both app and database sides.
        moodList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Mood moodSelected = moodDataList.get(i);
                new ViewFragment();
                ViewFragment.viewSelection(moodSelected).show(getChildFragmentManager(),"View Selected Post");
                return false;
            }
        });
        return view;
    }
}