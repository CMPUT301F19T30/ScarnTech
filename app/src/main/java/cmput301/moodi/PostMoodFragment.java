package cmput301.moodi;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

//import com.cmput301.moodi.R;

public class PostMoodFragment extends DialogFragment {

    // Variables used to pull input from user into fragment
    private EditText EmotionalState; // Change this to the selection from drop down list
    private EditText Reason;

    // Need to add location option, social situation as well as a picture

    // Used to pass data to main activity
    private OnFragmentInteractionListener listener;

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
        EmotionalState = view.findViewById(R.id.input_EmotionalState);
        Reason = view.findViewById(R.id.input_Reasoning);
        //Build the fragment and set the buttons to call the delete or edit method in main
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("New Trip")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // Pull the fresh entry from the fragment
                        String Mood = EmotionalState.getText().toString();
                        String Reasoning = Reason.getText().toString();

                        // Create a new Mood (Post) and send to MoodsActivity to be added to the list!
//                        if (Reasoning != null && !Reasoning.isEmpty())
//                            Mood mood = new Mood(Mood, Reasoning);
//                        else
//                            Mood newMood = new Mood(Mood);
                        Mood mood = new Mood(Mood, Reasoning);
                        listener.addNewPost(mood);
                    }
                })
                .create();
    }

}
