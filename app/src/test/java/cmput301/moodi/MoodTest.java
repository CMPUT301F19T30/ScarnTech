package cmput301.moodi;

import com.google.firebase.firestore.GeoPoint;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import cmput301.moodi.Objects.EmotionalState;
import cmput301.moodi.Objects.Location;
import cmput301.moodi.Objects.Mood;

import static org.junit.jupiter.api.Assertions.*;


class MoodTest {

    private Mood mockMood() {
        EmotionalState mockEmotion = mockEmotionalState();
        String mockReason = mockReason();
        String mockSocialSituation = "Out for drinks with friends!";
        GeoPoint mockLocation = new GeoPoint(53.123, 73.123);

        Mood mockMood = new Mood(mockEmotion, mockReason, mockSocialSituation, mockLocation);

        return mockMood;
    }

    private String mockReason() {
        return "I'm having a great day!";
    }

    private EmotionalState mockEmotionalState() {
        return new EmotionalState();
    }

    @Test
    void testGetEmotion() {
        Mood mockMood = mockMood();
        //assertEquals(mockEmotionalState(), mockMood.getEmotionalState());
    }

    @Test
    void testGetReason() {
        Mood mockMood = mockMood();
        assertEquals(mockReason(), mockMood.getReason());
    }


    @Test
    void testGetSocialSituation() {
        Mood mockMood = mockMood();
        String mockSocialSituation = "Out for drinks with friends!";
        assertEquals(mockSocialSituation, mockMood.getSocialSituation());
    }

}
