package com.example.printxpress.database;

public class DBConstants {
    // Database
    public static final String DB_NAME = "printxpress.db";
    public static final int DB_VERSION = 3;

    // Users table
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_USER_NAME = "name";
    public static final String COL_USER_EMAIL = "email";
    public static final String COL_USER_PHONE = "phone";
    public static final String COL_USER_PASSWORD = "password";
    public static final String COL_USER_ADDRESS = "address";

    // Products table
    public static final String TABLE_PRODUCTS = "products";
    public static final String COL_PRODUCT_ID = "product_id";
    public static final String COL_PRODUCT_NAME = "product_name";
    public static final String COL_PRODUCT_CATEGORY = "category";
    public static final String COL_PRODUCT_DESC = "description";
    public static final String COL_PRODUCT_PRICE = "price";
    public static final String COL_PRODUCT_IMAGE = "image_res";

    // Orders table
    public static final String TABLE_ORDERS = "orders";
    public static final String COL_ORDER_ID = "order_id";
    public static final String COL_ORDER_USER_ID = "user_id";
    public static final String COL_ORDER_PRODUCT_ID = "product_id";
    public static final String COL_ORDER_PRODUCT_NAME = "product_name";
    public static final String COL_ORDER_QTY = "quantity";
    public static final String COL_ORDER_TOTAL = "total_price";
    public static final String COL_ORDER_STATUS = "status";
    public static final String COL_ORDER_DATE = "order_date";
    public static final String COL_ORDER_SPECS = "specifications";
    public static final String COL_ORDER_DELIVERY = "delivery_type";
    public static final String COL_ORDER_DELIVERY_DATE = "delivery_date";

    // Payments table
    public static final String TABLE_PAYMENTS = "payments";
    public static final String COL_PAYMENT_ID = "payment_id";
    public static final String COL_PAYMENT_ORDER_ID = "order_id";
    public static final String COL_PAYMENT_METHOD = "method";
    public static final String COL_PAYMENT_AMOUNT = "amount";
    public static final String COL_PAYMENT_STATUS = "status";
    public static final String COL_PAYMENT_DATE = "payment_date";

    // Notifications table
    public static final String TABLE_NOTIFICATIONS = "notifications";
    public static final String COL_NOTIF_ID = "notif_id";
    public static final String COL_NOTIF_USER_ID = "user_id";
    public static final String COL_NOTIF_TITLE = "title";
    public static final String COL_NOTIF_MESSAGE = "message";
    public static final String COL_NOTIF_DATE = "notif_date";
    public static final String COL_NOTIF_READ = "is_read";
}
