package cmput301.moodi.ui.loggedIn.home;

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

import cmput301.moodi.Objects.Mood;
import cmput301.moodi.Objects.MoodiStorage;
import cmput301.moodi.R;
import cmput301.moodi.ui.loggedIn.profile.EditFragment;

/*
 * Class: ViewFragment
 * Fragment pulled up to pull up information on a post
 * 11/12/2019
 */


public class ViewFragment extends DialogFragment{

    // Variables used to detect user input and send a request!
    private TextView EmotionalState;
    private TextView Reason;
    private TextView SocialSituation;
    private TextView Date;
    private TextView Location;
    private TextView ImageText;
    private ImageView Image;
    private TextView Username;

    FirebaseStorage storage = FirebaseStorage.getInstance("gs://moodi-app-1cf5d.appspot.com/");

    // Used to pass data to main activity
    private viewPost listener;

    // Takes a selected moods properties to be able to reconstruct on view
    public static ViewFragment viewSelection(Mood selectedMood) {
        Bundle args = new Bundle();

        // Checking for no input
        GeoPoint location = selectedMood.getLocation();
        String reason = selectedMood.getReason();

        if (reason != "")
            args.putSerializable("User Reason", reason);
        else
            args.putSerializable("User Reason", "No Reason Available");

        if (location != null)
            args.putSerializable("Location", location.toString());
        else
            args.putSerializable("Location", "No Location Available");

        args.putSerializable("User Emotional State", selectedMood.getEmotionalState().getName());
        args.putSerializable("User Social Situation", selectedMood.getSocialSituation());
        args.putSerializable("User Date", selectedMood.getDate());
        args.putSerializable("Image", selectedMood.getImage());
        args.putSerializable("Username", selectedMood.getUniqueID());
        args.putSerializable("Color", selectedMood.getEmotionalState().getColor());

        ViewFragment fragment = new ViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

        // Used to make any call to this fragment require a reponse to a custom date entry
        public interface viewPost {
            void viewThePost();
        }

        // Fragment lifecycle method
        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            if (context instanceof viewPost) {
                listener = (viewPost) context;
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

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_view_post, null);

            // Point Variables to text on fragment
            EmotionalState = view.findViewById(R.id.input_EmotionalState_textView);
            Reason = view.findViewById(R.id.input_Reasoning_textView);
            SocialSituation = view.findViewById(R.id.input_SocialSituation_textView);
            Date = view.findViewById(R.id.input_Date_textView);
            Location = view.findViewById((R.id.input_Location_textView));
            Image = view.findViewById(R.id.view_photo);
            ImageText = view.findViewById(R.id.view_Photo_content);
            Username = view.findViewById(R.id.input_User_textView);

            // Onclick listener to take a user to the selected profile!
            Username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Make it close after opening new fragment
                    new FollowingProfileFragment();
                    FollowingProfileFragment.viewProfile(Username.getText().toString()).show(getChildFragmentManager(),"View User Profile");
                }
            });
            // Retrieve properties of selected mood from bundle and populate display
            Bundle args = getArguments();
            if (args != null) {

                // Set text views to user selected mood
                EmotionalState.setText(args.getString("User Emotional State"));
                EmotionalState.setTextColor(ContextCompat.getColor(getContext(), args.getInt("Color")));
                Reason.setText(args.getString("User Reason"));
                SocialSituation.setText(args.getString("User Social Situation"));
                Date.setText(args.getString("User Date"));
                Location.setText(args.getString("Location"));
                Username.setText(args.getString("Username"));

                String path = args.getString("Image");
                if (path != null && !path.equals("No Photo Available")){
                    // Create a storage reference from our app
                    StorageReference storageRef = storage.getReference();
                    StorageReference userRef = storageRef.child(path);

                    final long ONE_MEGABYTE = 1024 * 1024;
                    userRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length );
                            Image.setVisibility(View.VISIBLE);
                            ImageText.setText("");
                            Image.setImageBitmap(bitmap);
                        }
                    });}
                else {
                    ImageText.setText("No Photo Available");
                    Image.setVisibility(View.GONE);
                }
            }

            //Build the fragment and set the buttons to call the delete or edit method in main
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            return builder
                    .setView(view)
                    .setNegativeButton("Exit", null)
                    .create();

        }
    }

