package cmput301.moodi.ui.loggedIn.post;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CalendarView;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;

import java.util.Calendar;

import cmput301.moodi.Objects.Mood;
import cmput301.moodi.Objects.MoodiStorage;
import cmput301.moodi.R;

/*
 * Class: PostFragment
 * Posting page where a user enters a "post" and on button press, sends it to the database
 * 11/09/2019
 */
public class PostFragment extends Fragment {

    // Variables used to detect user input and send a request!
    private String DateAndTime = ""; // Change this to the selection from drop down list
    private EditText ReasonView;

    private ImageButton PostMood;
    private ImageButton getCustomDateButton;
    private ImageButton getPictureButton;
    private ImageButton getLocationButton;

    private CalendarView calendar;
    private String inputDate;
    private Spinner inputHourSpinner;
    private Spinner inputMinuteSpinner;

    private Spinner EmotionalStateSpinner;
    private Spinner SocialSituationSpinner;

    // cheesy way to get "live" position values from the database
    private TextView lastLat;
    private TextView lastLon;

    // Variables that are used to connect and reference Firebase
    FirebaseFirestore db;
    String TAG = "PostFragment";
    MoodiStorage moodiStorage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // TODO: Add implementation of: EmotionalState Spinner, Date, Image & Location
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        // Pointing variables for detection of user input (Text, buttons, than spinners and calendar)
        ReasonView = view.findViewById(R.id.input_Reasoning);

        PostMood = view.findViewById(R.id.post_mood_button);
        getCustomDateButton = view.findViewById(R.id.add_date_button);
        getPictureButton = view.findViewById(R.id.add_image_button);
        getLocationButton = view.findViewById(R.id.add_location_button);

        EmotionalStateSpinner = view.findViewById(R.id.input_EmotionalState_Spinner);
        SocialSituationSpinner = view.findViewById(R.id.input_SocialSituation_Spinner);

        inputHourSpinner = view.findViewById(R.id.input_hour);
        inputMinuteSpinner = view.findViewById(R.id.input_minute);
        calendar = view.findViewById(R.id.input_calendarView);

        lastLat = view.findViewById(R.id.last_lat);
        lastLon = view.findViewById(R.id.last_lon);

        // Get the current time and update the spinners
        Calendar instantCalendar = Calendar.getInstance();
        final int hours = instantCalendar.get(instantCalendar.HOUR_OF_DAY);
        final int minutes = instantCalendar.get(instantCalendar.MINUTE);
        inputHourSpinner.setSelection(hours);
        inputMinuteSpinner.setSelection(minutes);

        // Getting updated date
        // TODO: ITS NOT UPDATING DATE
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month += 1;
                String newDate = year + "-" + month + "-" + dayOfMonth;
                inputDate = newDate;
            }
        });

        // Access a Cloud Firestore instance from your Activity
        moodiStorage = new MoodiStorage();

        // user selected to add a location to the mood, go to add moood activity
        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddLocationActivity.class);
                // pass old location to add location activity for aesthetic
                if (lastLat.getText() != null && lastLon.getText() != null) {
                    i.putExtra("Latitude", lastLat.getText().toString());
                    i.putExtra("Longitude", lastLon.getText().toString());
                }
                startActivity(i);
            }
        });

        // Get most recent user position data from firebase
        DocumentReference docRef = moodiStorage.getLastLocation();
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    GeoPoint lastLocation = (GeoPoint) snapshot.getData().get("Location");
                    lastLat.setText(String.valueOf(lastLocation.getLatitude()));
                    lastLon.setText(String.valueOf(lastLocation.getLongitude()));
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

        // Set onclick listener for creation of new post
        PostMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Pulling user input from fragment
                String reason = ReasonView.getText().toString();
                String socialSituation = SocialSituationSpinner.getSelectedItem().toString();
                int index = EmotionalStateSpinner.getSelectedItemPosition();

                // Getting the updated time
                String hour = inputHourSpinner.getSelectedItem().toString();
                String minute = inputMinuteSpinner.getSelectedItem().toString();
                String date = inputDate + " " + hour + ":" + minute;


                // Create a new mood from the user input
                Mood mood = new Mood(index, reason, socialSituation, date);

                // Set mood location to the most recent location data from firebase (through text view)
                if (lastLat.getText() != null && lastLon.getText() != null) {
                    mood.setLocation(new GeoPoint(Double.valueOf(lastLat.getText().toString()), Double.valueOf(lastLon.getText().toString())));
                } else {
                    Toast.makeText(getActivity(), "No location to add", Toast.LENGTH_SHORT).show();
                }

                // Send data to the database
                moodiStorage.addMoodPost(mood);

                // Reset posts to prepare for more entries!
                ReasonView.setText("");
                SocialSituationSpinner.setSelection(0);
                EmotionalStateSpinner.setSelection(0);
                inputHourSpinner.setSelection(hours);
                inputMinuteSpinner.setSelection(minutes);
            }
        });

        return view;
    }
}