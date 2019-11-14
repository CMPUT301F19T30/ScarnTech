package cmput301.moodi.Objects;

import android.content.Context;
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
public class UserListAdapter extends ArrayAdapter<User> {

    private ArrayList<User> users;
    private Context context;

    public UserListAdapter(Context context, ArrayList<User> users){
        super(context,0, users);
        this.users = users;
        this.context = context;
    }

    // Actual population of the list by sending entry to our content
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.social_list_item, parent,false);
        }

        TextView nameView = view.findViewById(R.id.name);
        TextView usernameView = view.findViewById(R.id.username);

        User user = users.get(position);
        String name = user.getFirstName() + " " + user.getLastName();
        nameView.setText(name);
        usernameView.setText(user.getUsername());

        return view;
    }
}
