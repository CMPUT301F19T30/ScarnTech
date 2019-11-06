package cmput301.moodi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    // Variables that are pointed to login activity to retrieve data
    Button LoginButton;
    Button NewAccountButton;
    EditText UserID;
    EditText UserPassword;

    // Initialization of firebase
    FirebaseFirestore db;
    String TAG = "Sample";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // OnClick listeners
        configureLoginButton();
        configureNewAccountButton();

    }

    private void configureLoginButton() {
        // Pointing variables to the buttons and text
        LoginButton = findViewById(R.id.Login_Button);
        UserID = findViewById(R.id.UserID_text);
        UserPassword = findViewById(R.id.Password_text);

        // Adding an onClickListener to the login button awaiting a click.
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Retrieving the user name and the password from the EditText fields.
                final String userID = UserID.getText().toString();
                final String password = UserPassword.getText().toString();

                // We use a HashMap to store a key-value pair in firestore. Can you guess why? Because it's a No-SQL database.
                HashMap<String, String> data = new HashMap<>();

                // Access a Cloud Firestore instance from your Activity
                db = FirebaseFirestore.getInstance();

                // Get a top-level reference to the collection.
                final CollectionReference collectionReference = db.collection("users");

                // Checking if fields are empty
                if (userID.length() > 0 && password.length() > 0) {

                    // If there is some data in the EditText field, then we create a new key-value pair.
                    data.put("password", password);

                    // The set method sets a unique id for the document.
                    collectionReference
                            .document(userID)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // These are a method which gets executed when the task is successful.
                                    Log.d(TAG, "Data addition successful");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // This method gets executed if there is any problem.
                                    Log.d(TAG, "Data addition failed" + e.toString());
                                }
                            });

                    // Check for an existing ID
                    // If correct change activity to Activity main else reprompt login

                    // Setting the fields to null so the user can add a new city.
                    UserID.setText("");
                    UserPassword.setText("");
                }
            startActivity(new Intent(MainActivity.this, MoodsActivity.class ));
            }
        });

    }

    // OnClick listener for "Create New Account" button
    private void configureNewAccountButton() {
        // Pointing variable to the button
        NewAccountButton = findViewById(R.id.CreateAccount_Button);

        // On click, go to "Create Account Activity"
        NewAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateAccount.class ));
            }
        });
    }
}
