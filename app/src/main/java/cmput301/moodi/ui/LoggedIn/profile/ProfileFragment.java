package cmput301.moodi.ui.LoggedIn.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;

import cmput301.moodi.R;
import cmput301.moodi.ui.Login.LoginActivity;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    Button logout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        View root = inflater.inflate(R.layout.fragment_profile, container, false);


        final TextView textView = root.findViewById(R.id.text_profile);
        logout = root.findViewById(R.id.logout);


        profileViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {textView.setText(s);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                i.putExtra("finish", true); // if you are checking for this in your other Activities
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                getActivity().finish();
            }
        });

        return root;
    }
}