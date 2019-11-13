package cmput301.moodi.ui.LoggedIn.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;

import cmput301.moodi.Objects.MoodiStorage;
import cmput301.moodi.Objects.User;
import cmput301.moodi.R;
import cmput301.moodi.ui.Login.LoginActivity;

/*
 * Class: ProfileFragment
 *
 * Display profile username and name. Allow user to edit username and name. Display users personal mood history.
 */

public class ProfileFragment extends Fragment {

    private cmput301.moodi.ui.LoggedIn.profile.ProfileViewModel profileViewModel;
    private MoodiStorage moodiStorage;
    private static final String TAG = "ProfileActivity";
    private User userProfile;
    private ImageButton logout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        profileViewModel = ViewModelProviders.of(this).get(cmput301.moodi.ui.LoggedIn.profile.ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        moodiStorage = new MoodiStorage();
        userProfile = new User();

        /*
        //Todo: get user profile data and display it on page.
        final DocumentReference userProfile = moodiStorage.getUserProfile();
        userProfile.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        //Todo: parse data to display on profile page.

                        //userProfile.setupProfile(document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        */


        //final TextView textView = root.findViewById(R.id.text_profile);
        logout = root.findViewById(R.id.logout);


        profileViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { logoutUser(); }
        });

        return root;
    }


    /*
     * Used to log user out of application.
     */
    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getActivity(), LoginActivity.class);
        i.putExtra("finish", true); // if you are checking for this in your other Activities
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        getActivity().finish();
    }
}