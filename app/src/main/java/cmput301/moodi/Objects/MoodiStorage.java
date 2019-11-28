package cmput301.moodi.Objects;

/*
 * Class: MoodiStorage
 * Implements methods for interacting with Firebase Firestore.
 * 11/04/2019
 */

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static cmput301.moodi.util.Constants.FOLLOWERS_PATH;
import static cmput301.moodi.util.Constants.NOTIFICATIONS_PATH;
import static cmput301.moodi.util.Constants.POST_PATH;
import static cmput301.moodi.util.Constants.USER_PATH;

public class MoodiStorage {
    private FirebaseFirestore db;
    private static final String TAG = "moodiStorage";
    private String UID;
    private CollectionReference postCollection;
    private CollectionReference userCollection;
    private CollectionReference notificationsCollection;
    private CollectionReference followerCollection;

    public MoodiStorage() {
        this.db = FirebaseFirestore.getInstance();
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        this.UID = Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid();
        this.postCollection = this.db.collection(POST_PATH);
        this.userCollection = this.db.collection(USER_PATH);
        this.notificationsCollection = this.db.collection(NOTIFICATIONS_PATH);
        this.followerCollection = this.db.collection(FOLLOWERS_PATH);
    }

    /*
     * Returns the users UID associated with Firestore.
     */
    public String getUID() {
        return this.UID;
    }

    /*
     * Return a users document reference.
     */
    public Task searchByUID(String UID) {
        return this.userCollection.document(UID).get();
    }

    /*
     * Returns all moodi users following the user.
     */
    public Task getFollowers() {
        return this.followerCollection.whereEqualTo("following", this.UID).get();
    }

    public void addFollower(Object data) {
        this.followerCollection.document().set(data);
    }

    /*
     * Returns all moodi users that I am following.
     */
    public Task getFollowing() {
        return this.followerCollection.whereEqualTo("user", this.UID).get();
    }


    public Task isUserFollowing(String UID) {
        return this.followerCollection.whereEqualTo("following", UID).get();
    }

    public Task getNotifications() {
        return this.notificationsCollection.whereEqualTo("receiver", this.UID).get();
    }

    public void deleteNotification(String notificationDoc) {
        this.notificationsCollection.document(notificationDoc).delete();
    }

    public void sendFollowRequest(Object data) {
        this.notificationsCollection.document().set(data);
    }

    public void createFollower(Object data) {
        this.followerCollection.document().set(data);
    }

    /*
     * Creates a new user profile in Firestore.
     */
    public Task createNewUserProfile(HashMap<String, Object> preferences) {
        return this.userCollection.document(this.UID).set(preferences);
    }

    /*
     * Returns all users of the application.
     */
    public Task getApplicationUsers() {
        return this.userCollection.get();
    }

    /*
     * Returns all of the users own posts.
     */
    public Task getUMoodHistory() {
        return this.postCollection.whereEqualTo("UID", this.UID).get();
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

    /*
     * Returns all moods posted by the user
     */
    public Task getUserMoods() {
        return this.postCollection.whereEqualTo("UID", UID).get();
    }

//    /*
//     * Returns all moods of those users followed
//     */
//    public Task getFollowingMoods() {
//    }

    // add the last given location to firebase
    public Task addLastLocation(GeoPoint location) {
        Map<String, Object> user_location = new HashMap<>();
        user_location.put("Location", location);
        return db.collection( "users" ).document(this.UID).collection( "LiveData" ).document("Location").set(user_location);
    }

    // retrieve last given location of the user
    public DocumentReference getLastLocation() {
        return db.collection( "users" ).document(this.UID).collection( "LiveData" ).document("Location");
    }

}
