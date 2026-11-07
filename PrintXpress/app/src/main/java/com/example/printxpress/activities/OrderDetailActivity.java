package com.example.printxpress.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.printxpress.R;
import com.example.printxpress.dao.NotificationDAO;
import com.example.printxpress.dao.OrderDAO;
import com.example.printxpress.models.Notification;
import com.example.printxpress.models.Order;
import com.example.printxpress.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Order Details");

        int orderId = getIntent().getIntExtra("order_id", -1);
        OrderDAO orderDAO = new OrderDAO(this);
        NotificationDAO notifDAO = new NotificationDAO(this);
        SessionManager session = new SessionManager(this);
        Order order = orderDAO.getOrderById(orderId);

        if (order == null) { finish(); return; }

        ((TextView) findViewById(R.id.tvOrderId)).setText(getString(R.string.label_order_number, order.getOrderId()));
        ((TextView) findViewById(R.id.tvProduct)).setText(getString(R.string.label_product, order.getProductName()));
        ((TextView) findViewById(R.id.tvQuantity)).setText(getString(R.string.label_quantity, order.getQuantity()));
        ((TextView) findViewById(R.id.tvTotal)).setText(getString(R.string.label_total_rs,
                String.format(Locale.getDefault(), "%.2f", order.getTotalPrice())));
        ((TextView) findViewById(R.id.tvStatus)).setText(getString(R.string.label_status, order.getStatus()));
        ((TextView) findViewById(R.id.tvDate)).setText(getString(R.string.label_date, order.getOrderDate()));
        ((TextView) findViewById(R.id.tvDelivery)).setText(getString(R.string.label_delivery, order.getDeliveryType()));

        TextView tvDeliveryDate = findViewById(R.id.tvDeliveryDate);
        if (order.getDeliveryDate() != null && !order.getDeliveryDate().isEmpty()) {
            tvDeliveryDate.setText(getString(R.string.label_scheduled_for, order.getDeliveryDate()));
        } else {
            tvDeliveryDate.setText(R.string.label_not_yet_scheduled);
        }

        ((TextView) findViewById(R.id.tvSpecs)).setText(getString(R.string.label_specifications,
                order.getSpecifications() != null ? order.getSpecifications() : getString(R.string.specifications_none)));

        Button btnReschedule = findViewById(R.id.btnRescheduleOrder);
        Button btnCancel = findViewById(R.id.btnCancelOrder);
        Button btnPayNow = findViewById(R.id.btnPayNow);

        if ("Payment Pending".equals(order.getStatus())) {
            btnPayNow.setVisibility(android.view.View.VISIBLE);
            btnPayNow.setOnClickListener(v -> {
                Intent intent = new Intent(this, PaymentActivity.class);
                intent.putExtra("amount", order.getTotalPrice());
                intent.putExtra("order_id", order.getOrderId());
                intent.putExtra("delivery_type", order.getDeliveryType());
                startActivity(intent);
                finish();
            });
        } else {
            btnPayNow.setVisibility(android.view.View.GONE);
        }

        if ("Processing".equals(order.getStatus())) {
            btnReschedule.setEnabled(true);
            btnReschedule.setOnClickListener(v -> {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePicker = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {
                        String newDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        boolean rescheduled = orderDAO.rescheduleOrder(orderId, newDate);
                        if (rescheduled) {
                            Toast.makeText(this, "Delivery rescheduled successfully", Toast.LENGTH_SHORT).show();
                            tvDeliveryDate.setText(getString(R.string.label_scheduled_for, newDate));
                            String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
                            String msg = "Order " + orderId + ": Your delivery has been rescheduled to " + newDate + ".";
                            notifDAO.addNotification(new Notification(session.getUserId(), "Order Rescheduled", msg, nowDate));
                        } else {
                            Toast.makeText(this, "Could not reschedule delivery", Toast.LENGTH_SHORT).show();
                        }
                    },
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                datePicker.show();
            });
        } else {
            btnReschedule.setEnabled(false);
            btnReschedule.setText(R.string.cannot_reschedule);
        }

        if ("Processing".equals(order.getStatus())) {
            btnCancel.setEnabled(true);
            btnCancel.setOnClickListener(v -> {
                boolean cancelled = orderDAO.cancelOrder(orderId);
                if (cancelled) {
                    Toast.makeText(this, "Order cancelled successfully", Toast.LENGTH_SHORT).show();
                    ((TextView) findViewById(R.id.tvStatus)).setText(R.string.status_cancelled);
                    btnCancel.setEnabled(false);
                    btnReschedule.setEnabled(false);
                    btnReschedule.setText(R.string.cannot_reschedule);
                    String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
                    String msg = "Order " + orderId + ": Your order for " + order.getProductName() + " has been cancelled.";
                    notifDAO.addNotification(new Notification(session.getUserId(), "Order Cancelled", msg, nowDate));
                } else {
                    Toast.makeText(this, "Could not cancel order", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            btnCancel.setEnabled(false);
            btnCancel.setText(R.string.cannot_cancel);
        }
    }
}
