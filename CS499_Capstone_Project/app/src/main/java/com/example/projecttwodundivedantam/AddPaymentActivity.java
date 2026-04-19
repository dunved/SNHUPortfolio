package com.example.projecttwodundivedantam;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.ComponentActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class AddPaymentActivity extends ComponentActivity {

    private SharedPreferences prefs;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);

        username = getIntent().getStringExtra("username");
        if (username == null) username = "user";
        prefs = getSharedPreferences("AnacondaPrefs", MODE_PRIVATE);

        EditText editName     = findViewById(R.id.editNameOnCard);
        EditText editNumber   = findViewById(R.id.editCardNumber);
        EditText editExpiry   = findViewById(R.id.editExpiration);
        EditText editCvv      = findViewById(R.id.editSecurityCode);
        Button   btnSave      = findViewById(R.id.btnSaveCard);
        Button   btnBack      = findViewById(R.id.btnBackFromAddPayment);

        btnSave.setOnClickListener(v -> {
            String name   = editName.getText().toString().trim();
            String number = editNumber.getText().toString().trim();
            String expiry = editExpiry.getText().toString().trim();
            String cvv    = editCvv.getText().toString().trim();

            if (name.isEmpty() || number.isEmpty() || expiry.isEmpty() || cvv.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (number.length() < 4) {
                Toast.makeText(this, "Invalid card number.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Store only last 4 digits for display; never store full card data in plain text in production
            String last4 = number.substring(number.length() - 4);

            try {
                String existing = prefs.getString("cards_" + username, "[]");
                JSONArray cards = new JSONArray(existing);
                JSONObject card = new JSONObject();
                card.put("name", name);
                card.put("last4", last4);
                card.put("expiry", expiry);
                cards.put(card);
                prefs.edit().putString("cards_" + username, cards.toString()).apply();
                Toast.makeText(this, "Card ending in " + last4 + " saved.", Toast.LENGTH_SHORT).show();
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Error saving card.", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }
}