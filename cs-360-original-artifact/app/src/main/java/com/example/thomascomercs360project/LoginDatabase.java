package com.example.thomascomercs360project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LoginDatabase extends SQLiteOpenHelper {

    public static final String DBNAME = "Login.db";

    public LoginDatabase(Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase myDb) {
        myDb.execSQL("create Table users(username TEXT primary key, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase myDb, int i, int i1) {
        myDb.execSQL("drop Table if exists users");
    }

    // Insert data into the table.
    public Boolean insertData(String username, String password) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = myDb.insert("users", null, contentValues);

        if (result == -1) return false;
        else return true;
    }

    // Check for the username in the database.
    public Boolean checkUsername(String username) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("Select * from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0) return true;
        else return false;
    }

    // Check for the password.
    public Boolean checkUsernamePassword(String username, String password) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("Select * from users where username = ? and password = ?", new String[]{username, password});
        if (cursor.getCount() > 0) return true;
        else return false;
    }
}
