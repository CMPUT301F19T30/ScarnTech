package cmput301.moodi.Objects;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
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
public class UserListAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private UserList usersList;
    private UserList usersListFiltered;
    private UserFilter customFilter;
    String TAG = "UserListAdapter";

    public UserListAdapter(Context context, UserList users){
        super();
        this.usersList = users;
        this.usersListFiltered = users;
        this.context = context;
    }

    @Override
    public int getCount() {
        return usersListFiltered.size();
    }

    @Override
    public User getItem(int position) {
        return usersListFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.social_list_item, parent,false);
        }

        TextView nameView = view.findViewById(R.id.name);
        TextView usernameView = view.findViewById(R.id.username);

        User user = usersListFiltered.get(position);
        String name = user.getFirstName() + " " + user.getLastName();
        nameView.setText(name);
        usernameView.setText(user.getUsername());

        return view;
    }

    @Override
    public Filter getFilter() {
        if(customFilter == null) {
            customFilter = new UserFilter();
        }
        return customFilter;
    }

    private class UserFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            UserList filteredList = new UserList();
            Log.d(TAG, "Filtering result now -> " + constraint.toString());

            if (constraint != null && constraint.length()>0) {
                constraint = constraint.toString().toLowerCase();

                for (int i=0; i< usersList.size(); i++) {
                    User user = usersList.get(i);
                    String username = user.getUsername();

                    if(username.toLowerCase().startsWith(constraint.toString())) {
                        Log.d(TAG, "->" + user.getUsername());
                        filteredList.add(user);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;

            } else {
                results.count = usersList.size();
                results.values = usersList;
            }



            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            usersListFiltered = (UserList) filterResults.values;
            notifyDataSetChanged();
        }


    }

}
