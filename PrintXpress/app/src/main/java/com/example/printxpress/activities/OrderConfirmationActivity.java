package com.example.printxpress.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.printxpress.R;
import com.example.printxpress.dao.OrderDAO;
import com.example.printxpress.models.Order;
import java.util.Locale;

public class OrderConfirmationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Order Confirmed");

        int orderId = getIntent().getIntExtra("order_id", -1);
        OrderDAO orderDAO = new OrderDAO(this);
        Order order = orderDAO.getOrderById(orderId);

        if (order != null) {
            ((TextView) findViewById(R.id.tvOrderId)).setText(getString(R.string.label_order_number, order.getOrderId()));
            ((TextView) findViewById(R.id.tvProduct)).setText(order.getProductName());
            ((TextView) findViewById(R.id.tvTotal)).setText(getString(R.string.label_rs_amount,
                    String.format(Locale.getDefault(), "%.2f", order.getTotalPrice())));
            ((TextView) findViewById(R.id.tvDelivery)).setText(getString(R.string.label_delivery, order.getDeliveryType()));
            ((TextView) findViewById(R.id.tvStatus)).setText(getString(R.string.label_status, order.getStatus()));
        }

        findViewById(R.id.btnViewOrders).setOnClickListener(v -> {
            startActivity(new Intent(this, MyOrdersActivity.class));
            finish();
        });

        findViewById(R.id.btnGoHome).setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });
    }
}
