package cmput301.moodi.Ui.LoggedIn.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import cmput301.moodi.Objects.Location;
import cmput301.moodi.Objects.Mood;
import cmput301.moodi.Objects.MoodiStorage;
import cmput301.moodi.R;
import cmput301.moodi.ui.LoggedIn.BottomNavigationActivity;

/*
 * Class: PostFragment
 * Posting page where a user enters a "post" and on button press, sends it to the database
 * 11/09/2019
 */
public class PostFragment extends Fragment {

    // Variables used to detect user input and send a request!
    private EditText EmotionalStateView; // Change this to the selection from drop down list
    private EditText ReasonView;
    ImageButton PostMood;

    // Variables that are used to connect and reference Firebase
    FirebaseFirestore db;
    String TAG = "PostFragment";
    MoodiStorage moodiStorage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post, container, false);

        // Pointing variables for detection of user input
        EmotionalStateView = view.findViewById(R.id.input_EmotionalState);
        ReasonView = view.findViewById(R.id.input_Reasoning);
        PostMood = view.findViewById(R.id.post_mood_button);

        // Access a Cloud Firestore instance from your Activity
        moodiStorage = new MoodiStorage();

        // Set onclick listener for creation of new account
        PostMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Pulling user input from fragment
                String emotionalStateText = EmotionalStateView.getText().toString();
                String reasonText = ReasonView.getText().toString();

                // Create a mood object which can than be posted
                Mood mood = new Mood(emotionalStateText, reasonText);

                // get most recent user position data
                ((BottomNavigationActivity)getActivity()).updateUserLocation();
                GeoPoint lastLocation = ((BottomNavigationActivity)getActivity()).getLastLocation();

                // set mood location to the retrieved position data
                mood.setLocation(lastLocation);

                // TODO: Add implementation of: EmotionalState Spinner, Date, Image & Location

                // Send data to the database
                moodiStorage.addMoodPost(mood);

                // Clear text to prepare for more posts!
                EmotionalStateView.setText("");
                ReasonView.setText("");
            }
        });

        return view;
    }
}