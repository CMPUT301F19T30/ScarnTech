package cmput301.moodi.Objects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cmput301.moodi.R;

/*
 * Class: CustomList
 * Version 1: Creating a list class that will be used to create and manage various
 * groups of data such as users or moods
 * 11/04/2019
 */
public class MoodiNotificationsAdapter extends BaseAdapter {

    private NotificationList notifications;
    private Context context;

    // Bridge between the list and the displayed list
    public MoodiNotificationsAdapter(Context context, NotificationList notifications){
        super();
        this.notifications = notifications;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    // Actual population of the list by sending entry to our content
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
        String sender = notification.getSender();

        typeView.setText(type);
        typeExplanationView.setText(sender);

        return view;
    }
}
