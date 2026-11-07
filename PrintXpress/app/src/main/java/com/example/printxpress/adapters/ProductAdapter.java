package com.example.printxpress.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.printxpress.R;
import com.example.printxpress.models.PrintProduct;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import android.widget.ImageView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    public interface OnProductClickListener {
        void onProductClick(PrintProduct product);
    }

    private List<PrintProduct> products;
    private OnProductClickListener listener;

    public static final Map<String, Integer> PRODUCT_IMAGES = new HashMap<>();
    static {
        PRODUCT_IMAGES.put("Business Cards", R.drawable.product_business_cards);
        PRODUCT_IMAGES.put("A4 Flyers", R.drawable.product_a4_flyers);
        PRODUCT_IMAGES.put("Posters (A3)", R.drawable.product_posters_a3);
        PRODUCT_IMAGES.put("T-Shirts", R.drawable.product_tshirts);
        PRODUCT_IMAGES.put("Banners (3x6 ft)", R.drawable.product_banners);
        PRODUCT_IMAGES.put("Stickers", R.drawable.product_stickers);
        PRODUCT_IMAGES.put("Mugs", R.drawable.product_mugs);
        PRODUCT_IMAGES.put("Envelopes", R.drawable.product_envelopes);
    }

    public ProductAdapter(List<PrintProduct> products, OnProductClickListener listener) {
        this.products = products;
        this.listener = listener;
    }

    public void updateData(List<PrintProduct> newProducts) {
        this.products = newProducts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PrintProduct product = products.get(position);
        holder.tvName.setText(product.getName());
        holder.tvCategory.setText(product.getCategory());
        holder.tvPrice.setText("Rs. " + String.format("%.2f", product.getPrice()));

        // Load product-specific image based on product name, fallback to gallery icon
        Integer imageRes = PRODUCT_IMAGES.get(product.getName());
        int resolvedImage = imageRes != null ? imageRes : android.R.drawable.ic_menu_gallery;

        Glide.with(holder.itemView.getContext())
                .load(resolvedImage)
                .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(16)))
                .into(holder.ivProduct);

        holder.itemView.setOnClickListener(v -> listener.onProductClick(product));
    }

    @Override
    public int getItemCount() { return products.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCategory, tvPrice;
        ImageView ivProduct;
        ViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvProductName);
            tvCategory = view.findViewById(R.id.tvCategory);
            tvPrice = view.findViewById(R.id.tvPrice);
            ivProduct = view.findViewById(R.id.ivProduct);
        }
    }
}
