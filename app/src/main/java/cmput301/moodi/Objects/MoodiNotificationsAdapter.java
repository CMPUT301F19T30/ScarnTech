package cmput301.moodi.Objects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import cmput301.moodi.R;

/**
 * Class: MoodiNotificationsAdapter
 * This is a class that store Notifications for user
 * @see MoodiNotification
 * @since 11/04/2019
 */
public class MoodiNotificationsAdapter extends ArrayAdapter<MoodiNotification> {

    private ArrayList<MoodiNotification> notifications;
    private Context context;
    private String TAG = "MoodiNotificationsAdapter";
    private MoodiStorage moodiStorage;

    // Bridge between the list and the displayed list
    public MoodiNotificationsAdapter(Context context, ArrayList<MoodiNotification> notifications){
        super(context, 0, notifications);
        this.notifications = notifications;
        this.context = context;
        moodiStorage = new MoodiStorage();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent,false);
        }

        final MoodiNotification notification = notifications.get(position);

        TextView typeView = view.findViewById(R.id.type);
        TextView typeExplanationView = view.findViewById(R.id.type_explanation);
        Button decline = view.findViewById(R.id.decline_notification);
        Button accept = view.findViewById(R.id.accept_notification);

        String type = notification.getType();
        String sender = notification.getSenderName();

        typeView.setText(type);
        typeExplanationView.setText(sender);

       decline.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                moodiStorage.deleteNotification(notification.getDocumentID());
               Toast.makeText(getContext(),"Follow Request Declined", Toast.LENGTH_SHORT).show();
           }
       });

       accept.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                acceptNotification(notification);
               Toast.makeText(getContext(),"Follow Request Accepted", Toast.LENGTH_SHORT).show();
           }
       });

        return view;
    }

    /*
     * Accept the notification and create a follower in moodi.
     */
    public void acceptNotification(MoodiNotification notification) {
        // Ensure that the user is not already being followed.
        final MoodiNotification notificationData = notification;

        moodiStorage.isUserFollowing(notification.getSenderUID()).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {
                        HashMap<String, Object> data = new HashMap<>();
                        data.put("user", notificationData.getSenderUID());
                        data.put("following", notificationData.getReceiver());
                        data.put("permission", 1);
                        moodiStorage.createFollower(data);
                        moodiStorage.deleteNotification(notificationData.getDocumentID());
                    } else {
                        //Do nothing, follower already exists.
                    }
                }

            }
        });

    }


}
