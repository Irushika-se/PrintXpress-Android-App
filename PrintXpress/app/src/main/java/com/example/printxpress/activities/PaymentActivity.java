package com.example.printxpress.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.printxpress.R;
import com.example.printxpress.dao.NotificationDAO;
import com.example.printxpress.dao.OrderDAO;
import com.example.printxpress.models.Notification;
import com.example.printxpress.utils.SessionManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {
    private RadioGroup rgPaymentMethod;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Payment");

        session = new SessionManager(this);
        rgPaymentMethod = findViewById(R.id.rgPaymentMethod);
        TextView tvAmount = findViewById(R.id.tvAmount);

        double amount = getIntent().getDoubleExtra("amount", 0.0);
        int orderId = getIntent().getIntExtra("order_id", -1);
        String deliveryType = getIntent().getStringExtra("delivery_type");
        tvAmount.setText(getString(R.string.label_amount_to_pay,
                String.format(Locale.getDefault(), "%.2f", amount)));

        RadioButton rbCash = findViewById(R.id.rbCashOnDeliveryPickup);
        if ("Home Delivery".equals(deliveryType)) {
            rbCash.setText(R.string.cash_on_delivery);
        } else if ("Pickup from Store".equals(deliveryType)) {
            rbCash.setText(R.string.cash_on_pickup);
        } else {
            rbCash.setText(R.string.cash_on_delivery_pickup);
        }

        findViewById(R.id.btnConfirmPayment).setOnClickListener(v -> {
            int selectedId = rgPaymentMethod.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton rb = findViewById(selectedId);
            String method = rb.getText().toString();
            boolean isCashMethod = (selectedId == R.id.rbCashOnDeliveryPickup);
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

            NotificationDAO notifDAO = new NotificationDAO(this);
            String notifTitle;
            String notifMsg;
            if (isCashMethod) {
                notifTitle = "Payment Pending";
                notifMsg = "Order " + orderId + ": Rs. " + String.format(Locale.getDefault(), "%.2f", amount)
                        + " to be collected via " + method + " upon "
                        + ("Home Delivery".equals(deliveryType) ? "delivery" : "pickup") + ".";
            } else {
                notifTitle = "Payment Confirmed";
                notifMsg = "Order " + orderId + ": Payment of Rs. " + String.format(Locale.getDefault(), "%.2f", amount)
                        + " via " + method + " received.";
            }
            notifDAO.addNotification(new Notification(session.getUserId(), notifTitle, notifMsg, date));

            OrderDAO orderDAO = new OrderDAO(this);
            orderDAO.markOrderAsProcessing(orderId);

            Toast.makeText(this, isCashMethod ? "Order confirmed, pay via " + method
                    : "Payment successful via " + method, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, OrderConfirmationActivity.class);
            intent.putExtra("order_id", orderId);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}
