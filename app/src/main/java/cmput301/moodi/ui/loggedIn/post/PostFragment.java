package cmput301.moodi.ui.loggedIn.post;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.UUID;

import cmput301.moodi.Objects.Mood;
import cmput301.moodi.Objects.MoodiStorage;
import cmput301.moodi.R;

import static android.app.Activity.RESULT_OK;

/**
 * Class: PostFragment
 * Posting page where a user enters a "post" and on button press, sends it to the database
 * @since 11/09/2019
 */
public class PostFragment extends Fragment {

    // Variables used to detect user input and send a request!
    private EditText ReasonView;

    private Spinner EmotionalStateSpinner;
    private Spinner SocialSituationSpinner;

    private ImageButton PostMoodButton;
    private ImageButton getLocationButton;
    private ImageButton getPictureButton;
    private ImageButton getPhotoGalleryButton;

    private Switch location_toggle;

    private ImageView userPhoto;

    private CalendarView calendar;
    private String inputDate;
    private Spinner inputHourSpinner;
    private Spinner inputMinuteSpinner;

    private String lastLat = null;
    private String lastLon = null;

    // Codes used to check permissions and pulls image
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private int RESULT_LOAD_IMG;

    // Variables that are used to connect and reference Firebase
    FirebaseFirestore db;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    String TAG = "PostFragment";
    MoodiStorage moodiStorage;
    public String username;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        // Pointing variables for detection of user input (Text, buttons, than spinners and calendar)
//        intermediate = view.findViewById(R.id.intermediate);

        ReasonView = view.findViewById(R.id.input_Reasoning);

        EmotionalStateSpinner = view.findViewById(R.id.input_EmotionalState_Spinner);
        SocialSituationSpinner = view.findViewById(R.id.input_SocialSituation_Spinner);

        PostMoodButton = view.findViewById(R.id.post_mood_button);
        getPhotoGalleryButton = view.findViewById(R.id.add_gallery_button);
        getPictureButton = view.findViewById(R.id.add_picture_button);
        getLocationButton = view.findViewById(R.id.add_location_button);

        location_toggle = view.findViewById(R.id.location_toggle);

        userPhoto = view.findViewById(R.id.input_photo);

        inputHourSpinner = view.findViewById(R.id.input_hour);
        inputMinuteSpinner = view.findViewById(R.id.input_minute);
        calendar = view.findViewById(R.id.input_calendarView);

        // Get the current time and update the spinners in event that user doesn't customize date
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

        // Reference to check for a new image from the user
        final Drawable oldDrawable = userPhoto.getDrawable();

        // Getting updated date
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
        db = FirebaseFirestore.getInstance();

        // User selected to add a location to the mood, go to add moood activity
        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddLocationActivity.class);
                // pass old location to add location activity for aesthetic
                if (lastLat != null && lastLon != null) {
                    i.putExtra("Latitude", lastLat);
                    i.putExtra("Longitude", lastLon);
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
                    lastLat = String.valueOf(lastLocation.getLatitude());
                    lastLon = String.valueOf(lastLocation.getLongitude());
//                    lastLat.setText(String.valueOf(lastLocation.getLatitude()));
//                    lastLon.setText(String.valueOf(lastLocation.getLongitude()));
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

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
                    // TODO: Look into why permission not being sent or acknowledged (Catching something when exiting camera?)
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST); }

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        // Getting unique username and storing in a textView
        String UID = moodiStorage.getUID();
        CollectionReference Colref = db.collection("users");
        DocumentReference docReff = Colref.document(UID);
        docReff.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        username = document.getString("username");
//                        intermediate.setText(document.getString("username"));
                    } else {
                        Log.d("MoodiStorage", "No such user");
                    }
                } else {
                    Log.d("MoodiStorage", "get failed with ", task.getException());
                }
            }
        });

        // Set onClick Listener for creation of new post
        PostMoodButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
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

                // Pulling image from user input & converting the data from an ImageView to bytes
                if (userPhoto.getDrawable() != oldDrawable) {
                    Bitmap capture = Bitmap.createBitmap(
                            userPhoto.getWidth(),
                            userPhoto.getHeight(),
                            Bitmap.Config.ARGB_8888);
                    Canvas captureCanvas = new Canvas(capture);
                    userPhoto.draw(captureCanvas);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    capture.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    byte[] data = outputStream.toByteArray();

                    // Creating a reference to the firebase storage for our users photos
                    String path = "UserImage/" + UUID.randomUUID() + ".png";
                    StorageReference userPhotoRef = storage.getReference(path);

                    // Upload image to firebase with that url as a reference
                    UploadTask uploadTask = userPhotoRef.putBytes(data);
                }

                // Create a new mood from the user input without an image
                Mood mood = new Mood(index, reason, socialSituation, date, username);

                // Set mood location to the most recent location data from firebase (through text view)
                if (location_toggle.isChecked()) {
                    mood.setLocation(new GeoPoint(Double.valueOf(lastLat), Double.valueOf(lastLon)));
                }

                // Send data to the database
                moodiStorage.addMoodPost(mood);

                // reset the new post data and show user a post confirmation
                resetPost();
                new PostConfirmationFragment().show(getChildFragmentManager(), "Post_Confirmation_Fragment");
            }
        });
        return view;
    }

    /*
     * Reset posts input fields to prepare for more entries
     */
    public void resetPost() {
        ReasonView.setText("");
        SocialSituationSpinner.setSelection(0);
        EmotionalStateSpinner.setSelection(0);
        inputHourSpinner.setSelection((int) ((System.currentTimeMillis() / (1000*60*60)) % 24));
        inputMinuteSpinner.setSelection((int) ((System.currentTimeMillis() / (1000*60)) % 60));
        userPhoto.setImageResource(android.R.drawable.ic_menu_gallery);
        calendar.setDate(System.currentTimeMillis(), false, true);
    }

    /*
    * Sets image provided from gallery
     */
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        // Picture from camera
        if (resultCode == RESULT_OK && reqCode == CAMERA_REQUEST) {
            Bitmap photograph = (Bitmap) data.getExtras().get("data");
            userPhoto.setImageBitmap(photograph); }
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