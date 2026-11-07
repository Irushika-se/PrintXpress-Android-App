package com.example.printxpress.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.printxpress.database.DBConstants;
import com.example.printxpress.database.DatabaseHelper;
import com.example.printxpress.models.User;

public class UserDAO {
    private DatabaseHelper dbHelper;

    public UserDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long registerUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_USER_NAME, user.getName());
        values.put(DBConstants.COL_USER_EMAIL, user.getEmail());
        values.put(DBConstants.COL_USER_PHONE, user.getPhone());
        values.put(DBConstants.COL_USER_PASSWORD, user.getPassword());
        values.put(DBConstants.COL_USER_ADDRESS, user.getAddress());
        long result = db.insert(DBConstants.TABLE_USERS, null, values);
        db.close();
        return result;
    }

    public User loginUser(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBConstants.TABLE_USERS, null,
                DBConstants.COL_USER_EMAIL + "=? AND " + DBConstants.COL_USER_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = cursorToUser(cursor);
            cursor.close();
            db.close();
            return user;
        }
        if (cursor != null) cursor.close();
        db.close();
        return null;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBConstants.TABLE_USERS, null,
                DBConstants.COL_USER_EMAIL + "=?", new String[]{email}, null, null, null);
        boolean exists = cursor != null && cursor.getCount() > 0;
        if (cursor != null) cursor.close();
        db.close();
        return exists;
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBConstants.TABLE_USERS, null,
                DBConstants.COL_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = cursorToUser(cursor);
            cursor.close();
            db.close();
            return user;
        }
        if (cursor != null) cursor.close();
        db.close();
        return null;
    }

    public boolean updateUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_USER_NAME, user.getName());
        values.put(DBConstants.COL_USER_PHONE, user.getPhone());
        values.put(DBConstants.COL_USER_ADDRESS, user.getAddress());
        int rows = db.update(DBConstants.TABLE_USERS, values,
                DBConstants.COL_USER_ID + "=?", new String[]{String.valueOf(user.getUserId())});
        db.close();
        return rows > 0;
    }

    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.COL_USER_ID)));
        user.setName(cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.COL_USER_NAME)));
        user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.COL_USER_EMAIL)));
        user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.COL_USER_PHONE)));
        user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.COL_USER_PASSWORD)));
        user.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.COL_USER_ADDRESS)));
        return user;
    }
}
