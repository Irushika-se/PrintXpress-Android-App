package com.example.printxpress.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.printxpress.database.DBConstants;
import com.example.printxpress.database.DatabaseHelper;
import com.example.printxpress.models.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private DatabaseHelper dbHelper;

    public OrderDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long placeOrder(Order order) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_ORDER_USER_ID, order.getUserId());
        values.put(DBConstants.COL_ORDER_PRODUCT_ID, order.getProductId());
        values.put(DBConstants.COL_ORDER_PRODUCT_NAME, order.getProductName());
        values.put(DBConstants.COL_ORDER_QTY, order.getQuantity());
        values.put(DBConstants.COL_ORDER_TOTAL, order.getTotalPrice());
        values.put(DBConstants.COL_ORDER_STATUS, order.getStatus());
        values.put(DBConstants.COL_ORDER_DATE, order.getOrderDate());
        values.put(DBConstants.COL_ORDER_SPECS, order.getSpecifications());
        values.put(DBConstants.COL_ORDER_DELIVERY, order.getDeliveryType());
        long id = db.insert(DBConstants.TABLE_ORDERS, null, values);
        db.close();
        return id;
    }

    public List<Order> getOrdersByUser(int userId) {
        List<Order> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBConstants.TABLE_ORDERS, null,
                DBConstants.COL_ORDER_USER_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null,
                DBConstants.COL_ORDER_ID + " DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToOrder(cursor));
            }
            cursor.close();
        }
        db.close();
        return list;
    }

    public Order getOrderById(int orderId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBConstants.TABLE_ORDERS, null,
                DBConstants.COL_ORDER_ID + "=?", new String[]{String.valueOf(orderId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Order o = cursorToOrder(cursor);
            cursor.close();
            db.close();
            return o;
        }
        if (cursor != null) cursor.close();
        db.close();
        return null;
    }

    public boolean markOrderAsProcessing(int orderId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_ORDER_STATUS, "Processing");
        int rows = db.update(DBConstants.TABLE_ORDERS, values,
                DBConstants.COL_ORDER_ID + "=? AND " + DBConstants.COL_ORDER_STATUS + "=?",
                new String[]{String.valueOf(orderId), "Payment Pending"});
        db.close();
        return rows > 0;
    }

    public boolean cancelOrder(int orderId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_ORDER_STATUS, "Cancelled");
        int rows = db.update(DBConstants.TABLE_ORDERS, values,
                DBConstants.COL_ORDER_ID + "=? AND " + DBConstants.COL_ORDER_STATUS + "=?",
                new String[]{String.valueOf(orderId), "Processing"});
        db.close();
        return rows > 0;
    }

    public boolean rescheduleOrder(int orderId, String newDeliveryDate) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_ORDER_DELIVERY_DATE, newDeliveryDate);
        int rows = db.update(DBConstants.TABLE_ORDERS, values,
                DBConstants.COL_ORDER_ID + "=? AND " + DBConstants.COL_ORDER_STATUS + "=?",
                new String[]{String.valueOf(orderId), "Processing"});
        db.close();
        return rows > 0;
    }

    private Order cursorToOrder(Cursor cursor) {
        Order o = new Order();
        o.setOrderId(cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.COL_ORDER_ID)));
        o.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.COL_ORDER_USER_ID)));
        o.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.COL_ORDER_PRODUCT_ID)));
        o.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.COL_ORDER_PRODUCT_NAME)));
        o.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.COL_ORDER_QTY)));
        o.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(DBConstants.COL_ORDER_TOTAL)));
        o.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.COL_ORDER_STATUS)));
        o.setOrderDate(cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.COL_ORDER_DATE)));
        o.setSpecifications(cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.COL_ORDER_SPECS)));
        o.setDeliveryType(cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.COL_ORDER_DELIVERY)));
        int deliveryDateIdx = cursor.getColumnIndex(DBConstants.COL_ORDER_DELIVERY_DATE);
        o.setDeliveryDate(deliveryDateIdx >= 0 ? cursor.getString(deliveryDateIdx) : null);
        return o;
    }
}
