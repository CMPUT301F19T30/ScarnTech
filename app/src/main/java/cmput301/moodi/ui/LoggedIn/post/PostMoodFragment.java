package cmput301.moodi.ui.LoggedIn.post;
/*
 * Class: PostMoodFragment
 * Version 1: Fragment used to construct a post given user input!
 * Note that the mood selection list needs to be added to the fragment
 * 11/04/2019
 */

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.GeoPoint;

import cmput301.moodi.Objects.Location;
import cmput301.moodi.Objects.Mood;
import cmput301.moodi.R;

//import com.cmput301.moodi.R;

public class PostMoodFragment extends DialogFragment {

    // Variables used to pull input from user into fragment
    private EditText EmotionalStateView; // Change this to the selection from drop down list
    private EditText ReasonView;

    // Need to add location option, social situation as well as a picture

    // Used to pass data to main activity
    private OnFragmentInteractionListener listener;
//
    // Used to make any call to this fragment requires a post of a Mood
    interface OnFragmentInteractionListener{
        void addNewPost(Mood mood);
    }

    // Fragment lifecycle method
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_post_mood, null);

        // Point Variables to text on fragment
        EmotionalStateView = view.findViewById(R.id.input_EmotionalState);
        ReasonView = view.findViewById(R.id.input_Reasoning);
        //Build the fragment and set the buttons to call the delete or edit method in main
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Create Mood")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // Pull the fresh entry from the fragment
                        String emotionalStateText = EmotionalStateView.getText().toString();
                        String reasonText = ReasonView.getText().toString();

                        // Create a new Mood (Post) and send to MoodsActivity to be added to the list!
//                        if (Reasoning != null && !Reasoning.isEmpty())
//                            Mood mood = new Mood(Mood, Reasoning);
//                        else
//                            Mood newMood = new Mood(Mood);
//                        EmotionalState emotionalState = new EmotionalState(emotionalStateText);
                        Mood mood = new Mood(emotionalStateText, reasonText);
                        GeoPoint location = new GeoPoint(53.123, 113.234);
                        mood.setLocation(location);
                        listener.addNewPost(mood);
                    }
                })
                .create();
    }

}
