package com.example.printxpress.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.printxpress.R;
import java.util.Locale;

public class OrderSummaryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Order Summary");

        String productName = getIntent().getStringExtra("product_name");
        int qty = getIntent().getIntExtra("quantity", 1);
        double total = getIntent().getDoubleExtra("total", 0.0);
        int orderId = getIntent().getIntExtra("order_id", -1);
        String deliveryType = getIntent().getStringExtra("delivery_type");

        ((TextView) findViewById(R.id.tvProductName)).setText(getString(R.string.label_product, productName));
        ((TextView) findViewById(R.id.tvQuantity)).setText(getString(R.string.label_quantity, qty));
        ((TextView) findViewById(R.id.tvTotal)).setText(getString(R.string.label_total_rs,
                String.format(Locale.getDefault(), "%.2f", total)));

        findViewById(R.id.btnProceedPayment).setOnClickListener(v -> {
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra("amount", total);
            intent.putExtra("order_id", orderId);
            intent.putExtra("delivery_type", deliveryType);
            startActivity(intent);
        });
    }
}
