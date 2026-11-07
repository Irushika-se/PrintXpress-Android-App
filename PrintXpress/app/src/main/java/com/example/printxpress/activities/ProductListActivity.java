package com.example.printxpress.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.printxpress.R;
import com.example.printxpress.adapters.ProductAdapter;
import com.example.printxpress.dao.ProductDAO;
import com.example.printxpress.models.PrintProduct;
import java.util.List;

import android.widget.Button;
import android.widget.LinearLayout;
import android.graphics.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ProductListActivity extends AppCompatActivity {
    private List<PrintProduct> allProducts;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Our Products");

        RecyclerView recyclerView = findViewById(R.id.recyclerProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ProductDAO productDAO = new ProductDAO(this);
        allProducts = productDAO.getAllProducts();

        adapter = new ProductAdapter(allProducts, product -> {
            Intent intent = new Intent(this, ProductDetailActivity.class);
            intent.putExtra("product_id", product.getProductId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        setupCategories();
    }

    private void setupCategories() {
        LinearLayout llCategories = findViewById(R.id.llCategories);
        Set<String> categories = new HashSet<>();
        categories.add("All");
        for (PrintProduct p : allProducts) {
            if (p.getCategory() != null && !p.getCategory().isEmpty()) {
                categories.add(p.getCategory());
            }
        }

        for (String cat : categories) {
            Button btn = new Button(this);
            btn.setText(cat);
            btn.setAllCaps(false);
            btn.setBackgroundColor(Color.parseColor("#E0E0E0"));
            btn.setTextColor(Color.BLACK);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 16, 0);
            btn.setLayoutParams(params);
            
            btn.setOnClickListener(v -> filterProducts(cat));
            llCategories.addView(btn);
        }
    }

    private void filterProducts(String category) {
        if (category.equals("All")) {
            adapter.updateData(allProducts);
            return;
        }
        List<PrintProduct> filtered = new ArrayList<>();
        for (PrintProduct p : allProducts) {
            if (category.equals(p.getCategory())) {
                filtered.add(p);
            }
        }
        adapter.updateData(filtered);
    }
}
