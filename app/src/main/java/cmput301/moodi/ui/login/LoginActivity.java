package cmput301.moodi.ui.login;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;

import cmput301.moodi.ui.createAccount.GoogleAccountActivity;
import cmput301.moodi.ui.loggedIn.BottomNavigationActivity;
import cmput301.moodi.R;
import cmput301.moodi.ui.createAccount.CreateAccountActivity;

/**
 * Class: LoginActivity
 * Takes a user input and authenticates on app as well as firebase side to verify
 * @since 11/07/2019
 */

public class LoginActivity extends AppCompatActivity {

    // log in page objects
    Button button_login;
    EditText emailId, password;
    Button google_login;
    TextView signupTextView;

    // firebase auth link
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // link to firebase authentication
        mFirebaseAuth = FirebaseAuth.getInstance();

        // create all references to the layout
        emailId = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        button_login = findViewById(R.id.login_button);

        signupTextView = findViewById(R.id.signup_text);
        google_login = findViewById(R.id.button2);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    Toast.makeText(LoginActivity.this, "Log in Successful.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, BottomNavigationActivity.class);
                    startActivity(i);
                }
            }
        };


        // when a user clicks log in, the following logic checks if account is in the database
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pass = password.getText().toString();

                if (email.isEmpty()) {
                    // email text was left blank
                    emailId.setError("Please enter an email address.");
                    emailId.requestFocus();

                } else if (pass.isEmpty()) {
                    // password text was left blank
                    password.setError("Please enter a password.");
                    password.requestFocus();

                } else {
                    // both values are given, try to create the account
                    mFirebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Could not find account, try again or create a new account.", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent i = new Intent(LoginActivity.this, BottomNavigationActivity.class);
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

        // The user wishes create an account
        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                Intent i2 = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(i2);
            }
        });
        // The user wishes login in with google
        google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, GoogleAccountActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
    }
