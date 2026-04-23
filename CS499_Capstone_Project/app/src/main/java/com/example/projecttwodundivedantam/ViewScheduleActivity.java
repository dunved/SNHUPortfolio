package com.example.projecttwodundivedantam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.ComponentActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewScheduleActivity extends ComponentActivity {

    private GymDatabaseHelper dbHelper;
    private List<GymClass> classes;
    private LinearLayout containerScheduleClasses;
    private TextView textScheduleNoClasses;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);

        dbHelper = new GymDatabaseHelper(this);

        username = getIntent().getStringExtra("username");
        if (username == null) username = "Member";

        // ── Date in welcome box ──────────────────────────────────────────
        TextView textWelcome = findViewById(R.id.textScheduleWelcome);
        String today = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
                .format(new Date());
        textWelcome.setText("Welcome! Today is " + today);

        containerScheduleClasses = findViewById(R.id.containerScheduleClasses);
        textScheduleNoClasses    = findViewById(R.id.textScheduleNoClasses);

        // ── Admin: show Edit Classes button if needed ─────────────────────
        boolean isAdmin = getIntent().getBooleanExtra("is_admin", false);
        Button btnEditClasses = findViewById(R.id.btnEditClasses);
        if (isAdmin) {
            btnEditClasses.setVisibility(View.VISIBLE);
            btnEditClasses.setOnClickListener(v ->
                    startActivity(new Intent(this, ScheduleActivity.class)));
        }

        // ── Return to Main Menu ───────────────────────────────────────────
        Button btnReturn = findViewById(R.id.btnReturnToMain);
        btnReturn.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadClasses();
    }

    private void loadClasses() {
        containerScheduleClasses.removeAllViews();
        classes = dbHelper.getAllClasses();

        if (classes == null || classes.isEmpty()) {
            textScheduleNoClasses.setVisibility(View.VISIBLE);
            return;
        }

        textScheduleNoClasses.setVisibility(View.GONE);

        for (int i = 0; i < classes.size(); i++) {
            GymClass gc = classes.get(i);

            TextView tv = new TextView(this);
            tv.setText(gc.toString());
            tv.setTextColor(0xFFFFFFFF);
            tv.setTextSize(13);
            tv.setPadding(8, 16, 8, 16);
            tv.setClickable(true);
            tv.setFocusable(true);
            tv.setBackground(getDrawable(android.R.drawable.list_selector_background));

            tv.setOnClickListener(v -> {
                Intent intent = new Intent(this, SignInPageActivity.class);
                intent.putExtra("class_id",   (int) gc.getId());
                intent.putExtra("class_info", gc.toString());
                intent.putExtra("username",   username);
                startActivity(intent);
            });

            containerScheduleClasses.addView(tv);

            // Divider
            View divider = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 1);
            divider.setLayoutParams(params);
            divider.setBackgroundColor(0xFF2A2A2A);
            containerScheduleClasses.addView(divider);
        }
    }
}