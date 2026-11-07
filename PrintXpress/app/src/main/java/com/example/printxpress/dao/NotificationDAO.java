package com.example.printxpress.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.printxpress.database.DBConstants;
import com.example.printxpress.database.DatabaseHelper;
import com.example.printxpress.models.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {
    private DatabaseHelper dbHelper;

    public NotificationDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long addNotification(Notification n) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_NOTIF_USER_ID, n.getUserId());
        values.put(DBConstants.COL_NOTIF_TITLE, n.getTitle());
        values.put(DBConstants.COL_NOTIF_MESSAGE, n.getMessage());
        values.put(DBConstants.COL_NOTIF_DATE, n.getDate());
        values.put(DBConstants.COL_NOTIF_READ, 0);
        long id = db.insert(DBConstants.TABLE_NOTIFICATIONS, null, values);
        db.close();
        return id;
    }

    public List<Notification> getNotificationsForUser(int userId) {
        List<Notification> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBConstants.TABLE_NOTIFICATIONS, null,
                DBConstants.COL_NOTIF_USER_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null,
                DBConstants.COL_NOTIF_ID + " DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Notification n = new Notification();
                n.setNotifId(cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.COL_NOTIF_ID)));
                n.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.COL_NOTIF_USER_ID)));
                n.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.COL_NOTIF_TITLE)));
                n.setMessage(cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.COL_NOTIF_MESSAGE)));
                n.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.COL_NOTIF_DATE)));
                n.setRead(cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.COL_NOTIF_READ)) == 1);
                list.add(n);
            }
            cursor.close();
        }
        db.close();
        return list;
    }

    public void markAllRead(int userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_NOTIF_READ, 1);
        db.update(DBConstants.TABLE_NOTIFICATIONS, values,
                DBConstants.COL_NOTIF_USER_ID + "=?", new String[]{String.valueOf(userId)});
        db.close();
    }
}
