package com.example.projecttwodundivedantam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewScheduleActivity extends ComponentActivity {

    private GymDatabaseHelper dbHelper;
    private List<GymClass> classes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);

        dbHelper = new GymDatabaseHelper(this);

        // ── Date in welcome box ──────────────────────────────────────────────
        TextView textWelcome = findViewById(R.id.textScheduleWelcome);
        String today = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
                .format(new Date());
        textWelcome.setText("Welcome! Today is " + today);

        // ── Class list ───────────────────────────────────────────────────────
        ListView listView       = findViewById(R.id.listScheduleClasses);
        TextView textNoClasses  = findViewById(R.id.textScheduleNoClasses);

        classes = dbHelper.getAllClasses();
        if (classes == null || classes.isEmpty()) {
            textNoClasses.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            ArrayList<String> classStrings = new ArrayList<>();
            for (GymClass gc : classes) {
                classStrings.add(gc.toString());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    classStrings
            );
            listView.setAdapter(adapter);
            listView.setVisibility(View.VISIBLE);
            textNoClasses.setVisibility(View.GONE);

            // Clicking a class opens the Sign In Page for that class
            listView.setOnItemClickListener((parent, view, position, id) -> {
                GymClass selected = classes.get(position);
                Intent intent = new Intent(this, SignInPageActivity.class);
                intent.putExtra("class_id",   selected.getId());
                intent.putExtra("class_info", selected.toString());
                startActivity(intent);
            });
        }

        // ── Admin: show Edit Classes button if user is admin ─────────────────
        // Pass "is_admin" boolean extra from MainActivity when needed
        boolean isAdmin = getIntent().getBooleanExtra("is_admin", false);
        Button btnEditClasses = findViewById(R.id.btnEditClasses);
        if (isAdmin) {
            btnEditClasses.setVisibility(View.VISIBLE);
            btnEditClasses.setOnClickListener(v -> {
                Intent intent = new Intent(this, ScheduleActivity.class);
                startActivity(intent);
            });
        }

        // ── Return to Main Menu ──────────────────────────────────────────────
        Button btnReturn = findViewById(R.id.btnReturnToMain);
        btnReturn.setOnClickListener(v -> finish());
    }
}