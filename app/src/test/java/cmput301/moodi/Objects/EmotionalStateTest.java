package cmput301.moodi.Objects;

import org.junit.Test;

import cmput301.moodi.R;

import static org.junit.Assert.*;

/**
 * Class: EmotionalStateTest
 * this class is a junit test for Emotionalstate class;
 * Emotionalstate class use setName function to assign preset variable to the class
 * @since 27/11/2019
 */
public class EmotionalStateTest {


    @Test
    /*
    test getColor function whether can get correct color int
     */
    public void getColor() {
        EmotionalState emotionalState = new EmotionalState();
        emotionalState.setColor(R.color.Happy);
        int HappyColor = R.color.Happy;
        assertEquals(HappyColor,emotionalState.getColor());
    }

    @Test
    public void getIndex() {
        /*
        test getIndex function whether can get correct index of a emotional state
         */
        EmotionalState emotionalState = new EmotionalState();
        emotionalState.setName("Happy");
        assertEquals(0,emotionalState.getIndex());
    }



    @Test
    public void getName() {
        /*
        test getName/setName function whether can get or set correct Name of a emotional state
         */
        EmotionalState emotionalState = new EmotionalState();
        emotionalState.setName("Happy");
        assertSame("Happy",emotionalState.getName());
    }


    @Test
    public void getEmoji() {
        /*
        test getEmoji/setName function whether can get or set correct int of a picture
         */
        EmotionalState emotionalState = new EmotionalState();
        emotionalState.setEmoji(R.drawable.happy);
        int i = R.drawable.happy;
        assertEquals(i,emotionalState.getEmoji());
    }
}