package cmput301.moodi.ui.LoggedIn.social;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import cmput301.moodi.Objects.MoodiStorage;
import cmput301.moodi.Objects.User;
import cmput301.moodi.Objects.UserListAdapter;
import cmput301.moodi.R;

/*
 * Class: SocialFragment
 * Todo: implement adding friends functionality
 * Todo: display all users of the app and allow filter of results
 * Todo:
 */

public class SocialFragment extends Fragment {

    private final static String TAG = "SocialFragment";
    MoodiStorage moodiStorage;

    // Variables that are used to build the list of moods (User Posts)
    ListView userList;
    ArrayAdapter<User> userAdapter;
    ArrayList<User> userDataList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_social, container, false);

        moodiStorage = new MoodiStorage();

        userList = root.findViewById(R.id.social_list);
        userDataList = new ArrayList<>();
        userAdapter = new UserListAdapter(container.getContext(), userDataList);
        userList.setAdapter(userAdapter);

        loadSocialList();

        return root;
    }

    public void loadSocialList() {
        moodiStorage.getApplicationUsers().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    userDataList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, "document ->" + document.getData());

                        User user = new User();
                        user.setUsername(document.getString("username"));
                        user.setFirstName(document.getString("first_name"));
                        user.setLastName(document.getString("last_name"));

                        userDataList.add(user);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }

                userAdapter.notifyDataSetChanged();
            }
        });
    }
}