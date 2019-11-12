//package cmput301.moodi.Ui.LoggedIn.profile;
//
//import android.os.Bundle;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//
//import cmput301.moodi.Objects.MoodiStorage;
//import cmput301.moodi.Objects.User;
//
///*
// * Display profile information and details.
// * Display all of users post history.
// */
//
//public class ProfileAcitivty extends AppCompatActivity {
//    MoodiStorage moodiStorage;
//    private static final String TAG = "ProfileActivity";
//    User userProfile;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //setContentView(R.layout.fragment_profile);
//
//        moodiStorage = new MoodiStorage();
//
//        //Todo: get user profile data and display it on page.
//        final DocumentReference userProfile = moodiStorage.getUserProfile();
//        userProfile.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                        //Todo: parse data to display on profile page.
//                        //userProfile = new User();
//                        //userProfile.setupProfile(document.getData());
//                    } else {
//                        Log.d(TAG, "No such document");
//                    }
//                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
//                }
//            }
//        });
//
//        //Todo: get mood history of profile.
//    }
//
//
//}
