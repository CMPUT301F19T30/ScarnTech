package cmput301.moodi.Objects;

public class EmotionalState  {
    private String name;
    private String color;

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