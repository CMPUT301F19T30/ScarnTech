package cmput301.moodi.ui.loggedIn.profile;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import cmput301.moodi.Objects.Mood;
import cmput301.moodi.R;

/*
 * Class: DateFragment
 * Fragment pulled up to enter a custom date if the user does not want the instantaneous date
 * on post creation.
 * 11/12/2019
 */

public class EditFragment  extends DialogFragment {

    // Variables used to detect user input and send a request!
    private EditText ReasonView;
    private TextView Location;
    private TextView Date;
    private TextView ImageText;
    private Spinner EmotionalStateSpinner;
    private Spinner SocialSituationSpinner;
    private ImageView Image;
    private String UniquePostID;

    // Used to pass data to main activity
    private addNewDate listener;

    // Variables that are used to connect and reference Firebase
    FirebaseFirestore db;
    String TAG = "PostFragment";
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://moodi-app-1cf5d.appspot.com/");


    // Takes a selected moods properties to be able to reconstruct on edit
    public static EditFragment editSelection(Mood selectedMood) {
        Bundle args = new Bundle();

        // Convert type Number to the serializable type int
        int Index = selectedMood.getEmotionalState().getIndex();

        // Checking for no input
        GeoPoint location = selectedMood.getLocation();
        String reason = selectedMood.getReason();

        if (reason != "")
            args.putSerializable("Old Reason", reason);
        else
            args.putSerializable("Old Reason", "No Reason Available");

        if (location != null)
            args.putSerializable("Old Location", location.toString());
        else
            args.putSerializable("Old Location", "No Location Available");

        args.putSerializable("Old UniqueKeyID", selectedMood.getKeyID());
        args.putSerializable("Old Index", Index);
        args.putSerializable("Old SocialSituation", selectedMood.getSocialSituation());
        args.putSerializable("Old Date", selectedMood.getDate());
        args.putSerializable("Old Image", selectedMood.getImage());

        EditFragment fragment = new EditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // Used to make any call to this fragment require a reponse to a custom date entry
    public interface addNewDate {
        void customDate();
    }

    // Fragment lifecycle method
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof addNewDate) {
            listener = (addNewDate) context;
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

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_post, null);

        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();

        // Get a top-level reference to the collection.
        final CollectionReference collectionReference = db.collection("posts");

        // Point Variables to text on fragment
        ReasonView = view.findViewById(R.id.input_Reasoning);
        EmotionalStateSpinner = view.findViewById(R.id.input_EmotionalState_Spinner);
        SocialSituationSpinner = view.findViewById(R.id.input_SocialSituation_Spinner);
        Date = view.findViewById(R.id.edit_Date_textView);
        Location = view.findViewById((R.id.edit_Location_textView));
        ImageText = view.findViewById(R.id.edit_Photo_content);
        Image = view.findViewById(R.id.edit_photo);

        // Retrieve properties of selected mood from bundle and populate display
        Bundle args = getArguments();
        if (args != null) {
            UniquePostID = args.getString("Old UniqueKeyID");

            EmotionalStateSpinner.setSelection(args.getInt("Old Index"));
            Date.setText(args.getString("Old Date"));
            Location.setText(args.getString("Old Location"));

            // Check if reason is empty
            String intermediate2 = args.getString("Old Reason");
            if (intermediate2 == "No Reason Available")
                ReasonView.setHint("Add a Reason");
            else
                ReasonView.setText(intermediate2);

            // Check for which social situation and put into spinner!
            String inputSituation = args.getString("Old SocialSituation");
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.social_situation, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            SocialSituationSpinner.setAdapter(adapter);
            if (inputSituation != null) {
                int index = adapter.getPosition(inputSituation);
                SocialSituationSpinner.setSelection(index);
            }

            // Checking if an image is available
            String path = args.getString("Old Image");
            //Log.d("Recieved path", path);
            //Log.d("Expected path", "No Photo Available");
            if (path != null && !path.equals("No Photo Available")){
                // Create a storage reference from our app
                StorageReference storageRef = storage.getReference();
                StorageReference userRef = storageRef.child(path);

                final long ONE_MEGABYTE = 1024 * 1024;
                userRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length );
                        ImageText.setText("");
                        Image.setVisibility(View.VISIBLE);
                        Image.setImageBitmap(bitmap);
                    }
                });
                }
            else {
                ImageText.setText("No Photo Available");
                Image.setVisibility(View.GONE);
            }
        }

        // Build the fragment and set the buttons to call the delete or edit method in main
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            return builder
                    .setView(view)
                    .setNeutralButton("Cancel", null)
                    .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            collectionReference
                                    .document(UniquePostID)
                                    .delete();
                            listener.customDate();
                            dismiss();
                        }
                    })
                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            // Pull the edited entry from the fragment
                            String reason = ReasonView.getText().toString();
                            String socialSituation = SocialSituationSpinner.getSelectedItem().toString();
                            Number index = EmotionalStateSpinner.getSelectedItemPosition();

                            collectionReference
                                    .document(UniquePostID)
                                    .update(
                                            "Reason", reason,
                                            "Social Situation", socialSituation,
                                            "Index", index);
                            listener.customDate();
                       }
                    })
                    .create();

    }
}
