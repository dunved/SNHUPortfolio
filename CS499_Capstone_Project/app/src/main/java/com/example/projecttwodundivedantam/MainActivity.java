package com.example.projecttwodundivedantam;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new GymDatabaseHelper(this);
        username = getIntent().getStringExtra("username");
        if (username == null) username = "user";

        TextView textWelcome = findViewById(R.id.textWelcome);
        String today = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
                .format(new Date());
        textWelcome.setText("Welcome! Today is " + today);

        ListView listViewClasses = findViewById(R.id.listViewClasses);
        TextView textNoClasses   = findViewById(R.id.textNoClasses);

        List<GymClass> classes = dbHelper.getAllClasses();
        if (classes == null || classes.isEmpty()) {
            textNoClasses.setVisibility(android.view.View.VISIBLE);
            listViewClasses.setVisibility(android.view.View.GONE);
        } else {
            ArrayList<String> classStrings = new ArrayList<>();
            for (GymClass gc : classes) classStrings.add(gc.toString());
            listViewClasses.setAdapter(new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, classStrings));
            listViewClasses.setVisibility(android.view.View.VISIBLE);
            textNoClasses.setVisibility(android.view.View.GONE);
        }

        Button btnEditClasses = findViewById(R.id.btnEditClasses);
        btnEditClasses.setOnClickListener(v ->
                startActivity(new Intent(this, ScheduleActivity.class)));

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

        navSchedule.setOnClickListener(v ->
                startActivity(new Intent(this, ViewScheduleActivity.class)));

        navRetail.setOnClickListener(v -> {
            Intent intent = new Intent(this, RetailActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        // Chat → ChatActivity
        navChat.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        // SmoothComp → open browser
        navSmoothComp.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://smoothcomp.com/en"));
            startActivity(intent);
        });
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