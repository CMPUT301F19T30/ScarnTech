package cmput301.moodi.Objects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import cmput301.moodi.R;
import cmput301.moodi.ui.LoggedIn.home.HomeFragment;

/*
 * Class: CustomList
 * Version 1: Creating a list class that will be used to create and manage various
 * groups of data such as users or moods
 * 11/04/2019
 */
public class MoodListAdapter extends ArrayAdapter<Mood> {

    private ArrayList<Mood> moods;
    private Context context;

    // Bridge between the list and the displayed list
    public MoodListAdapter(Context context, ArrayList<Mood> moods){
        super(context,0, moods);
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

        // TODO: Add implemetation of new EmotionalState updates and firebase
        Mood mood = moods.get(position);

        // Point at placeholders in list
        TextView currentEmotionalState = view.findViewById(R.id.EmotionalState_text);
        TextView currentDate = view.findViewById(R.id.Date_text);
        ImageView img = view.findViewById(R.id.emoji);


        // Update this to account for a drop down of pre-made moods!
        // currentEmotionalState.setText(mood.getEmotionalState().toString());
        currentEmotionalState.setText(mood.getDummyEmotionalState());
        currentDate.setText(mood.getDate());


        if(currentEmotionalState.getText().equals("Happy") ){
            img.setImageResource(R.drawable.happy);
        }
        else if (currentEmotionalState.getText().equals("Mad")){
            img.setImageResource(R.drawable.mad);
        }
        else if (currentEmotionalState.getText().equals("Sad")){
            img.setImageResource(R.drawable.sad);
        }
        else if (currentEmotionalState.getText().equals("Love")){
            img.setImageResource(R.drawable.love);
        }
        else if (currentEmotionalState.getText().equals("Tired")){
            img.setImageResource(R.drawable.tired);
        }
        else{
            img.setImageResource(R.drawable.heartbreak);
        }



        return view;

    }
}
