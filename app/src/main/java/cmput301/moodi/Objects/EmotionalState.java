package cmput301.moodi.Objects;

import android.graphics.Color;

import java.lang.reflect.Array;

public class EmotionalState  {
    private String mood;
    private String color;
    private int image;
    private EmotionalState emotionalState;
    //private EmotionalState[] moods;



    public EmotionalState(String mood,String color,int image) {
        this.mood = mood;
        this.color = color;
        this.image = image;
    }

    public String getmood() {
        return mood;
    }


    public void setEmotionalState(String mood,String color,int image){
        this.mood = mood;
        this.image = image;
        this.color = color;
    }


    public  EmotionalState getEmotionalState() {
        return emotionalState;
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getImage(){
        return image;
    }
/*
    private static int Moodimage(String mood){
        switch(mood){
            case "happy" : return R.drawable.happy;
            case "mad" : return R.drawable.mad;
            case "sad" : return R.drawable.sad;
            case "love" : return R.drawable.love;
            case "tired" : return R.drawable.tired;
            case "heartbreak" : return R.drawable.heartbreak;

        }
        return 0;
    }
    private static String Moodcolor(String mood){
        switch(mood){
            case "happy" : return "0ED12E";
            case "mad" : return "B02525";
            case "sad" : return "897979";
            case "love" : return "F70707";
            case "tired" : return "3D5FAF";
            case "heartbreak" : return "46425E";

        }


        return null;
    }
    */

/*
    public EmotionalState(){
        EmotionalState Happy = new EmotionalState("happy");
        Happy.setColor("0ED12E");

        EmotionalState Mad = new EmotionalState("mad");
        Mad.setColor("B02525");

        EmotionalState Sad = new EmotionalState("sad");
        Sad.setColor("897979");

        EmotionalState Tired = new EmotionalState("tired");
        Tired.setColor("F70707");

        EmotionalState Love = new EmotionalState("love");
        Love.setColor("3D5FAF");

        EmotionalState Heartbreak = new EmotionalState("heartbreak");
        Heartbreak.setColor("46425E");


    EmotionalState[] moods = new EmotionalState[]{
            new EmotionalState("happy"),
            new EmotionalState("mad"),
            new EmotionalState("sad"),
            new EmotionalState("tired"),
            new EmotionalState("love"),
            new EmotionalState("heartbreak"),

    };
    }

*/





}