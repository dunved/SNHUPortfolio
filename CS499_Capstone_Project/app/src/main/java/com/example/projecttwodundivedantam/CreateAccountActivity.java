package com.example.projecttwodundivedantam;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.ComponentActivity;

public class CreateAccountActivity extends ComponentActivity {

    private GymDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        dbHelper = new GymDatabaseHelper(this);

        EditText editUsername = findViewById(R.id.editNewUsername);
        EditText editPhone    = findViewById(R.id.editPhone);
        EditText editEmail    = findViewById(R.id.editEmail);
        EditText editPassword = findViewById(R.id.editNewPassword);
        Button   btnCreate    = findViewById(R.id.btnCreateAccount);

        btnCreate.setOnClickListener(v -> {
            String username = editUsername.getText().toString().trim();
            String phone    = editPhone.getText().toString().trim();
            String email    = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            // Username and password are required
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this,
                        "Username and Password are required.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Attempt to register – GymDatabaseHelper.registerUser() already
            // returns false when the username is a duplicate.
            boolean success = dbHelper.registerUser(username, password);
            if (success) {
                Toast.makeText(this,
                        "Account created! You can now log in.",
                        Toast.LENGTH_SHORT).show();
                finish(); // return to login screen
            } else {
                Toast.makeText(this,
                        "Username, phone, or email already in use.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}