package com.example.projecttwodundivedantam;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class EditPaymentActivity extends ComponentActivity {

    private SharedPreferences prefs;
    private String username;
    private LinearLayout containerCards;
    private TextView textNoCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_payment);

        username = getIntent().getStringExtra("username");
        if (username == null) username = "user";
        prefs = getSharedPreferences("AnacondaPrefs", MODE_PRIVATE);

        containerCards = findViewById(R.id.containerCards);
        textNoCards    = findViewById(R.id.textNoCards);

        Button btnExit = findViewById(R.id.btnExitEditPayment);
        btnExit.setOnClickListener(v -> finish());

        loadCards();
    }

    private void loadCards() {
        containerCards.removeAllViews();
        try {
            String existing = prefs.getString("cards_" + username, "[]");
            JSONArray cards = new JSONArray(existing);

            if (cards.length() == 0) {
                textNoCards.setVisibility(View.VISIBLE);
                return;
            }
            textNoCards.setVisibility(View.GONE);

            for (int i = 0; i < cards.length(); i++) {
                JSONObject card = cards.getJSONObject(i);
                String last4  = card.getString("last4");
                String expiry = card.getString("expiry");
                final int index = i;

                // Card label
                LinearLayout cardRow = new LinearLayout(this);
                cardRow.setOrientation(LinearLayout.VERTICAL);
                cardRow.setBackgroundColor(0xFF1E1E1E);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 16);
                cardRow.setLayoutParams(params);
                cardRow.setPadding(32, 24, 32, 24);

                TextView cardLabel = new TextView(this);
                cardLabel.setText("Card ending in " + last4 + "  (Exp: " + expiry + ")");
                cardLabel.setTextColor(0xFFFFFFFF);
                cardLabel.setTextSize(15);
                cardRow.addView(cardLabel);

                // Button row
                LinearLayout btnRow = new LinearLayout(this);
                btnRow.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams btnRowParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                btnRowParams.setMargins(0, 12, 0, 0);
                btnRow.setLayoutParams(btnRowParams);

                Button btnRemove = new Button(this);
                btnRemove.setText("Remove");
                btnRemove.setBackgroundColor(0xFF8B0000);
                btnRemove.setTextColor(0xFFFFFFFF);
                btnRemove.setOnClickListener(v -> removeCard(index));

                btnRow.addView(btnRemove);
                cardRow.addView(btnRow);
                containerCards.addView(cardRow);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error loading cards.", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeCard(int index) {
        try {
            String existing = prefs.getString("cards_" + username, "[]");
            JSONArray cards = new JSONArray(existing);
            JSONArray updated = new JSONArray();
            for (int i = 0; i < cards.length(); i++) {
                if (i != index) updated.put(cards.get(i));
            }
            prefs.edit().putString("cards_" + username, updated.toString()).apply();
            Toast.makeText(this, "Card removed.", Toast.LENGTH_SHORT).show();
            loadCards();
        } catch (Exception e) {
            Toast.makeText(this, "Error removing card.", Toast.LENGTH_SHORT).show();
        }
    }
}