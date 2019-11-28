package cmput301.moodi.ui.loggedIn.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

import cmput301.moodi.Objects.Mood;
import cmput301.moodi.Objects.MoodHistoryAdapter;
import cmput301.moodi.Objects.MoodiStorage;
import cmput301.moodi.Objects.User;
import cmput301.moodi.R;

/**
 * Class: ProfileFragment
 * Display profile username and name. Allow user to edit username and name. Display users personal mood history.
 * @since 11/15/2019
 */

public class ProfileFragment extends Fragment {

    private cmput301.moodi.ui.loggedIn.profile.ProfileViewModel profileViewModel;
    private MoodiStorage moodiStorage;
    private static final String TAG = "ProfileActivity";
    private User userProfile;
    private TextView username, nameDisplay;

    // Moods
    private ListView moodList;
    private MoodHistoryAdapter moodAdapter;
    private ArrayList<Mood> moodDataList;
    //private MoodList moodList; //Todo: use MoodList type in the future.

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        profileViewModel = ViewModelProviders.of(this).get(cmput301.moodi.ui.loggedIn.profile.ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        moodiStorage = new MoodiStorage();
        userProfile = new User();

        // Views from layout
        username = root.findViewById(R.id.username);
        nameDisplay = root.findViewById(R.id.full_name);

        // Load lists for moods.
        moodList = root.findViewById(R.id.mood_history);
        moodDataList = new ArrayList<>();
        moodAdapter = new MoodHistoryAdapter(container.getContext(), moodDataList);
        moodList.setAdapter(moodAdapter);


        // Load spinner resources
        Spinner spinner = (Spinner) root.findViewById(R.id.filter_by);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.mood_history_filters, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String emotionalState = adapterView.getItemAtPosition(position).toString();
                moodAdapter.getFilter().filter(emotionalState);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });


        profileViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        // Adding an onItemLongClickListener to view more information, edit or delete
        moodList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Mood moodSelected = moodDataList.get(i);
//                new cmput301.moodi.ui.loggedIn.profile.EditFragment();
                cmput301.moodi.ui.loggedIn.profile.EditFragment.editSelection(moodSelected).show(getChildFragmentManager(), "Edit_Moods");
                return false;
            }
        });

        // TODO: close fragment (Taking two clicks bug)

        // Load views for profile.
        this.loadUserPreferences();
        this.loadMoodHistory();
        this.loadSocialInfo();
        checkForUpdates();

        return root;
    }

    /*
     * Query for user preferences and profile data.
     */
    private void loadUserPreferences() {
        final DocumentReference userProfile = moodiStorage.getUserProfile();
        userProfile.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String fullName = document.getString("first_name") + " " + document.getString("last_name");
                        username.setText(document.getString("username"));
                        nameDisplay.setText(fullName);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    /*
     * Query for users mood history.
     */
    private void loadMoodHistory() {
        moodiStorage.getMyMoodHistory().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    moodDataList.clear();
                    for (QueryDocumentSnapshot doc : task.getResult()) {

                        String postID = doc.getId();
                        String reasonText = (String) doc.getData().get("Reason");
                        String date = (String) doc.getData().get("Date");
                        String socialSituation = (String) doc.getData().get("Social Situation");
                        Number index = (Number) doc.getData().get("Index");
                        String path = (String) doc.getData().get("Image");
                        String uniqueID = (String) doc.getData().get("Username");
                        if (index != null) {
                            int i = index.intValue();
                            // TODO: Add location to constructor
                            moodDataList.add(new Mood(reasonText, date, socialSituation, postID, i, path, uniqueID));
                        }
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }

                //moodList.sortReverseChronological(); //Todo: implement MoodList sorting
                Collections.sort(moodDataList);

                moodAdapter.notifyDataSetChanged();
            }
        });

    }

    /*
     * Query for updates users mood history
     */
    private void checkForUpdates() {

        // Variable used to reference database
        FirebaseFirestore db;

        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();

        // Get a top-level reference to the collection.
        final CollectionReference collectionReference = db.collection("posts");

        collectionReference.whereEqualTo("UID", moodiStorage.getUID()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                // clear the old list
                moodDataList.clear();

                // Point at database, receive any changes and append them to our list of posts
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                    // Log for debugging and reference
                    Log.d(TAG, String.valueOf(doc.getData().get("Emotional State")));

                    String postID = doc.getId();
                    String reasonText = (String) doc.getData().get("Reason");
                    String date = (String) doc.getData().get("Date");
                    String socialSituation = (String) doc.getData().get("Social Situation");
                    Number index = (Number) doc.getData().get("Index");
                    String path = (String) doc.getData().get("Image");
                    String uniqueID = (String) doc.getData().get("Username");

                    if (index != null) {
                        int i = index.intValue();
                        // TODO: Add Location to constructor
                        moodDataList.add(new Mood(reasonText, date, socialSituation, postID, i, path, uniqueID));
                    }
                }
                moodAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud.
                }
            });
    }


    /*
     * Check user click on notifications.
     *
     */
    private void getNotificationResponse() {
        //Todo: get id of clicked element.

    }

    /*
     * Load following and followers list.
     */
    private void loadSocialInfo() {
        moodiStorage.getFollowers().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Getting followers... ");
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Log.d(TAG, "user -> " + doc.getString("user"));
                        Log.d(TAG, "following -> " + doc.getString("following"));
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });


        moodiStorage.getFollowing().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Getting following... ");
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Log.d(TAG, "user -> " + doc.getString("user"));
                        Log.d(TAG, "following -> " + doc.getString("following"));
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
}