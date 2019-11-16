package cmput301.moodi.ui.loggedIn.post;

import android.Manifest;
import android.app.Activity;
import android.app.AppComponentFactory;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CalendarView;
import android.widget.ArrayAdapter;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.internal.CircularBorderDrawable;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

import cmput301.moodi.Objects.Mood;
import cmput301.moodi.Objects.MoodiStorage;
import cmput301.moodi.R;
import cmput301.moodi.ui.loggedIn.BottomNavigationActivity;

import static android.app.Activity.RESULT_OK;

/*
 * Class: PostFragment
 * Posting page where a user enters a "post" and on button press, sends it to the database
 * 11/09/2019
 */
public class PostFragment extends Fragment {

    // Variables used to detect user input and send a request!
    private String DateAndTime = ""; // Change this to the selection from drop down list
    private EditText ReasonView;

    private ImageButton PostMoodButton;
    private ImageButton getLocationButton;
    private ImageButton getPictureButton;
    private ImageButton getPhotoGalleryButton;

    private ImageView userPhoto;

    private CalendarView calendar;
    private String inputDate;
    private Spinner inputHourSpinner;
    private Spinner inputMinuteSpinner;

    private Spinner EmotionalStateSpinner;
    private Spinner SocialSituationSpinner;

    // Codes used to check permissions
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private int RESULT_LOAD_IMG;

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

        PostMoodButton = view.findViewById(R.id.post_mood_button);
        getPhotoGalleryButton = view.findViewById(R.id.add_gallery_button);
        getPictureButton = view.findViewById(R.id.add_picture_button);
        getLocationButton = view.findViewById(R.id.add_location_button);

        userPhoto = view.findViewById(R.id.input_photo);

        EmotionalStateSpinner = view.findViewById(R.id.input_EmotionalState_Spinner);
        SocialSituationSpinner = view.findViewById(R.id.input_SocialSituation_Spinner);

        inputHourSpinner = view.findViewById(R.id.input_hour);
        inputMinuteSpinner = view.findViewById(R.id.input_minute);
        calendar = view.findViewById(R.id.input_calendarView);

        // Get the current time and update the spinners in event that user doesnt customize date
        Calendar instantCalendar = Calendar.getInstance();
        int year = instantCalendar.get(instantCalendar.YEAR);
        int month = instantCalendar.get(instantCalendar.MONTH);
        int dayOfMonth = instantCalendar.get(instantCalendar.DAY_OF_MONTH);
        String newDate = year + "-" + month + "-" + dayOfMonth;
        inputDate = newDate;
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

        // Set onClick Listener to retrieve a photo from the gallery
        getPhotoGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

        // Set onClick Listener to put in a picture taken
        getPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE); }

                else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST); }
            }
        });

        // Set onClick Listener for creation of new post
        PostMoodButton.setOnClickListener(new View.OnClickListener() {
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
                ((BottomNavigationActivity) getActivity()).updateUserLocation();
                GeoPoint lastLocation = ((BottomNavigationActivity) getActivity()).getLastLocation();

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
                userPhoto.setImageResource(R.drawable.ic_launcher_background);
            }
        });
        return view;
    }

    /*
    * Sets image provided from gallery
     */
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        // Picture from camera
        if (resultCode == RESULT_OK && reqCode == CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            userPhoto.setImageBitmap(photo); }
        // Gallery photo
        else {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            userPhoto.setImageURI(data.getData());

            try {
                final Uri imageUri = data.getData();
                final InputStream image;
                image = getContext().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(image);
                userPhoto.setImageBitmap(selectedImage); }

            catch (FileNotFoundException e) {
                e.printStackTrace(); }
            }
    }
}