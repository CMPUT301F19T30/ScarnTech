package cmput301.moodi.ui.loggedIn.social;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import cmput301.moodi.Objects.MoodiNotification;
import cmput301.moodi.Objects.MoodiNotificationsAdapter;
import cmput301.moodi.Objects.MoodiStorage;
import cmput301.moodi.Objects.User;
import cmput301.moodi.Objects.UserList;
import cmput301.moodi.Objects.UserListAdapter;
import cmput301.moodi.R;

import static cmput301.moodi.util.Constants.FOLLOW_REQUEST;

/*
 * Class: SocialFragment
 */

public class SocialFragment extends Fragment {

    private final static String TAG = "SocialFragment";
    MoodiStorage moodiStorage;

    // Variables that are used to build the list of moods (User Posts)
    ListView userListView;
    UserListAdapter userAdapter;
    UserList userList, followersList, followingList;
    EditText inputSearch;
    String userStatusFilter;

    private ListView notificationListView;
    private MoodiNotificationsAdapter notificationAdapter;
    private ArrayList<MoodiNotification> notificationList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_social, container, false);

        TabHost tabs = (TabHost) root.findViewById(R.id.tabhost);
        tabs.setup();
        TabHost.TabSpec spec = tabs.newTabSpec("tag1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Search");
        tabs.addTab(spec);
        spec = tabs.newTabSpec("tag2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Notifications");
        tabs.addTab(spec);

        moodiStorage = new MoodiStorage();

        userListView = root.findViewById(R.id.social_list);
        userList = new UserList();
        userAdapter = new UserListAdapter(container.getContext(), userList);

        //Load lists for notifications.
        notificationListView = root.findViewById(R.id.notification_list);
        notificationList = new ArrayList<>();
        notificationAdapter = new MoodiNotificationsAdapter(container.getContext(), notificationList);
        notificationListView.setAdapter(notificationAdapter);

        userListView.setAdapter(userAdapter);

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final User user = (User) adapterView.getItemAtPosition(position);

                moodiStorage.isUserFollowing(user.getUID()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            //DocumentReference doc = task.getResult();
                            Log.d(TAG, task.getResult().toString());
                            //UserDialogFragment dialog = new UserDialogFragment(user.getUsername());
                            final Dialog userDialog = new Dialog(getContext());
                            userDialog.setContentView(R.layout.user_dialog);

                            TextView usernameView = userDialog.findViewById(R.id.username);
                            usernameView.setText(user.getUsername());

                            TextView nameView = userDialog.findViewById(R.id.full_name);
                            nameView.setText(user.getFirstName() + " " + user.getLastName());

                            ImageButton closeButton = userDialog.findViewById(R.id.close);
                            closeButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    userDialog.dismiss();
                                }
                            });

                            //Todo: set status on dialog. Set follow request button if user is not currently following.
                            // Also ensure no pending notifications to user are matching the description.

                            Button folloRequestButton = userDialog.findViewById(R.id.follow_request);
                            folloRequestButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    userDialog.dismiss();
                                    HashMap<String, Object> data = new HashMap<>();
                                    data.put("sender", moodiStorage.getUID());
                                    data.put("receiver", user.getUID());
                                    data.put("type", FOLLOW_REQUEST);
                                    moodiStorage.sendFollowRequest(data);
                                    Toast.makeText(getActivity(), "Follow Request Sent!", Toast.LENGTH_SHORT).show();
                                }
                            });

                            userDialog.show();
                            Window window = userDialog.getWindow();
                            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                            //dialog.show(getActivity().getSupportFragmentManager(), "Hello");
                        } else {
                            Log.d(TAG, "Failed to check if following user.");
                        }
                    }
                });
            }
        });

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


        loadSocialList();
        this.loadNotifications();

        return root;

    }


    /*
     * Begin by displaying all. Must update on radio click change.
     */
    public void loadSocialList() {
        moodiStorage.getApplicationUsers().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    userList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (!moodiStorage.getUID().equals(document.getId())) {
                            User user = new User();
                            user.setUsername(document.getString("username"));
                            user.setFirstName(document.getString("first_name"));
                            user.setLastName(document.getString("last_name"));
                            user.setUID(document.getId());
                            userList.add(user);
                        }
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }

                userAdapter.notifyDataSetChanged();
            }
        });
    }

    public void loadFollowers() {
        moodiStorage.getFollowers().addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

            }
        });
    }

    /*
     * Load the notification view.
     */
    private void loadNotifications() {
        moodiStorage.getNotifications().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    notificationList.clear();

                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        //Todo: move notifications to social tab
                        String UID = doc.getString("sender");
                        final QueryDocumentSnapshot notificationData = doc;
                        moodiStorage.searchByUID(UID).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    Log.d(TAG, "username -> " + document.getString("username"));
                                    String senderName = document.getString("first_name") + " " +
                                            document.getString("last_name") + " (" +
                                            document.getString("username") + ")";
                                    MoodiNotification notification = new MoodiNotification();
                                    notification.setFromDocument(notificationData);
                                    notification.setSenderName(senderName);
                                    notificationList.add(notification);
                                    notificationAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
                notificationAdapter.notifyDataSetChanged();
            }
        });
    }


}