package cmput301.moodi.Objects;

/*
 * Class: MoodiStorage
 * Implements methods for interacting with Firebase Firestore.
 * 11/04/2019
 */

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;

import static cmput301.moodi.util.Constants.POST_PATH;
import static cmput301.moodi.util.Constants.USER_PATH;
import static cmput301.moodi.util.Constants.FOLLOWERS_PATH;
import static cmput301.moodi.util.Constants.FOLLOWING_PATH;
import static cmput301.moodi.util.Constants.NOTIFICATIONS_PATH;

public class MoodiStorage {
    private FirebaseFirestore db;
    private static final String TAG = "moodiStorage";
    private CollectionReference postCollection;
    private CollectionReference userCollection;
    private CollectionReference followingCollection;
    private CollectionReference followerCollection;
    private CollectionReference notificationsCollection;

    private String UID;

    public MoodiStorage() {
            this.db = FirebaseFirestore.getInstance();
            FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
            this.UID = mFirebaseAuth.getCurrentUser().getUid();

//        userCollection.whereEqualTo("UID", UID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    // user found
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        name = (String) document.getData().get("Username");
//
//                    }
//                } else {
//                    Log.d(TAG, "Error getting username: ", task.getException());
//                }
//            }
//        });
//
//        this.username = name;
        this.postCollection = this.db.collection(POST_PATH);
        this.userCollection = this.db.collection(USER_PATH);
        this.followerCollection = this.userCollection.document(this.UID).collection(FOLLOWERS_PATH);
        this.followingCollection = this.userCollection.document(this.UID).collection(FOLLOWING_PATH);
        this.notificationsCollection = this.userCollection.document(this.UID).collection(NOTIFICATIONS_PATH);
    }

    /*
     * Returns the users UID associated with Firestore.
     */
    public String getUserUID() {
        return this.UID;
    }

    /*
     * Returns the followers documents for the user.
     */
    public Task getFollowers() {
        return this.followerCollection.get();
    }

    /*
     * Returns the followers documents for the user.
     */
    public Task getFollowing() {
        return this.followingCollection.get();
    }

    public Task getNotifications() {
        return this.notificationsCollection.get();
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

    /*
     * Returns all moods of those users followed
     */
    public Query getFollowingMoods() {
        // TODO later... fuc dat
        return null;
    }
}
