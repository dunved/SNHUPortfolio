package com.example.projecttwodundivedantam;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;

import java.util.Collections;
import java.util.List;

public class ScheduleActivity extends ComponentActivity {

    private GymDatabaseHelper dbHelper;
    private GymClassAdapter adapter;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            finish(); // closes ScheduleActivity and returns to MainActivity
        });


        dbHelper = new GymDatabaseHelper(this);

        EditText editClassName = findViewById(R.id.editClassName);
        EditText editInstructor = findViewById(R.id.editInstructor);
        EditText editTime = findViewById(R.id.editTime);
        EditText editMat = findViewById(R.id.editMat);
        EditText editClassId = findViewById(R.id.editClassId);

        Button btnAdd = findViewById(R.id.btnAddClass);
        Button btnUpdate = findViewById(R.id.btnUpdateClass);
        Button btnDelete = findViewById(R.id.btnDeleteClass);
        Button btnLoad = findViewById(R.id.btnLoadClasses);

        gridView = findViewById(R.id.gridClasses);
        adapter = new GymClassAdapter(this, Collections.emptyList());
        gridView.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            String name = editClassName.getText().toString().trim();
            String instructor = editInstructor.getText().toString().trim();
            String time = editTime.getText().toString().trim();
            String mat = editMat.getText().toString().trim();

            if (name.isEmpty() || instructor.isEmpty() || time.isEmpty() || mat.isEmpty()) {
                Toast.makeText(this, "Fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            long id = dbHelper.addClass(name, instructor, time, mat);
            if (id != -1) {
                Toast.makeText(this, "Class added", Toast.LENGTH_SHORT).show();
                loadClasses();
            } else {
                Toast.makeText(this, "Error adding class", Toast.LENGTH_SHORT).show();
            }
        });

        btnUpdate.setOnClickListener(v -> {
            String idText = editClassId.getText().toString().trim();
            if (idText.isEmpty()) {
                Toast.makeText(this, "Enter a class ID", Toast.LENGTH_SHORT).show();
                return;
            }

            long id = Long.parseLong(idText);
            String name = editClassName.getText().toString().trim();
            String instructor = editInstructor.getText().toString().trim();
            String time = editTime.getText().toString().trim();
            String mat = editMat.getText().toString().trim();

            int rows = dbHelper.updateClass(id, name, instructor, time, mat);
            if (rows > 0) {
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                loadClasses();
            } else {
                Toast.makeText(this, "Class not found", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(v -> {
            String idText = editClassId.getText().toString().trim();
            if (idText.isEmpty()) {
                Toast.makeText(this, "Enter a class ID", Toast.LENGTH_SHORT).show();
                return;
            }

            long id = Long.parseLong(idText);
            int rows = dbHelper.deleteClass(id);

            if (rows > 0) {
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                loadClasses();
            } else {
                Toast.makeText(this, "Class not found", Toast.LENGTH_SHORT).show();
            }
        });

        btnLoad.setOnClickListener(v -> loadClasses());
        loadClasses();
    }

    private void loadClasses() {
        List<GymClass> list = dbHelper.getAllClasses();
        adapter.updateData(list);
    }
}
