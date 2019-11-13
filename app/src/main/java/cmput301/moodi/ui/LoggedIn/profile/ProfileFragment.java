package cmput301.moodi.ui.LoggedIn.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import cmput301.moodi.Objects.Mood;
import cmput301.moodi.Objects.MoodListAdapter;
import cmput301.moodi.Objects.MoodiStorage;
import cmput301.moodi.Objects.User;
import cmput301.moodi.R;

/*
 * Class: ProfileFragment
 *
 * Display profile username and name. Allow user to edit username and name. Display users personal mood history.
 */

public class ProfileFragment extends Fragment {

    private cmput301.moodi.ui.LoggedIn.profile.ProfileViewModel profileViewModel;
    private MoodiStorage moodiStorage;
    private static final String TAG = "ProfileActivity";
    private User userProfile;
    private ImageButton logout;
    private TextView username, nameDisplay;

    // Variables that are used to build the list of moods (User Posts)
    ListView moodList;
    ArrayAdapter<Mood> moodAdapter;
    ArrayList<Mood> moodDataList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        profileViewModel = ViewModelProviders.of(this).get(cmput301.moodi.ui.LoggedIn.profile.ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        moodiStorage = new MoodiStorage();
        userProfile = new User();
        username = root.findViewById(R.id.username);
        nameDisplay = root.findViewById(R.id.full_name);

        moodList = root.findViewById(R.id.mood_history);
        moodDataList = new ArrayList<>();
        moodAdapter = new MoodListAdapter(container.getContext(), moodDataList);
        moodList.setAdapter(moodAdapter);

        this.loadUserPreferences();
        this.loadMoodHistory();

        profileViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });


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
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
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
     * Query for users mood history
     */
    private void loadMoodHistory() {
        moodiStorage.getUMoodHistory().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    moodDataList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String postID = document.getId();
                        String emotionalStateText = (String) document.getData().get("Emotional State");
                        String reasonText = (String) document.getData().get("Reason");
                        String date = (String) document.getData().get("Date");

                        moodDataList.add(new Mood(emotionalStateText, reasonText, date, postID));
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }

                moodAdapter.notifyDataSetChanged();
            }
        });

    }
}