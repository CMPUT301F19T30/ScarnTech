package cmput301.moodi.ui.loggedIn.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cmput301.moodi.Objects.Mood;
import cmput301.moodi.Objects.MoodListAdapter;
import cmput301.moodi.Objects.MoodiStorage;
import cmput301.moodi.R;
/**
 * Class: HomeFragment
 * Main page where a user may view there own personal posts or see "followers" posts
 * @since 11/09/2019
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

    public List<Mood> moods;

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

        // wait for posts to be updated, then re run queries for the moods of users being followed
        postReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                // first clear the old list of moods
                moodDataList.clear();

                // then query the current list of followers
                moodiStorage.getFollowing().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot following_doc : task.getResult()) {

                                // for each following get most recent mood
                                moodiStorage.getUserMoods((String) following_doc.getData().get("following")).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (moods == null) {
                                                moods = new ArrayList<>();
                                            } else {
                                                moods.clear();
                                            }

                                            for (QueryDocumentSnapshot mood_doc : task.getResult()) {
                                                Mood mood = new Mood();
                                                mood.setFromDocument(mood_doc);
                                                moods.add(mood);
                                            }

                                            if (!moods.isEmpty()) {
                                                Collections.sort(moods);
                                                moodDataList.add(moods.get(0)); // only the most recent from each following
                                                moodAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });

        // View the post by clicking it
        moodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Mood moodSelected = moodDataList.get(i);
                new ViewFragment();
                ViewFragment.viewSelection(moodSelected).show(getChildFragmentManager(),"View Selected Post");
                return;
            }
        });
        return view;
    }
}