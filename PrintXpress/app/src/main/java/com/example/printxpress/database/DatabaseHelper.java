package com.example.printxpress.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, DBConstants.DB_NAME, null, DBConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Users table
        db.execSQL("CREATE TABLE " + DBConstants.TABLE_USERS + " (" +
                DBConstants.COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBConstants.COL_USER_NAME + " TEXT NOT NULL, " +
                DBConstants.COL_USER_EMAIL + " TEXT UNIQUE NOT NULL, " +
                DBConstants.COL_USER_PHONE + " TEXT, " +
                DBConstants.COL_USER_PASSWORD + " TEXT NOT NULL, " +
                DBConstants.COL_USER_ADDRESS + " TEXT)");

        // Products table
        db.execSQL("CREATE TABLE " + DBConstants.TABLE_PRODUCTS + " (" +
                DBConstants.COL_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBConstants.COL_PRODUCT_NAME + " TEXT NOT NULL, " +
                DBConstants.COL_PRODUCT_CATEGORY + " TEXT, " +
                DBConstants.COL_PRODUCT_DESC + " TEXT, " +
                DBConstants.COL_PRODUCT_PRICE + " REAL, " +
                DBConstants.COL_PRODUCT_IMAGE + " TEXT)");

        // Orders table
        db.execSQL("CREATE TABLE " + DBConstants.TABLE_ORDERS + " (" +
                DBConstants.COL_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBConstants.COL_ORDER_USER_ID + " INTEGER, " +
                DBConstants.COL_ORDER_PRODUCT_ID + " INTEGER, " +
                DBConstants.COL_ORDER_PRODUCT_NAME + " TEXT, " +
                DBConstants.COL_ORDER_QTY + " INTEGER, " +
                DBConstants.COL_ORDER_TOTAL + " REAL, " +
                DBConstants.COL_ORDER_STATUS + " TEXT DEFAULT 'Processing', " +
                DBConstants.COL_ORDER_DATE + " TEXT, " +
                DBConstants.COL_ORDER_SPECS + " TEXT, " +
                DBConstants.COL_ORDER_DELIVERY + " TEXT, " +
                DBConstants.COL_ORDER_DELIVERY_DATE + " TEXT)");

        // Payments table
        db.execSQL("CREATE TABLE " + DBConstants.TABLE_PAYMENTS + " (" +
                DBConstants.COL_PAYMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBConstants.COL_PAYMENT_ORDER_ID + " INTEGER, " +
                DBConstants.COL_PAYMENT_METHOD + " TEXT, " +
                DBConstants.COL_PAYMENT_AMOUNT + " REAL, " +
                DBConstants.COL_PAYMENT_STATUS + " TEXT, " +
                DBConstants.COL_PAYMENT_DATE + " TEXT)");

        // Notifications table
        db.execSQL("CREATE TABLE " + DBConstants.TABLE_NOTIFICATIONS + " (" +
                DBConstants.COL_NOTIF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBConstants.COL_NOTIF_USER_ID + " INTEGER, " +
                DBConstants.COL_NOTIF_TITLE + " TEXT, " +
                DBConstants.COL_NOTIF_MESSAGE + " TEXT, " +
                DBConstants.COL_NOTIF_DATE + " TEXT, " +
                DBConstants.COL_NOTIF_READ + " INTEGER DEFAULT 0)");

        // Seed sample products
        seedProducts(db);
    }

    private void seedProducts(SQLiteDatabase db) {
        String[][] products = {
            {"Business Cards", "Cards", "Premium quality business cards with glossy or matte finish. Available in various sizes.", "1500.00"},
            {"A4 Flyers", "Flyers", "Full-color A4 flyers, ideal for promotions and events. Single or double-sided.", "950.00"},
            {"Posters (A3)", "Posters", "Vibrant A3 posters for advertising and decoration. Weather-resistant options available.", "1800.00"},
            {"T-Shirts", "Merchandise", "Custom printed t-shirts with your design. Available in all sizes and colors.", "2200.00"},
            {"Banners (3x6 ft)", "Banners", "Large format vinyl banners perfect for events and storefronts.", "5500.00"},
            {"Stickers", "Stickers", "Custom die-cut or sheet stickers. Waterproof and UV resistant.", "750.00"},
            {"Mugs", "Merchandise", "Ceramic mugs with custom print. Dishwasher safe and microwave friendly.", "1600.00"},
            {"Envelopes", "Stationery", "Custom printed envelopes for professional correspondence.", "1100.00"}
        };
        for (String[] p : products) {
            db.execSQL("INSERT INTO " + DBConstants.TABLE_PRODUCTS +
                    " (product_name, category, description, price) VALUES ('" +
                    p[0] + "','" + p[1] + "','" + p[2] + "'," + p[3] + ")");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_PAYMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_NOTIFICATIONS);
        onCreate(db);
    }
}
