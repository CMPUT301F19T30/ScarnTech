package cmput301.moodi.ui.LoggedIn.post;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
    private String DateAndTime = ""; // Change this to the selection from drop down list
    private EditText ReasonView;

    private ImageButton PostMood;
    private ImageButton getCustomDateButton;
    private ImageButton getPictureButton;
    private ImageButton getCustomLocationButton;

    private CalendarView calendar;
    private String inputDate;
    private Spinner inputHourSpinner;
    private Spinner inputMinuteSpinner;

    private Spinner EmotionalStateSpinner;
    private Spinner SocialSituationSpinner;

    // Variables that are used to connect and reference Firebase
    FirebaseFirestore db;
    String TAG = "PostFragment";
    MoodiStorage moodiStorage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // TODO: Add implementation of: EmotionalState Spinner, Date, Image & Location
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        // Pointing variables for detection of user input
        ReasonView = view.findViewById(R.id.input_Reasoning);

        PostMood = view.findViewById(R.id.post_mood_button);
        getCustomDateButton = view.findViewById(R.id.add_date_button);
        getPictureButton = view.findViewById(R.id.add_image_button);
        getCustomLocationButton = view.findViewById(R.id.add_location_button);

        EmotionalStateSpinner = view.findViewById(R.id.input_EmotionalState_Spinner);
        SocialSituationSpinner = view.findViewById(R.id.input_SocialSituation_Spinner);

        inputHourSpinner = view.findViewById(R.id.input_hour);
        inputMinuteSpinner = view.findViewById(R.id.input_minute);
        calendar = view.findViewById(R.id.input_calendarView);

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

                // Get most recent user position data
                ((BottomNavigationActivity)getActivity()).updateUserLocation();
                GeoPoint lastLocation = ((BottomNavigationActivity)getActivity()).getLastLocation();

                // Create a new mood from the user input
                Mood mood = new Mood(index, reason, socialSituation, date);

                // Set mood location to the retrieved position data
                mood.setLocation(lastLocation);

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

        getCustomDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DateFragment().show(getActivity().getSupportFragmentManager(), "Custom_Date");
            }
        });

        return view;
    }

}