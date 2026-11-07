package com.example.printxpress.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.printxpress.database.DBConstants;
import com.example.printxpress.database.DatabaseHelper;
import com.example.printxpress.models.PrintProduct;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private DatabaseHelper dbHelper;

    public ProductDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public List<PrintProduct> getAllProducts() {
        List<PrintProduct> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBConstants.TABLE_PRODUCTS, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToProduct(cursor));
            }
            cursor.close();
        }
        db.close();
        return list;
    }

    public List<PrintProduct> getProductsByCategory(String category) {
        List<PrintProduct> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBConstants.TABLE_PRODUCTS, null,
                DBConstants.COL_PRODUCT_CATEGORY + "=?", new String[]{category}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToProduct(cursor));
            }
            cursor.close();
        }
        db.close();
        return list;
    }

    public PrintProduct getProductById(int productId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBConstants.TABLE_PRODUCTS, null,
                DBConstants.COL_PRODUCT_ID + "=?", new String[]{String.valueOf(productId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            PrintProduct p = cursorToProduct(cursor);
            cursor.close();
            db.close();
            return p;
        }
        if (cursor != null) cursor.close();
        db.close();
        return null;
    }

    private PrintProduct cursorToProduct(Cursor cursor) {
        PrintProduct p = new PrintProduct();
        p.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.COL_PRODUCT_ID)));
        p.setName(cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.COL_PRODUCT_NAME)));
        p.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.COL_PRODUCT_CATEGORY)));
        p.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.COL_PRODUCT_DESC)));
        p.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(DBConstants.COL_PRODUCT_PRICE)));
        return p;
    }
}
