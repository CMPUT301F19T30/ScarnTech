package cmput301.moodi.Objects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;

import cmput301.moodi.R;
/**
 * Class MoodHistoryAdapter
 * Version 1: Creating a list class that will be used to create and manage various
 * groups of data such as users or moods
 * @since 11/04/2019
 *
 */
public class MoodHistoryAdapter extends BaseAdapter implements Filterable {

    private ArrayList<Mood> moodsList;
    private ArrayList<Mood> moodsListFiltered;
    private MoodFilter customFilter;
    private Context context;
    private static final String TAG = "MoodHistoryAdapter";

    // Bridge between the list and the displayed list
    public MoodHistoryAdapter(Context context, ArrayList<Mood> moods){
        super();
        this.moodsList = moods;
        this.moodsListFiltered = moods;
        this.context = context;
    }


    // Actual population of the list by sending entry to our content
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
        }

        Collections.sort(moodsListFiltered);
        Mood mood = moodsListFiltered.get(position);

        // Point at placeholders of the context in a post
        TextView currentEmotionalState = view.findViewById(R.id.EmotionalState_text);
        TextView currentDate = view.findViewById(R.id.Date_text);
        TextView currentUsername = view.findViewById(R.id.Username_text);
        ImageView currentEmoji = view.findViewById(R.id.emoji);

        // Pull information from the post!
        int color = mood.getEmotionalState().getColor();
        String name = mood.getEmotionalState().getName();
        String username = mood.getUsername();

        // Push information pulled from user input into a new post
        currentEmotionalState.setText(name);
        currentDate.setText(mood.getDate());
        currentEmoji.setImageResource(mood.getEmotionalState().getEmoji());
        view.setBackgroundColor(ContextCompat.getColor(context, color));

        if (username == null)
            currentUsername.setText("");
        else
            currentUsername.setText(username);
        return view;
    }


    @Override
    public int getCount() {
        return moodsListFiltered.size();
    }

    @Override
    public Mood getItem(int position) {
        return moodsListFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        if(customFilter == null) {
            customFilter = new MoodFilter();
        }
        return customFilter;
    }


    private class MoodFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<Mood> filteredList = new ArrayList<>();

            Collections.sort(moodsList);
            results.count = moodsList.size();
            results.values = moodsList;

            if (constraint.equals("Show All")) {
                return results;
            }

            if (constraint != null && constraint.length()>0) {
                constraint = constraint.toString().toLowerCase();

                for (int i=0; i< moodsList.size(); i++) {
                    Mood mood = moodsList.get(i);
                    String moodType = mood.getEmotionalState().getName().toLowerCase();
                    if(moodType.startsWith(constraint.toString())) {
                        filteredList.add(mood);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            moodsListFiltered =  (ArrayList<Mood>) filterResults.values;
            Collections.sort(moodsListFiltered);
            notifyDataSetChanged();
        }

    }

}
