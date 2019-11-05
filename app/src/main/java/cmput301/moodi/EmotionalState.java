package cmput301.moodi;

public class EmotionalState extends Mood {
    private String EmotionalName;
    private String Color;
    // Add image variable as well as getter/setter

    public String getEmotionalName() {
        return EmotionalName;
    }

    public void setEmotionalName(String emotionalName) {
        EmotionalName = emotionalName;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }
}
