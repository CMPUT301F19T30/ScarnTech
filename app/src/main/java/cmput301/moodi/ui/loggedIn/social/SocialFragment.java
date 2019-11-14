package cmput301.moodi.ui.loggedIn.social;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import cmput301.moodi.Objects.MoodiStorage;
import cmput301.moodi.Objects.User;
import cmput301.moodi.Objects.UserList;
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
    ListView userListView;
    UserListAdapter userAdapter;
    UserList userList, followersList, followingList;
    EditText inputSearch;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_social, container, false);

        moodiStorage = new MoodiStorage();

        userListView = root.findViewById(R.id.social_list);
        userList = new UserList();
        followersList = new UserList();
        followingList = new UserList();

        userAdapter = new UserListAdapter(container.getContext(), userList);
        userListView.setAdapter(userAdapter);

        loadSocialList();

        inputSearch = (EditText) root.findViewById(R.id.search_bar);
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                userAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            @Override
            public void afterTextChanged(Editable arg0) {}
        });

        return root;
    }

    public void loadSocialList() {
        moodiStorage.getApplicationUsers().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    userList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, "document ->" + document.getData());

                        User user = new User();
                        user.setUsername(document.getString("username"));
                        user.setFirstName(document.getString("first_name"));
                        user.setLastName(document.getString("last_name"));

                        userList.add(user);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }

                userAdapter.notifyDataSetChanged();
            }
        });
    }

    public void onRadioSelect(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radio_all:
                if (checked)
                    Log.d(TAG, "show all");
                break;

            case R.id.radio_followers:
                if (checked)
                    Log.d(TAG, "show followers");
                    break;

            case R.id.radio_following:
                if (checked)
                    Log.d(TAG, "show following");
                    break;
        }
    }
}