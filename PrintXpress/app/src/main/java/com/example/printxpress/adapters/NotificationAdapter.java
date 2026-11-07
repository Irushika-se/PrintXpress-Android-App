package com.example.printxpress.adapters;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.printxpress.R;
import com.example.printxpress.models.Notification;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<Notification> notifications;

    public NotificationAdapter(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification n = notifications.get(position);
        holder.tvTitle.setText(n.getTitle());
        holder.tvMessage.setText(n.getMessage());
        holder.tvDate.setText(n.getDate());

        if (!n.isRead()) {
            holder.tvTitle.setTypeface(null, Typeface.BOLD);
        }

        // Set icon tint color based on notification type
        String title = n.getTitle();
        if (title.contains("Placed")) {
            holder.ivIcon.setColorFilter(Color.parseColor("#388E3C")); // green
        } else if (title.contains("Processing")) {
            holder.ivIcon.setColorFilter(Color.parseColor("#FF6D00")); // orange
        } else if (title.contains("Ready")) {
            holder.ivIcon.setColorFilter(Color.parseColor("#388E3C")); // green
        } else if (title.contains("Cancelled")) {
            holder.ivIcon.setColorFilter(Color.parseColor("#B71C1C")); // red
        } else if (title.contains("Rescheduled")) {
            holder.ivIcon.setColorFilter(Color.parseColor("#FF6D00")); // orange
        } else {
            holder.ivIcon.setColorFilter(Color.parseColor("#607D8B")); // grey default
        }
    }

    @Override
    public int getItemCount() { return notifications.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvMessage, tvDate;
        ImageView ivIcon;
        ViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvMessage = view.findViewById(R.id.tvMessage);
            tvDate = view.findViewById(R.id.tvDate);
            ivIcon = view.findViewById(R.id.ivNotifIcon);
        }
    }
}
