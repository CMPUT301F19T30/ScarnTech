package cmput301.moodi.Objects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

import cmput301.moodi.R;

/*
 * Class: CustomList
 * Version 1: Creating a list class that will be used to create and manage various
 * groups of data such as users or moods
 * 11/04/2019
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
           }
       });

       accept.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               HashMap<String, Object> data = new HashMap<>();
               data.put("user", notification.getSenderUID());
               data.put("following", notification.getReceiver());
               data.put("permission", 2);
               moodiStorage.createFollower(data);
               moodiStorage.deleteNotification(notification.getDocumentID());
           }
       });

        return view;
    }


}
