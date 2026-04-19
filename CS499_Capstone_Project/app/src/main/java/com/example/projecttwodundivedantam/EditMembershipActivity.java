package com.example.projecttwodundivedantam;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditMembershipActivity extends ComponentActivity {

    private SharedPreferences prefs;
    private String username;

    private RadioButton radioStriking, radioGrappling, radioUltimate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_membership);

        username = getIntent().getStringExtra("username");
        if (username == null) username = "user";

        prefs = getSharedPreferences("AnacondaPrefs", MODE_PRIVATE);

        TextView textName           = findViewById(R.id.textEditMemberName);
        TextView textCurrentType    = findViewById(R.id.textCurrentMembership);
        TextView textCurrentRenewal = findViewById(R.id.textCurrentRenewal);

        radioStriking  = findViewById(R.id.radioStriking);
        radioGrappling = findViewById(R.id.radioGrappling);
        radioUltimate  = findViewById(R.id.radioUltimate);

        // ── Populate header ───────────────────────────────────────────────
        String displayName = prefs.getString("display_name_" + username, username);
        textName.setText(displayName);

        // ── Show current membership ───────────────────────────────────────
        String currentMembership = prefs.getString("membership_" + username, "None");
        String currentRenewal    = prefs.getString("renewal_date_" + username, "—");
        textCurrentType.setText("Type: " + currentMembership);
        textCurrentRenewal.setText("Renews: " + currentRenewal);

        // Pre-select current membership radio button
        switch (currentMembership) {
            case "Striking Membership":  radioStriking.setChecked(true);  break;
            case "Grappling Membership": radioGrappling.setChecked(true); break;
            case "Ultimate Membership":  radioUltimate.setChecked(true);  break;
        }

        // ── Tapping cards selects the radio button ────────────────────────
        LinearLayout cardStriking  = findViewById(R.id.cardStriking);
        LinearLayout cardGrappling = findViewById(R.id.cardGrappling);
        LinearLayout cardUltimate  = findViewById(R.id.cardUltimate);

        cardStriking.setOnClickListener(v  -> selectMembership("striking"));
        cardGrappling.setOnClickListener(v -> selectMembership("grappling"));
        cardUltimate.setOnClickListener(v  -> selectMembership("ultimate"));
        radioStriking.setOnClickListener(v  -> selectMembership("striking"));
        radioGrappling.setOnClickListener(v -> selectMembership("grappling"));
        radioUltimate.setOnClickListener(v  -> selectMembership("ultimate"));

        // ── Save button ───────────────────────────────────────────────────
        Button btnSave = findViewById(R.id.btnSaveMembership);
        btnSave.setOnClickListener(v -> {
            String selected = getSelectedMembership();
            if (selected == null) {
                Toast.makeText(this, "Please select a membership.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Calculate renewal date (30 days from today)
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 30);
            String renewalDate = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
                    .format(cal.getTime());

            prefs.edit()
                    .putString("membership_" + username, selected)
                    .putString("renewal_date_" + username, renewalDate)
                    .apply();

            Toast.makeText(this,
                    "Membership updated to " + selected, Toast.LENGTH_SHORT).show();

            // Update the display
            textCurrentType.setText("Type: " + selected);
            textCurrentRenewal.setText("Renews: " + renewalDate);
        });

        // ── Cancel Membership button ──────────────────────────────────────
        Button btnCancel = findViewById(R.id.btnCancelMembership);
        btnCancel.setOnClickListener(v -> {
            // Send cancellation request (in a full system this would notify admin)
            prefs.edit()
                    .putString("membership_" + username, "Cancellation Requested")
                    .putString("renewal_date_" + username, "—")
                    .apply();

            radioStriking.setChecked(false);
            radioGrappling.setChecked(false);
            radioUltimate.setChecked(false);

            textCurrentType.setText("Type: Cancellation Requested");
            textCurrentRenewal.setText("Renews: —");

            Toast.makeText(this,
                    "Cancellation request sent to admin.", Toast.LENGTH_LONG).show();
        });

        // ── Back button ───────────────────────────────────────────────────
        Button btnBack = findViewById(R.id.btnBackFromMembership);
        btnBack.setOnClickListener(v -> finish());
    }

    private void selectMembership(String type) {
        radioStriking.setChecked(false);
        radioGrappling.setChecked(false);
        radioUltimate.setChecked(false);
        switch (type) {
            case "striking":  radioStriking.setChecked(true);  break;
            case "grappling": radioGrappling.setChecked(true); break;
            case "ultimate":  radioUltimate.setChecked(true);  break;
        }
    }

    private String getSelectedMembership() {
        if (radioStriking.isChecked())  return "Striking Membership";
        if (radioGrappling.isChecked()) return "Grappling Membership";
        if (radioUltimate.isChecked())  return "Ultimate Membership";
        return null;
    }
}