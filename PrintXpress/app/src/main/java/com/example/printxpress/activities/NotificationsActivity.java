package com.example.printxpress.activities;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.printxpress.R;
import com.example.printxpress.adapters.NotificationAdapter;
import com.example.printxpress.dao.NotificationDAO;
import com.example.printxpress.models.Notification;
import com.example.printxpress.utils.SessionManager;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Notifications");

        SessionManager session = new SessionManager(this);
        NotificationDAO notifDAO = new NotificationDAO(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerNotifications);
        View emptyContainer = findViewById(R.id.tvEmptyContainer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Notification> notifications = notifDAO.getNotificationsForUser(session.getUserId());
        notifDAO.markAllRead(session.getUserId());

        if (notifications.isEmpty()) {
            emptyContainer.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyContainer.setVisibility(View.GONE);
            recyclerView.setAdapter(new NotificationAdapter(notifications));
        }
    }
}
