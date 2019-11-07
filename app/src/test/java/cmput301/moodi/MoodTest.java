package cmput301.moodi;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class MoodTest {

    private Mood mockMood() {
        Image mockImage = new Image();
        EmotionalState mockEmotion = mockEmotionalState();
        String mockReason = mockReason();
        String mockSocialSituation = "Out for drinks with friends!";
        Location mockLocation = new Location(53.123, 73.123);

        Mood mockMood = new Mood(mockEmotion, mockReason, mockImage, mockSocialSituation, mockLocation);

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
    void testGetImageReason() {
        Mood mockMood = mockMood();
        Image mockImage = new Image();
        //assertEquals(mockImage, mockMood.getReasonImage());
    }

    @Test
    void testGetSocialSituation() {
        Mood mockMood = mockMood();
        String mockSocialSituation = "Out for drinks with friends!";
        assertEquals(mockSocialSituation, mockMood.getSocialSituation());
    }

}
