package cmput301.moodi.ui.loggedIn.home;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import cmput301.moodi.Objects.Mood;
import cmput301.moodi.Objects.MoodHistoryAdapter;
import cmput301.moodi.Objects.MoodiNotificationsAdapter;
import cmput301.moodi.Objects.NotificationList;
import cmput301.moodi.Objects.User;
import cmput301.moodi.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cmput301.moodi.Objects.Mood;
import cmput301.moodi.Objects.MoodiStorage;
import cmput301.moodi.R;
import cmput301.moodi.ui.loggedIn.profile.EditFragment;

/*
 * Class: FollowingProfileFragment
 * Fragment pulled up a followed users profile
 * 11/27/2019
 */
public class FollowingProfileFragment extends DialogFragment {

    FirebaseFirestore db;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    private MoodiStorage moodiStorage;
    private static final String TAG = "SelectedUserProfileActivity";
    private TextView username, nameDisplay; //,intermediate;
    private User userProfile;

    // Moods
    private ListView moodList;
    private MoodHistoryAdapter moodAdapter;
    private ArrayList<Mood> moodDataList;

    // Used to pass data to main activity
    private viewUser listener;

    public List<Mood> moods;
    public String uniqueID;
    private String userName;

    // Takes a selected moods properties to be able to reconstruct on view
    public static FollowingProfileFragment viewProfile(String Username) {
        Bundle args = new Bundle();
        args.putSerializable("Username", Username);
        FollowingProfileFragment fragment = new FollowingProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface viewUser {
        void viewTheUser();
    }

    // Fragment lifecycle method
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof viewUser) {
            listener = (viewUser) context;
            if (this.getDialog() != null)
                this.getDialog().dismiss();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    // Creates Fragement, pulls data from user entry and passes the custom date to PostFragment
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_view_user, null);

        db = FirebaseFirestore.getInstance();
        moodiStorage = new MoodiStorage();
        userProfile = new User();

        // Point Variables to header & list
        nameDisplay = view.findViewById(R.id.viewed_full_name);
        username = view.findViewById(R.id.viewed_username);
        moodList = view.findViewById(R.id.viewed_mood_history);

        // Load lists for moods.
        moodDataList = new ArrayList<Mood>();
        moodAdapter = new MoodHistoryAdapter(this.getContext(), moodDataList);
        moodList.setAdapter(moodAdapter);

        // Retrieve properties of selected mood from bundle and populate display
        Bundle args = getArguments();
        if (args != null) {
            userName = args.getString("Username");
            username.setText(userName);

            final Task task = moodiStorage.searchByUsername(userName)
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    String fullName = (String) document.getData().get("first_name") + " " + (String) document.getData().get("last_name");
                                    nameDisplay.setText(fullName);
                                    // Getting user's key ID to get there most recent post
                                    uniqueID = document.getId();
                                }
                            }
                        }
                    });

            task.addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    moodiStorage.getUserMoodHistory(uniqueID).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                if (moods == null) {
                                    moods = new ArrayList<>();
                                } else {
                                    moods.clear(); }

                                for (QueryDocumentSnapshot mood_doc : task.getResult()) {

                                    Mood mood = new Mood();
                                    mood.setFromDocument(mood_doc);
                                    moods.add(mood); }

                                if (!moods.isEmpty()) {
                                    Collections.sort(moods);
                                    moodDataList.add(moods.get(0)); // only the most recent from each following
                                    moodAdapter.notifyDataSetChanged(); }
                            }
                        }

                    });
                }
            });
        }

        //Build the fragment and set the buttons to call the delete or edit method in main
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setNegativeButton("Exit", null)
                .create();
    }
}


