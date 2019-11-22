package cmput301.moodi.ui.createAccount;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;

import cmput301.moodi.Objects.MoodiStorage;
import cmput301.moodi.ui.createAccount.GoogleAccountActivity;
import cmput301.moodi.ui.loggedIn.BottomNavigationActivity;
import cmput301.moodi.R;

import cmput301.moodi.ui.createAccount.CreateAccountActivity;

import cmput301.moodi.R;
import cmput301.moodi.ui.login.LoginActivity;



public class GoogleAccountActivity extends AppCompatActivity {

    int RC_SIGN_IN = 9001;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    MoodiStorage moodiStorage;
    GeoPoint lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_loggin);

        //Initializing Views
        signInButton = findViewById(R.id.google_signup);
        lastLocation = null;

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();



        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            //Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            //GoogleSignInAccount account = task.getResult(ApiException.class);
            //firebaseAuthWithGoogle(account);



            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                handleSignInResult(account);
            } catch (ApiException e) {
                Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());

            }
        }
    }

    private void handleSignInResult(GoogleSignInAccount acct){

            //GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            //GoogleSignInAccount acct = completeTask.getResult(ApiException.class);

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        //mAuth.id
        final String fname = acct.getGivenName();
        final String lname = acct.getFamilyName();
        final String username = acct.getDisplayName();


        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    moodiStorage = new MoodiStorage();
                    moodiStorage.createNewUserProfile(getUserPreferences(fname ,lname,username));

                    //FirebaseUser user = mAuth.getCurrentUser();



                }
            });

            // Signed in successfully, show authenticated UI.
            //startActivity(new Intent(this, BottomNavigationActivity.class));

    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private HashMap<String, Object> getUserPreferences(String f,String l,String u ) {
        HashMap<String, Object> preferences = new HashMap<>();
        preferences.put("first_name",   f);
        preferences.put("last_name",    l);
        preferences.put("username",     u);
        preferences.put("lastLocation", this.lastLocation);
        return preferences;
    }

    @Override
    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //if(currentUser != null) {
          //  startActivity(new Intent(GoogleAccountActivity.this, BottomNavigationActivity.class));
        //}

    }
}