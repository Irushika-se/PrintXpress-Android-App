package com.example.printxpress.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.printxpress.R;
import com.example.printxpress.adapters.ProductAdapter;
import com.example.printxpress.dao.ProductDAO;
import com.example.printxpress.models.PrintProduct;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        int productId = getIntent().getIntExtra("product_id", -1);
        ProductDAO productDAO = new ProductDAO(this);
        PrintProduct product = productDAO.getProductById(productId);

        if (product == null) { finish(); return; }

        if (getSupportActionBar() != null) getSupportActionBar().setTitle(product.getName());

        ((TextView) findViewById(R.id.tvProductName)).setText(product.getName());
        ((TextView) findViewById(R.id.tvCategory)).setText(getString(R.string.label_category, product.getCategory()));
        ((TextView) findViewById(R.id.tvDescription)).setText(product.getDescription());
        ((TextView) findViewById(R.id.tvPrice)).setText(getString(R.string.label_rs_amount,
                String.format(Locale.getDefault(), "%.2f", product.getPrice())));
        
        ImageView ivProductImage = findViewById(R.id.ivProductImage);
        Integer imageRes = ProductAdapter.PRODUCT_IMAGES.get(product.getName());
        int resolvedImage = imageRes != null ? imageRes : android.R.drawable.ic_menu_gallery;
        com.bumptech.glide.Glide.with(this)
            .load(resolvedImage)
            .into(ivProductImage);

        findViewById(R.id.btnOrderNow).setOnClickListener(v -> {
            Intent intent = new Intent(this, PlaceOrderActivity.class);
            intent.putExtra("product_id", productId);
            startActivity(intent);
        });
    }
}
