package cmput301.moodi.Objects;

import android.graphics.Color;
import android.util.Log;

import cmput301.moodi.R;

public class EmotionalState  {

    // Properties of an emotional state
    private String name;
//    private String color;
    private int emoji;
    private int index;
    private int color;

    public EmotionalState() { }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setEmoji(int emoji) {
        this.emoji = emoji;
    }

    public int getEmoji() {
        return emoji;
    }


    /*
    * Takes the index of the selected item from the spinner and returns
    * the corresponding emotional state selected as an object
     */
    public void setEmotionalState(int i) {

        // Preset colors associated with the provided emotional states!
        int HappyColor = R.color.Happy;
        int MadColor = R.color.Mad;
        int SadColor = R.color.Sad;
        int LoveColor = R.color.Love;
        int TiredColor = R.color.Tired;

        // Preset emojis associated with the provided emotional states!
        int HappyEmoji = R.drawable.happy;
        int MadEmoji = R.drawable.mad;
        int SadEmoji = R.drawable.sad;
        int LoveEmoji = R.drawable.love;
        int TiredEmoji = R.drawable.tired;

        switch(i) {
            case 0: // Happy
                setName("Happy");
                setColor(HappyColor);
                setEmoji(HappyEmoji);
                setIndex(0);
                break;
            case 1: // Mad
                setName("Mad");
                setColor(MadColor);
                setEmoji(MadEmoji);
                setIndex(1);
                break;
            case 2: // Sad
                setName("Sad");
                setColor(SadColor);
                setEmoji(SadEmoji);
                setIndex(2);
                break;
            case 3: // Love
                setName("Love");
                setColor(LoveColor);
                setEmoji(LoveEmoji);
                setIndex(3);
                break;
            case 4: // Tired
                setName("Tired");
                setColor(TiredColor);
                setEmoji(TiredEmoji);
                setIndex(4);
                break;
        }
    }
}
