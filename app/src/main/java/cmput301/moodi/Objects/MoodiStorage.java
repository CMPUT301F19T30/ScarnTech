package cmput301.moodi.Objects;

/*
 * Class: MoodiStorage
 *
 * Implements methods for interacting with Firebase Firestore.
 *
 */

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MoodiStorage {
    private FirebaseFirestore db;
    private static final String TAG = "moodiStorage";
    private static String POST_PATH = "posts";
    private CollectionReference postCollection;
    private static String USER_PATH = "users";
    private CollectionReference userCollection;
    private String UID;

    public MoodiStorage() {
        this.db = FirebaseFirestore.getInstance();
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        this.UID = mFirebaseAuth.getCurrentUser().getUid();
        this.postCollection = this.db.collection(POST_PATH);
        this.userCollection = this.db.collection(USER_PATH);
    }

    /*
     * Returns the users UID associated with Firestore.
     */
    public String getUserUID() {
        return this.UID;
    }

    /*
     * Creates a new user profile in Firestore.
     */
    public Task createNewUserProfile(HashMap<String, Object> preferences) {
        return this.userCollection.document(this.UID).set(preferences);
    }


    /*
     * Returns all of the users own posts.
     */
    public void getUserPosts() {
        //Todo: return users 'moodhistory'.

    }


    /*
     * Creates a post object and adds it to Firebase post collection.
     *
     */
    public Task addMoodPost(Mood mood) {
        HashMap<String, Object> postData = mood.serializeMood();
        postData.put("UID", this.UID);
        return this.postCollection.add(postData);
    }


    /*
     * Deletes a post from Firebase.
     *
     */
    public void deletePost(String postID) {
        //Todo: implement post deletion from firebase.
    }

    /*
     * Returns user profile data. Need to complete onCompleteListeners to retrieve data.
     */
    public DocumentReference getUserProfile() {
        return this.userCollection.document(this.UID);
    }
}
