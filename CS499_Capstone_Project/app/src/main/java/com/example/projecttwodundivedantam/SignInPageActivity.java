package com.example.projecttwodundivedantam;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;

import java.util.ArrayList;

public class SignInPageActivity extends ComponentActivity {

    // In a full implementation this list would be persisted in the database.
    // For now we keep it in memory for the session.
    private ArrayList<String> signedInStudents = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_page);

        // ── Class info passed from ViewScheduleActivity ──────────────────────
        String classInfo = getIntent().getStringExtra("class_info");
        int    classId   = getIntent().getIntExtra("class_id", -1);

        TextView textClassDetail = findViewById(R.id.textClassDetail);
        textClassDetail.setText(classInfo != null ? classInfo : "Class details unavailable");

        // ── Signed-in list ───────────────────────────────────────────────────
        ListView listView     = findViewById(R.id.listSignedInStudents);
        TextView textNoSignIns = findViewById(R.id.textNoSignIns);

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                signedInStudents
        );
        listView.setAdapter(adapter);
        updateEmptyState(listView, textNoSignIns);

        // ── Sign In button ───────────────────────────────────────────────────
        Button btnSignIn = findViewById(R.id.btnSignInToClass);
        btnSignIn.setOnClickListener(v -> {
            // In a full implementation get the logged-in username from the session/intent.
            // Here we show a toast and add a placeholder name as a demo.
            String currentUser = getIntent().getStringExtra("username");
            if (currentUser == null || currentUser.isEmpty()) currentUser = "Student";

            if (signedInStudents.contains(currentUser)) {
                Toast.makeText(this, "You are already signed in.", Toast.LENGTH_SHORT).show();
            } else {
                signedInStudents.add(currentUser);
                adapter.notifyDataSetChanged();
                updateEmptyState(listView, textNoSignIns);
                Toast.makeText(this, "Signed in to class!", Toast.LENGTH_SHORT).show();
            }
        });

        // ── Back button ──────────────────────────────────────────────────────
        Button btnBack = findViewById(R.id.btnBackToSchedule);
        btnBack.setOnClickListener(v -> finish());
    }

    private void updateEmptyState(ListView listView, TextView textNoSignIns) {
        if (signedInStudents.isEmpty()) {
            textNoSignIns.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            textNoSignIns.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }
}