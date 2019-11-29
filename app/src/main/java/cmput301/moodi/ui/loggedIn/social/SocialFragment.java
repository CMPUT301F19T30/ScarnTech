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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
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

/**
 * Class: SocialFragment
 * allow user to see a list of people they are following and people who are following them
 * @since 11/13/2019
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
    TabHost tabs;

    private ListView notificationListView;
    private MoodiNotificationsAdapter notificationAdapter;
    private ArrayList<MoodiNotification> notificationList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_social, container, false);
        this.setupTabs(root);

        moodiStorage = new MoodiStorage();

        //Load notification resources.
        notificationListView = root.findViewById(R.id.notification_list);
        notificationList = new ArrayList<>();
        notificationAdapter = new MoodiNotificationsAdapter(container.getContext(), notificationList);
        notificationListView.setAdapter(notificationAdapter);

        // Load social search resources.
        userListView = root.findViewById(R.id.social_list);
        userList = new UserList();
        userAdapter = new UserListAdapter(container.getContext(), userList);
        userListView.setAdapter(userAdapter);

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                User user = (User) adapterView.getItemAtPosition(position);
                dialogHandler(user);
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

        // Load views.
        loadSocialList();
        this.loadNotifications();

        return root;

    }

    /*
     * Setup tabs for view.
     */
    public void setupTabs(View root) {
        tabs = (TabHost) root.findViewById(R.id.tabhost);
        tabs.setup();
        TabHost.TabSpec spec = tabs.newTabSpec("tag1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Search");
        tabs.addTab(spec);
        spec = tabs.newTabSpec("tag2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Notifications");
        tabs.addTab(spec);
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


    /*
     * Setup the dialog view
     */
    private void dialogHandler(final User userData) {
        final User user = userData;
        final Dialog userDialog = new Dialog(getContext());
        userDialog.setContentView(R.layout.user_dialog);
        TextView usernameView = userDialog.findViewById(R.id.username);
        ImageButton closeButton = userDialog.findViewById(R.id.close);
        TextView nameView = userDialog.findViewById(R.id.full_name);
        final TextView statusView = userDialog.findViewById(R.id.status);
        statusView.setText("");
        final Button followRequestButton = userDialog.findViewById(R.id.follow_request);
        followRequestButton.setVisibility(View.INVISIBLE);


        moodiStorage.isUserFollowing(user.getUID()).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {
                        moodiStorage.isNotificationPending(userData.getUID()).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    if(task.getResult().isEmpty()) {
                                        Log.d(TAG, "Empty!");
                                        statusView.setText("Not following");
                                        setupRequestButton(followRequestButton, userDialog, user);

                                    } else {
                                        statusView.setText("Pending Request");
                                    }
                                }
                            }
                        });

                    } else {
                        // Currently following the user.
                        Log.d(TAG, "Following");
                        statusView.setText("Following");
                        // Todo: Allow to remove user
                    }


                } else {
                    Log.d(TAG, "Failed to check if following user.");
                }
            }
        });

        usernameView.setText(user.getUsername());
        nameView.setText(user.getFirstName() + " " + user.getLastName());

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDialog.dismiss();
            }
        });

        userDialog.show();
        Window window = userDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }


    /*
     * Setup Follow Button
     */
    public void setupRequestButton(Button button, Dialog userDialog, User userData) {
        final User user = userData;
        final Dialog dialog = userDialog;

        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                HashMap<String, Object> data = new HashMap<>();
                data.put("sender", moodiStorage.getUID());
                data.put("receiver", user.getUID());
                data.put("type", FOLLOW_REQUEST);
                moodiStorage.sendFollowRequest(data);
                Toast.makeText(getActivity(), "Follow Request Sent!", Toast.LENGTH_SHORT).show();
            }
        });

    }




    /*
     * Load the notification view.
     */
    private void loadNotifications() {
        moodiStorage.getNotificationReference().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                notificationList.clear();

                moodiStorage.getNotifications().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
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
        });
    }
}