package com.example.projecttwodundivedantam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.ComponentActivity;

public class ViewProfileActivity extends ComponentActivity {

    private SharedPreferences prefs;
    private String username;
    private ImageView imgProfilePicture;

    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    imgProfilePicture.setImageURI(uri);
                    imgProfilePicture.setPadding(0, 0, 0, 0);
                    imgProfilePicture.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
                    prefs.edit().putString("profile_pic_" + username, uri.toString()).apply();
                    try {
                        getContentResolver().takePersistableUriPermission(
                                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } catch (Exception ignored) {}
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        username = getIntent().getStringExtra("username");
        if (username == null) username = "user";

        prefs = getSharedPreferences("AnacondaPrefs", MODE_PRIVATE);

        imgProfilePicture = findViewById(R.id.imgProfilePicture);
        TextView textName       = findViewById(R.id.textProfileName);
        TextView textDateJoined = findViewById(R.id.textDateJoined);
        TextView textBeltRank   = findViewById(R.id.textBeltRank);
        TextView textMemberType = findViewById(R.id.textMembershipType);
        TextView textRenewal    = findViewById(R.id.textMembershipRenewal);

        // Load saved profile picture
        String savedPicUri = prefs.getString("profile_pic_" + username, null);
        if (savedPicUri != null) {
            imgProfilePicture.setImageURI(Uri.parse(savedPicUri));
            imgProfilePicture.setPadding(0, 0, 0, 0);
            imgProfilePicture.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
        }

        imgProfilePicture.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        textName.setText(prefs.getString("display_name_" + username, username));
        textDateJoined.setText("Date Joined: " + prefs.getString("date_joined_" + username, "Not set"));
        textBeltRank.setText("Belt Rank: " + prefs.getString("belt_rank_" + username, "White Belt"));
        textMemberType.setText("Type: " + prefs.getString("membership_" + username, "None"));
        textRenewal.setText("Renews: " + prefs.getString("renewal_date_" + username, "—"));

        // Edit Membership
        Button btnEditMembership = findViewById(R.id.btnEditMembership);
        btnEditMembership.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditMembershipActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        // Add Payment Method
        Button btnAddPayment = findViewById(R.id.btnAddPayment);
        btnAddPayment.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddPaymentActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        // Edit Payment Methods
        Button btnEditPayment = findViewById(R.id.btnEditPayment);
        btnEditPayment.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditPaymentActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        // Back
        Button btnBack = findViewById(R.id.btnBackFromProfile);
        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView textMemberType = findViewById(R.id.textMembershipType);
        TextView textRenewal    = findViewById(R.id.textMembershipRenewal);
        textMemberType.setText("Type: " + prefs.getString("membership_" + username, "None"));
        textRenewal.setText("Renews: " + prefs.getString("renewal_date_" + username, "—"));
    }
}