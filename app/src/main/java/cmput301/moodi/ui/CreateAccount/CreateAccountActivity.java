import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

import cmput301.moodi.Objects.MoodiStorage;
import cmput301.moodi.R;
import cmput301.moodi.ui.LoggedIn.BottomNavigationActivity;
import cmput301.moodi.ui.Login.LoginActivity;
/*
 * Handles account creation.
 */
//Todo: ensure username is unique when creating account
public class CreateAccountActivity extends AppCompatActivity {
    private static final String TAG = "CreateAccountActivity";

    // create account page objects
    Button button_signup, button_goto_login;
    EditText emailField, passwordField;
    EditText usernameField, firstNameField, lastNameField;

    // firebase auth link
    FirebaseAuth mFirebaseAuth;
    MoodiStorage moodiStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // link to firebase authentication
        mFirebaseAuth = FirebaseAuth.getInstance();

        // create all references to the layout
        emailField = findViewById(R.id.signup_email);
        passwordField = findViewById(R.id.signup_password);
        button_signup = findViewById(R.id.signup_button);
        button_goto_login = findViewById(R.id.goto_login);

        // User preferences fields
        firstNameField = findViewById(R.id.signup_firstName);
        lastNameField = findViewById(R.id.signup_lastName);
        usernameField = findViewById(R.id.signup_username);

        // when a user clicks create account, the following logic checks if account creation works
        // with the given inputs
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString();
                String pass = passwordField.getText().toString();

                if (!isValidFirstName() || !isValidLastName() || !isValidUsername()) {
                    Toast.makeText(CreateAccountActivity.this, "Please fix errors to create account.", Toast.LENGTH_SHORT).show();
                } else if (!isValidPassword() || !isValidEmail()) {
                    Toast.makeText(CreateAccountActivity.this, "Please fix password or email to create account.", Toast.LENGTH_SHORT).show();
                } else if (!email.isEmpty() && !pass.isEmpty()) {
                    // both values are given, try to create the account
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
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
        });

        // The user wishes to back out of the account creation and see the log in screen again
        button_goto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateAccountActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    /*
     * Return the profile preferences and fields from account creation.
     */
    private HashMap<String, Object> getUserPreferences() {
        HashMap<String, Object> preferences = new HashMap<>();
        preferences.put("first_name", this.firstNameField.getText().toString());
        preferences.put("last_name", this.lastNameField.getText().toString());
        preferences.put("username", this.usernameField.getText().toString());
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
     * Validate user input for username field.
     * Requirements are:
     * - Max Length: <=20
     * - Min Length: >=5
     */
    private boolean isValidUsername() {
        String username = this.usernameField.getText().toString();
        int minLength = 5;
        int maxLength = 20;

        if(username.length() >= minLength && username.length() <= maxLength) { return true; }

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
        //Todo: create validation rules for email input.
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
        //Todo: create validation rules for password input.
        String password = this.passwordField.getText().toString();
        int maxLength = 20;

        if(!password.isEmpty() && password.length() <= maxLength ) { return true; }

        passwordField.setError("Please enter a valid password.");
        passwordField.requestFocus();
        return false;
    }

}
