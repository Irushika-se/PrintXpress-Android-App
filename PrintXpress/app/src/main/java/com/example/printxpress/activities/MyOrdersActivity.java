package com.example.printxpress.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.printxpress.R;
import com.example.printxpress.adapters.OrderAdapter;
import com.example.printxpress.dao.OrderDAO;
import com.example.printxpress.models.Order;
import com.example.printxpress.utils.SessionManager;
import java.util.List;

public class MyOrdersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private View emptyContainer;
    private SessionManager session;
    private OrderDAO orderDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle("My Orders");

        recyclerView = findViewById(R.id.recyclerOrders);
        emptyContainer = findViewById(R.id.tvEmptyContainer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        session = new SessionManager(this);
        orderDAO = new OrderDAO(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }

    private void loadOrders() {
        List<Order> orders = orderDAO.getOrdersByUser(session.getUserId());

        if (orders.isEmpty()) {
            emptyContainer.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyContainer.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            OrderAdapter adapter = new OrderAdapter(orders, order -> {
                Intent intent = new Intent(this, OrderDetailActivity.class);
                intent.putExtra("order_id", order.getOrderId());
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
        }
    }
}
