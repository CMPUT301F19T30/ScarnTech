package cmput301.moodi.ui.createAccount;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

import cmput301.moodi.Objects.MoodiStorage;
import cmput301.moodi.R;
import cmput301.moodi.ui.loggedIn.BottomNavigationActivity;

import static cmput301.moodi.util.Constants.USER_PATH;

/*
 * Handles account creation.
 */
//Todo: ensure username is unique when creating account
public class CreateAccountActivity extends AppCompatActivity {
    private static final String TAG = "CreateAccountActivity";

    // Create account page objects
    Button button_signup;
    EditText emailField, passwordField;
    EditText usernameField, firstNameField, lastNameField;
    GeoPoint lastLocation;
    TextView loginTextView;
    public String username, email, password;
    public boolean isUsernameValid;

    // Firebase auth link
    FirebaseAuth mFirebaseAuth;
    MoodiStorage moodiStorage;
    FirebaseFirestore db;
    CollectionReference userCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // link to firebase authentication
        mFirebaseAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        this.userCollection = this.db.collection(USER_PATH);


        // create all references to the layout
        emailField = findViewById(R.id.signup_email);
        passwordField = findViewById(R.id.signup_password);
        button_signup = findViewById(R.id.signup_button);
        loginTextView = findViewById(R.id.login_text);

        // User preferences fields
        firstNameField = findViewById(R.id.signup_firstName);
        lastNameField = findViewById(R.id.signup_lastName);
        usernameField = findViewById(R.id.signup_username);


        lastLocation = null;

        // when a user clicks create account, the following logic checks if account creation works
        // with the given inputs
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailField.getText().toString();
                password = passwordField.getText().toString();

                if (!isUsernameValid()) {
                    Toast.makeText(CreateAccountActivity.this, "Username is not valid. Please fix errors to create account.", Toast.LENGTH_SHORT).show();
                } else if (!isValidFirstName() || !isValidLastName()) {
                    Toast.makeText(CreateAccountActivity.this, "First name or last name is not valid. Please fix errors to create account.", Toast.LENGTH_SHORT).show();
                } else if (!isValidPassword() || !isValidEmail()) {
                    Toast.makeText(CreateAccountActivity.this, "Password or email is not valid. Please fix errors to create account.", Toast.LENGTH_SHORT).show();
                } else if (!email.isEmpty() && !password.isEmpty()) {
                    checkUniqueUsername();
                }
            }
        });

        // The user wishes to back out of the account creation and see the log in screen again
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(CreateAccountActivity.this, LoginActivity.class);
                //startActivity(i);
                CreateAccountActivity.super.onBackPressed();
            }
        });
    }

    /*
     * Return the profile preferences and fields from account creation.
     */
    private HashMap<String, Object> getUserPreferences() {
        HashMap<String, Object> preferences = new HashMap<>();
        preferences.put("first_name",   this.firstNameField.getText().toString());
        preferences.put("last_name",    this.lastNameField.getText().toString());
        preferences.put("username",     this.usernameField.getText().toString());
        preferences.put("lastLocation", this.lastLocation);
        return preferences;
    }


    /*
     * Validate user input for first name field.
     * Requirements are:
     * - Max Length: <20
     * - Min Length: >1
     */
    private boolean isValidFirstName() {
        String firstName = this.firstNameField.getText().toString();
        int minLength = 1;
        int maxLength = 20;
        Log.d(TAG, "isValidFirstName: " + firstName);
        Log.d(TAG, "isValidFirstName: " + firstName.length());

        if(firstName.length() >= minLength && firstName.length() <= maxLength) { return true; }

        this.firstNameField.setError("Must be between 1 and 20 characters.");
        this.firstNameField.requestFocus();
        return false;
    }

    /*
     * Validate user input for last name field.
     * Requirements are:
     * - Max Length: <20
     * - Min Length: >1
     */
    private boolean isValidLastName() {
        String lastName = this.lastNameField.getText().toString();
        int minLength = 1;
        int maxLength = 20;

        if(lastName.length() >= minLength && lastName.length() <= maxLength) { return true; }

        this.lastNameField.setError("Must be between 1 and 20 characters.");
        this.lastNameField.requestFocus();
        return false;
    }

    /*
     * Check the database for any other occurence of the username.
     */
    private void checkUniqueUsername() {
        final int minLength = 5;
        final int maxLength = 20;
        username = this.usernameField.getText().toString();


        userCollection.whereEqualTo("username", username).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                isUsernameValid = task.getResult().isEmpty();

                if (!isUsernameValid) {
                    usernameField.setError("Sorry, that username is already taken. Please choose something else.");
                    usernameField.requestFocus();
                } else {

                    createMoodiAccount();
                }
            }
        });
    }

    /*
     * Validate user input for username field.
     * Requirements are:
     * - Unique username in DB
     * - Max Length: <=20
     * - Min Length: >=5
     */
    private boolean isUsernameValid() {
        final int minLength = 5;
        final int maxLength = 20;
        username = this.usernameField.getText().toString();

        if(username.length() >= minLength && username.length() <= maxLength) {
            return true;
        }

        this.usernameField.setError("Must be between " + minLength + " and " + maxLength + " characters.");
        this.usernameField.requestFocus();
        return false;
    }

    /*
     * Validate user input for email field.
     * Requirements:
     * - Min Length: >0
     * - Max Length: <=40
     */
    private boolean isValidEmail() {
        String email = this.emailField.getText().toString();
        int maxLength = 40;

        if(!email.isEmpty() && email.length() <= maxLength ) { return true; }

        emailField.setError("Please enter a valid email address.");
        emailField.requestFocus();
        return false;
    }

    /*
     * Validate user input for password field.
     */
    private boolean isValidPassword() {
        String password = this.passwordField.getText().toString();
        int maxLength = 20;

        if(!password.isEmpty() && password.length() <= maxLength ) { return true; }

        passwordField.setError("Please enter a valid password.");
        passwordField.requestFocus();
        return false;
    }


    /*
     * Create the user account for moodi, redirect to home screen of app.
     */
    public void createMoodiAccount() {
        email = emailField.getText().toString();
        password = passwordField.getText().toString();

        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    // account was not able to be created
                    Toast.makeText(CreateAccountActivity.this, "Account creation was not successful, try again.", Toast.LENGTH_SHORT).show();

                } else {
                    // account was created, user is taken to the home screen
                    Toast.makeText(CreateAccountActivity.this, "Account creation was successful! Welcome to Moodi!", Toast.LENGTH_SHORT).show();

                    // Retrieve preferences and save in Firestore.
                    moodiStorage = new MoodiStorage();
                    moodiStorage.createNewUserProfile(getUserPreferences());

                    // Start new activity.
                    Intent i = new Intent(CreateAccountActivity.this, BottomNavigationActivity.class);
                    i.putExtra("finish", true); // if you are checking for this in your other Activities
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

}
