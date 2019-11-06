package cmput301.moodi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateAccount extends AppCompatActivity {

    // Variables that are pointed to login activity to retrieve data
    Button CreateNewAccountButton;
    Button CancelButton;
    EditText newUserID;
    EditText newUserPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        configureCreateNewAccountButton();
        configureCancelButton();
    }

    private void configureCreateNewAccountButton() {
        // Pointing variables to the buttons and text
        CreateNewAccountButton = findViewById(R.id.new_account_Button);
        newUserID = findViewById(R.id.new_UserID_text);
        newUserPassword = findViewById(R.id.new_Password_text);

        // Set onclick listener for creation of new account
        CreateNewAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add firebase and authentication along with the conditionals here

                // Goes back to login activity where user will sign in with new account
                finish();
            }
        });
    }

    // Go back to main login screen
    private void configureCancelButton() {
        // Pointing variables to the buttons and text
        CancelButton = findViewById(R.id.cancel_Button);

        // Go back to login activity
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
