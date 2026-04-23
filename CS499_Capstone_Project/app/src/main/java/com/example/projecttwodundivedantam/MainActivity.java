package com.example.projecttwodundivedantam;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends ComponentActivity {

    private static final int REQ_SEND_SMS = 1001;
    private Button btnSmsReminder;
    private String reminderPhoneNumber = "5551234567";
    private String reminderMessage     = "Anaconda Gym: Remember your next class!";
    private GymDatabaseHelper dbHelper;
    private String username;
    private LinearLayout containerClasses;
    private TextView textNoClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new GymDatabaseHelper(this);
        username = getIntent().getStringExtra("username");
        if (username == null) username = "user";

        // ── Welcome text ─────────────────────────────────────────────────
        TextView textWelcome = findViewById(R.id.textWelcome);
        String today = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
                .format(new Date());
        textWelcome.setText("Welcome! Today is " + today);

        containerClasses = findViewById(R.id.containerClasses);
        textNoClasses    = findViewById(R.id.textNoClasses);

        // ── Edit Classes (Admin) ──────────────────────────────────────────
        Button btnEditClasses = findViewById(R.id.btnEditClasses);
        btnEditClasses.setOnClickListener(v ->
                startActivity(new Intent(this, ScheduleActivity.class)));

        // ── SMS Reminder ──────────────────────────────────────────────────
        btnSmsReminder = findViewById(R.id.btnSmsReminder);
        btnSmsReminder.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
                sendReminderSms();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS}, REQ_SEND_SMS);
            }
        });

        // ── Bottom Navigation ─────────────────────────────────────────────
        Button navProfile    = findViewById(R.id.navProfile);
        Button navSchedule   = findViewById(R.id.navSchedule);
        Button navRetail     = findViewById(R.id.navRetail);
        Button navChat       = findViewById(R.id.navChat);
        Button navSmoothComp = findViewById(R.id.navSmoothComp);

        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, ViewProfileActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        // Pass username so ViewScheduleActivity can pass it to SignInPageActivity
        navSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(this, ViewScheduleActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        navRetail.setOnClickListener(v -> {
            Intent intent = new Intent(this, RetailActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        navChat.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        navSmoothComp.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://smoothcomp.com/en"));
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadClasses();
    }

    private void loadClasses() {
        containerClasses.removeAllViews();
        List<GymClass> classes = dbHelper.getAllClasses();

        if (classes == null || classes.isEmpty()) {
            textNoClasses.setVisibility(android.view.View.VISIBLE);
        } else {
            textNoClasses.setVisibility(android.view.View.GONE);
            for (GymClass gc : classes) {
                TextView tv = new TextView(this);
                tv.setText(gc.toString());
                tv.setTextColor(0xFFFFFFFF);
                tv.setTextSize(13);
                tv.setPadding(8, 12, 8, 12);
                containerClasses.addView(tv);

                android.view.View divider = new android.view.View(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 1);
                divider.setLayoutParams(params);
                divider.setBackgroundColor(0xFF2A2A2A);
                containerClasses.addView(divider);
            }
        }
    }

    private void sendReminderSms() {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(reminderPhoneNumber, null, reminderMessage, null, null);
            Toast.makeText(this, "Reminder SMS sent", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_SEND_SMS && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendReminderSms();
        } else {
            Toast.makeText(this, "SMS permission denied. You can still use the app.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}