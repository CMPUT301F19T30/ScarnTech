package cmput301.moodi.Objects;

public class EmotionalState  {
    private String name;
    private String color;

    // Initializing our preset emotional states
    EmotionalState Happy;
    EmotionalState Heartbreak;
    EmotionalState Love;
    EmotionalState Mad;
    EmotionalState Sad;
    EmotionalState Tired;

    public EmotionalState() {
    }

    public EmotionalState(String name) {
        this.name = name;
    }

    public String getEmotionalName() {
        return name;
    }

    public void setEmotionalName(String emotionalName) {
        this.name = emotionalName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
