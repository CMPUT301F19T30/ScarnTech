package cmput301.moodi.Objects;

/**
 * Class: MoodiStorage
 * This is the class that upload user's new mood to firebase.
 * @since 11/04/2019
 */

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

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

    public CollectionReference getNotificationReference() {
        return this.notificationsCollection;
    }

    /*
     * Check if username exists in the database.
     */
    public Task isUsernameUnique(String username) {
        return this.userCollection.whereEqualTo("username", username).limit(1).get();
    }

    /*
     * Returns the users UID associated with Firestore.
    /**
     * @param uid
     * is user's id which is created along with account
     * @return the users UID associated with Firestore.
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
     * @return the followers documents for the user.
     */
    public Task getFollowers() {
        return this.followerCollection.whereEqualTo("following", this.UID).get();
    }

    /*
     * Returns all moodi users following the selected user (Overloaded Const.).
     * @return the followers documents for the user.
     */
    public Task getFollowers(String selectedUserUID) {
        return this.followerCollection.whereEqualTo("following", selectedUserUID).get();
    }

    /*
    * Add a follower to firebase
     */
    public void addFollower(Object data) {
        this.followerCollection.document().set(data);
    }

    /*
     * Returns all moodi users that I am following.
     * @return the following documents for the user.
     */
    public Task getFollowing() {
        return this.followerCollection.whereEqualTo("user", this.UID).get();
    }

    /*
     * Returns all moodi users of a selected user (Overloaded Constructor).
     * @return the following documents for the user.
     */
    public Task getFollowing(String selectedUserUID) {
        return this.followerCollection.whereEqualTo("user", selectedUserUID).get();
    }

    /*
    * @return the reference to which users are following the provided UID.
     */
    public Task isUserFollowing(String UID) {
        return this.followerCollection.whereEqualTo("following", UID).whereEqualTo("user", this.UID).limit(1).get();
    }

    /**
     * Get all current pending notifications.
     * @return the notification documents for the user.
     */
    public Task getNotifications() {
        return this.notificationsCollection.whereEqualTo("receiver", this.UID).get();
    }

    /*
     * Check to see if there is a notification pending in the database.
     */
    public Task isNotificationPending(String receiver) {
        return this.notificationsCollection.whereEqualTo("receiver", receiver).whereEqualTo("sender", this.UID).get();
    }

    public void deleteNotification(String notificationDoc) {
        this.notificationsCollection.document(notificationDoc).delete();
    }

    public void sendFollowRequest(Object data) {
        this.notificationsCollection.document().set(data);
    }

    /*
     * Create a follower.
     */
    public void createFollower(Object data) {
        this.followerCollection.document().set(data);
    }

    /*
    /**
     * Creates a new user profile in Firestore.
     */
    public Task createNewUserProfile(HashMap<String, Object> preferences) {
        return this.userCollection.document(this.UID).set(preferences);
    }

    /**
     * @return all users of the application.
     */
    public Task getApplicationUsers() {
        return this.userCollection.limit(500).get();
    }

    /**
     * @return all of the users own posts.
     */
    public Task getMyMoodHistory() {
        return this.postCollection.whereEqualTo("UID", this.UID).get();
    }

    /*
     * Returns all of the selected users own posts.
     */
    public Task getUserMoodHistory(String UID) {
        return this.postCollection.whereEqualTo("UID", UID).get();
    }

    /*
     * Returns the selected users task
     */
    public Task searchByUsername(String UID) {
        return this.userCollection.whereEqualTo("username", UID).get();
    }

    /**
     * Creates a post object and adds it to Firebase post collection.
     * @param mood
     * this is a object user created when they post a mood
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

    /**
     * @return user profile data. Need to complete onCompleteListeners to retrieve data.
     */
    public DocumentReference getUserProfile() {
        return this.userCollection.document(this.UID);
    }

    /*
     * Returns all moods posted by the current user
    /**
     * @return all moods posted by the user
     */
    public Task getUserMoods() {
        return this.postCollection.whereEqualTo("UID", UID).get();
    }

    /*
     * Returns all moods posted by a specific UID
    /**
     * @return all moods of those users followed
     */
    public Task getUserMoods(String otherUID) {
        return this.postCollection.whereEqualTo("UID", otherUID).get();
    }

//    /*
//     * Returns only the most recent mood posted by a specific UID
//     */
//    public Task getLastMood(String otherUID) {
//        Task task = this.getUserMoods(UID);
//
//        try {
//            Tasks.await(task);
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return this.postCollection.whereEqualTo("UID", otherUID).get();
//    }

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
