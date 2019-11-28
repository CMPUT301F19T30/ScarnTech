package cmput301.moodi.Objects;

import org.junit.Test;

import cmput301.moodi.R;

import static org.junit.Assert.*;

public class EmotionalStateTest {


    @Test
    public void getColor() {
        EmotionalState emotionalState = new EmotionalState();
        emotionalState.setColor(R.color.Happy);
        int HappyColor = R.color.Happy;
        assertEquals(HappyColor,emotionalState.getColor());
    }

    @Test
    public void getIndex() {
        EmotionalState emotionalState = new EmotionalState();
        emotionalState.setName("Happy");
        assertEquals(0,emotionalState.getIndex());
    }



    @Test
    public void getName() {
        EmotionalState emotionalState = new EmotionalState();
        emotionalState.setName("Happy");
        assertSame("Happy",emotionalState.getName());
    }


    @Test
    public void getEmoji() {
        EmotionalState emotionalState = new EmotionalState();
        emotionalState.setEmoji(R.drawable.happy);
        int i = R.drawable.happy;
        assertEquals(i,emotionalState.getEmoji());
    }
}