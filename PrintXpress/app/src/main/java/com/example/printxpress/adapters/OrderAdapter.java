package com.example.printxpress.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.printxpress.R;
import com.example.printxpress.models.Order;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    private List<Order> orders;
    private OnOrderClickListener listener;

    public OrderAdapter(List<Order> orders, OnOrderClickListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.tvOrderId.setText("Order " + order.getOrderId());
        holder.tvProduct.setText(order.getProductName());
        holder.tvTotal.setText("Rs. " + String.format("%.2f", order.getTotalPrice()));
        holder.tvStatus.setText(order.getStatus());
        holder.tvDate.setText(order.getOrderDate());

        if (order.getDeliveryDate() != null && !order.getDeliveryDate().isEmpty()) {
            holder.tvDeliveryDate.setText("Rescheduled to: " + order.getDeliveryDate());
            holder.tvDeliveryDate.setTextColor(Color.parseColor("#FF6D00"));
            holder.tvDeliveryDate.setVisibility(View.VISIBLE);
        } else {
            holder.tvDeliveryDate.setVisibility(View.GONE);
        }

        switch (order.getStatus()) {
            case "Payment Pending": holder.tvStatus.setTextColor(Color.parseColor("#EF6C00")); break;
            case "Processing": holder.tvStatus.setTextColor(Color.parseColor("#388E3C")); break;
            case "Printing":   holder.tvStatus.setTextColor(Color.parseColor("#00897B")); break;
            case "Ready":      holder.tvStatus.setTextColor(Color.parseColor("#2E7D32")); break;
            case "Cancelled":  holder.tvStatus.setTextColor(Color.parseColor("#B71C1C")); break;
            default:           holder.tvStatus.setTextColor(Color.GRAY);
        }

        holder.itemView.setOnClickListener(v -> listener.onOrderClick(order));
    }

    @Override
    public int getItemCount() { return orders.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvProduct, tvTotal, tvStatus, tvDate, tvDeliveryDate;
        ViewHolder(View view) {
            super(view);
            tvOrderId = view.findViewById(R.id.tvOrderId);
            tvProduct = view.findViewById(R.id.tvProduct);
            tvTotal = view.findViewById(R.id.tvTotal);
            tvStatus = view.findViewById(R.id.tvStatus);
            tvDate = view.findViewById(R.id.tvDate);
            tvDeliveryDate = view.findViewById(R.id.tvDeliveryDate);
        }
    }
}
