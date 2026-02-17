package com.example.thomascomercs360project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MainDatabase extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "my_library";
    private static final String ID_COLUMN = "_id";
    private static final String TITLE_COLUMN = "title";
    private static final String DESCRIPTION_COLUMN = "description";
    private static final String DATE_COLUMN = "date";
    private static final String TIME_COLUMN = "time";
    private static final String PERSISTENT_COLUMN = "persistent";
    private final Context context;
    private static final String DATABASE_NAME = "Event.db";
    private static final int DATABASE_VERSION = 3;

    MainDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TABLE_NAME +
                        " (" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TITLE_COLUMN + " TEXT, " +
                        DESCRIPTION_COLUMN + " TEXT, " +
                        DATE_COLUMN + " TEXT, " +
                        TIME_COLUMN + " TEXT, " +
                        PERSISTENT_COLUMN + " INTEGER DEFAULT 0);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + PERSISTENT_COLUMN + " INTEGER DEFAULT 0");
        }
    }

    Cursor readAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(query, null);
    }

    Cursor searchEvents(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + TITLE_COLUMN + " LIKE ? OR " + DESCRIPTION_COLUMN + " LIKE ?";
        return db.rawQuery(sql, new String[]{"%" + query + "%", "%" + query + "%"});
    }

    long addReminder(String date, String time, String title, String description, boolean persistent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE_COLUMN, date);
        contentValues.put(TIME_COLUMN, time);
        contentValues.put(TITLE_COLUMN, title);
        contentValues.put(DESCRIPTION_COLUMN, description);
        contentValues.put(PERSISTENT_COLUMN, persistent ? 1 : 0);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            Toast.makeText(context, "Failed to add", Toast.LENGTH_SHORT).show();
            return result;
        } else {
            Toast.makeText(context, "Event added", Toast.LENGTH_SHORT).show();
            return result;
        }
    }

    void updateData(String rowId, String title, String description, String date, String time, boolean persistent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE_COLUMN, title);
        contentValues.put(DESCRIPTION_COLUMN, description);
        contentValues.put(DATE_COLUMN, date);
        contentValues.put(TIME_COLUMN, time);
        contentValues.put(PERSISTENT_COLUMN, persistent ? 1 : 0);

        long result = db.update(TABLE_NAME, contentValues, "_id=?", new String[]{rowId});
        if (result == -1) {
            Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteOneRow(String row) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row});
        if (result == -1) {
            Toast.makeText(context, "Failed deletion", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();
        }
    }
}
