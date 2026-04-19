package com.example.projecttwodundivedantam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class GymDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "anaconda_app.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";

    public static final String TABLE_CLASSES = "classes";
    public static final String COL_CLASS_ID = "id";
    public static final String COL_CLASS_NAME = "name";
    public static final String COL_INSTRUCTOR = "instructor";
    public static final String COL_TIME = "time";
    public static final String COL_MAT = "mat";

    public GymDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_USERS + "(" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_CLASSES + "(" +
                COL_CLASS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CLASS_NAME + " TEXT, " +
                COL_INSTRUCTOR + " TEXT, " +
                COL_TIME + " TEXT, " +
                COL_MAT + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASSES);
        onCreate(db);
    }

    // ---------- LOGIN METHODS ----------

    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COL_USER_ID},
                COL_USERNAME + " = ? AND " + COL_PASSWORD + " = ?",
                new String[]{username, password},
                null,
                null,
                null
        );

        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    // ---------- CLASS CRUD METHODS ----------

    public long addClass(String name, String instructor, String time, String mat) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_CLASS_NAME, name);
        values.put(COL_INSTRUCTOR, instructor);
        values.put(COL_TIME, time);
        values.put(COL_MAT, mat);

        return db.insert(TABLE_CLASSES, null, values);
    }

    public int updateClass(long id, String name, String instructor, String time, String mat) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_CLASS_NAME, name);
        values.put(COL_INSTRUCTOR, instructor);
        values.put(COL_TIME, time);
        values.put(COL_MAT, mat);

        return db.update(TABLE_CLASSES, values, COL_CLASS_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int deleteClass(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_CLASSES, COL_CLASS_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public List<GymClass> getAllClasses() {
        SQLiteDatabase db = getReadableDatabase();
        List<GymClass> list = new ArrayList<>();

        Cursor cursor = db.query(
                TABLE_CLASSES,
                null,
                null,
                null,
                null,
                null,
                COL_TIME + " ASC"
        );

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_CLASS_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_CLASS_NAME));
            String instructor = cursor.getString(cursor.getColumnIndexOrThrow(COL_INSTRUCTOR));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(COL_TIME));
            String mat = cursor.getString(cursor.getColumnIndexOrThrow(COL_MAT));

            list.add(new GymClass(id, name, instructor, time, mat));
        }

        cursor.close();
        return list;
    }
}
