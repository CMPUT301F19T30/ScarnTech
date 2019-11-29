package cmput301.moodi.Objects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import cmput301.moodi.R;

/**
 * Class: CustomList
 * Version 1: Creating a list class that will be used to create and manage various
 * groups of data such as users or moods
 * @since 11/04/2019
 */
public class MoodListAdapter extends MoodHistoryAdapter {

    private ArrayList<Mood> moods;
    private Context context;

    // Bridge between the list and the displayed list
    public MoodListAdapter(Context context, ArrayList<Mood> moods){
        super(context, moods);
        this.moods = moods;
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

        Mood mood = moods.get(position);

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
            currentUsername.setText("Old Post No Username Yet");
        else
            currentUsername.setText(username);
        return view;
    }
}
