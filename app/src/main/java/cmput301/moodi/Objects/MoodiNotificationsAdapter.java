package cmput301.moodi.Objects;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

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

    // Bridge between the list and the displayed list
    public MoodiNotificationsAdapter(Context context, ArrayList<MoodiNotification> notifications){
        super(context, 0, notifications);
        this.notifications = notifications;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent,false);
        }

        MoodiNotification notification = notifications.get(position);

        TextView typeView = view.findViewById(R.id.type);
        TextView typeExplanationView = view.findViewById(R.id.type_explanation);

        String type = notification.getType();
        String sender = notification.getSenderName();

        typeView.setText(type);
        typeExplanationView.setText(sender);

        Log.d(TAG, "Here now. ");

        return view;
    }
}
