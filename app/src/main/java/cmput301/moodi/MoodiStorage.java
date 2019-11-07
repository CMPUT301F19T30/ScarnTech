package cmput301.moodi;

/*
 * Class: MoodiStorage
 *
 * Implements methods for interacting with Firebase Firestore.
 *
 */

import com.google.android.gms.tasks.Task;
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
    private int userID;

    public MoodiStorage() {
        //Todo: set user id based off of login information.
        this.userID = 12356123;
        this.db = FirebaseFirestore.getInstance();
        this.postCollection = this.db.collection(POST_PATH);
        this.userCollection = this.db.collection(USER_PATH);
    }


    /*
     * Returns all of the users own posts.
     */
    public void getUserPosts() {


    }


    /*
     * Creates a post object and adds it to Firebase post collection.
     *
     */
    public Task addMoodPost(Mood mood) {
        HashMap<String, Object> postData = mood.serializeMood();
        postData.put("userID", this.userID);
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
        //Todo: get user profile based off of login information. Maybe used saved preferences file.
        return this.userCollection.document("steven-lagrange");
    }
}
