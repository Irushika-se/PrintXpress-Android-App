package com.example.printxpress.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.printxpress.R;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class DesignAdapter extends RecyclerView.Adapter<DesignAdapter.DesignViewHolder> {

    private List<String> designNames;

    private static final Map<String, Integer> DESIGN_IMAGES = new HashMap<>();
    static {
        DESIGN_IMAGES.put("Elegant Business Card", R.drawable.design_business_card);
        DESIGN_IMAGES.put("Promotional Agency Flyer", R.drawable.design_agency_flyer);
        DESIGN_IMAGES.put("Grand Event Banner", R.drawable.design_event_banner);
        DESIGN_IMAGES.put("Custom Printed T-Shirt", R.drawable.design_tshirt);
        DESIGN_IMAGES.put("Branded Menu Poster", R.drawable.design_restaurant_menu);
        DESIGN_IMAGES.put("Premium Logo Print", R.drawable.design_logo_print);
    }

    public DesignAdapter(List<String> designNames) {
        this.designNames = designNames;
    }

    @NonNull
    @Override
    public DesignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design, parent, false);
        return new DesignViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DesignViewHolder holder, int position) {
        String designName = designNames.get(position);
        holder.tvDesignName.setText(designName);

        // Load design-specific image based on name, fallback to gallery icon
        Integer imageRes = DESIGN_IMAGES.get(designName);
        int resolvedImage = imageRes != null ? imageRes : android.R.drawable.ic_menu_gallery;

        Glide.with(holder.itemView.getContext())
                .load(resolvedImage)
                .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(16)))
                .into(holder.ivDesign);
    }

    @Override
    public int getItemCount() {
        return designNames.size();
    }

    static class DesignViewHolder extends RecyclerView.ViewHolder {
        ImageView ivDesign;
        TextView tvDesignName;

        DesignViewHolder(View view) {
            super(view);
            ivDesign = view.findViewById(R.id.ivDesign);
            tvDesignName = view.findViewById(R.id.tvDesignName);
        }
    }
}
