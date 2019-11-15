package cmput301.moodi.ui.loggedIn.profile;

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
import cmput301.moodi.Objects.MoodiNotificationsAdapter;
import cmput301.moodi.Objects.MoodiStorage;
import cmput301.moodi.Objects.NotificationList;
import cmput301.moodi.Objects.User;
import cmput301.moodi.R;

/*
 * Class: ProfileFragment
 *
 * Display profile username and name. Allow user to edit username and name. Display users personal mood history.
 */

public class ProfileFragment extends Fragment {

    private cmput301.moodi.ui.loggedIn.profile.ProfileViewModel profileViewModel;
    private MoodiStorage moodiStorage;
    private static final String TAG = "ProfileActivity";
    private User userProfile;
    private ImageButton logout;
    private TextView username, nameDisplay;

    // Moods
    private ListView moodListView;
    private ArrayAdapter<Mood> moodAdapter;
    private ArrayList<Mood> moodList;

    // Notifications
    private ListView notificationListView;
    private MoodiNotificationsAdapter notificationAdapter;
    private NotificationList notificationList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        profileViewModel = ViewModelProviders.of(this).get(cmput301.moodi.ui.loggedIn.profile.ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        moodiStorage = new MoodiStorage();
        userProfile = new User();

        // Views from layout
        username = root.findViewById(R.id.username);
        nameDisplay = root.findViewById(R.id.full_name);
        notificationListView = root.findViewById(R.id.notification_list);
        moodListView = root.findViewById(R.id.mood_history);

        // Load lists for moods.
        moodList = new ArrayList<Mood>();
        moodAdapter = new MoodListAdapter(container.getContext(), moodList);
        moodListView.setAdapter(moodAdapter);

        // Load lists for notifications.
        notificationList = new NotificationList();
        notificationAdapter = new MoodiNotificationsAdapter(container.getContext(), notificationList);
        notificationListView.setAdapter(notificationAdapter);


        // Load views for profile.
        this.loadUserPreferences();
        this.loadMoodHistory();
        this.loadNotifications();

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
     * Query for users mood history.
     */
    private void loadMoodHistory() {
        moodiStorage.getUMoodHistory().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    moodList.clear();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        // Easy enough to pull more information from database!
                        String postID = doc.getId();
                        String emotionalStateText = (String) doc.getData().get("Emotional State");
                        String reasonText = (String) doc.getData().get("Reason");
                        String date = (String) doc.getData().get("Date");
                        String socialSituation = (String) doc.getData().get("Social Situation");
                        Number index = (Number) doc.getData().get("Index");

                        if (index != null) {
                            int i = index.intValue();
                            moodList.add(new Mood(emotionalStateText, reasonText, date, socialSituation , postID, i));
                            Log.d(TAG, socialSituation);
                        }
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
                moodList.sort();
                moodAdapter.notifyDataSetChanged();
            }
        });

    }


    /*
     * Load the notification view.
     */
    private void loadNotifications() {
        moodiStorage.getNotifications().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    notificationList.clear();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        //Todo: serialize notification.
                        /*
                        if (index != null) {
                            int i = index.intValue();
                            moodList.add(new Mood(emotionalStateText, reasonText, date, socialSituation, postID, i));
                            Log.d(TAG, socialSituation);
                        }

                         */
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }

                moodAdapter.notifyDataSetChanged();
            }
        });
    }
}