package cmput301.moodi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccount extends AppCompatActivity {

    // create account page objects
    Button button_signup, button_goto_login;
    EditText emailId, password;

    // firebase auth link
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // link to firebase authentication
        mFirebaseAuth = FirebaseAuth.getInstance();

        // create all references to the layout
        emailId = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        button_signup = findViewById(R.id.signup_button);
        button_goto_login = findViewById(R.id.goto_login);


        // when a user clicks create account, the following logic checks if account creation works
        // with the given inputs
        button_signup.setOnClickListener(new View.OnClickListener() {
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

                } else if (!email.isEmpty() && !pass.isEmpty()) {
                    // both values are given, try to create the account
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                // account was not able to be created
                                Toast.makeText(CreateAccount.this, "Account creation was not successful, try again.", Toast.LENGTH_SHORT).show();

                            } else {
                                // account was created, user is taken to the home screen
                                Toast.makeText(CreateAccount.this, "Account creation was successful! Welcome to Moodi!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(CreateAccount.this, HomeActivity.class);
                                startActivity(i);
                            }
                        }
                    });

                } else {
                    // This should never happen logically. If it does the world might be imploding
                    Toast.makeText(CreateAccount.this, "Unknown error.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // The user wishes to back out of the account creation and see the log in screen again
        button_goto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateAccount.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

}