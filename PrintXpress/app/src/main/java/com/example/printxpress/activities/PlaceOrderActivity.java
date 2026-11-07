package com.example.printxpress.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.printxpress.R;
import com.example.printxpress.dao.OrderDAO;
import com.example.printxpress.dao.NotificationDAO;
import com.example.printxpress.dao.ProductDAO;
import com.example.printxpress.models.Notification;
import com.example.printxpress.models.Order;
import com.example.printxpress.models.PrintProduct;
import com.example.printxpress.utils.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import android.net.Uri;
import android.view.View;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PlaceOrderActivity extends AppCompatActivity {
    private EditText etQuantity, etSpecs;
    private RadioGroup rgDelivery;
    private TextView tvTotal, tvSampleDesignName;
    private ImageView ivSampleDesign;
    private PrintProduct product;
    private SessionManager session;
    private Uri sampleDesignUri = null;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Place Order");

        int productId = getIntent().getIntExtra("product_id", -1);
        ProductDAO productDAO = new ProductDAO(this);
        product = productDAO.getProductById(productId);
        session = new SessionManager(this);

        TextView tvProductName = findViewById(R.id.tvProductName);
        TextView tvUnitPrice = findViewById(R.id.tvUnitPrice);
        tvTotal = findViewById(R.id.tvTotal);
        etQuantity = findViewById(R.id.etQuantity);
        etSpecs = findViewById(R.id.etSpecs);
        rgDelivery = findViewById(R.id.rgDelivery);
        Button btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        ImageView ivProductImage = findViewById(R.id.ivProductImage);
        Button btnUploadDesign = findViewById(R.id.btnUploadDesign);
        ivSampleDesign = findViewById(R.id.ivSampleDesign);
        tvSampleDesignName = findViewById(R.id.tvSampleDesignName);

        tvProductName.setText(product.getName());
        tvUnitPrice.setText(getString(R.string.label_unit_price_rs,
                String.format(Locale.getDefault(), "%.2f", product.getPrice())));
        tvTotal.setText(getString(R.string.label_total_rs,
                String.format(Locale.getDefault(), "%.2f", product.getPrice())));

        Integer imageRes = com.example.printxpress.adapters.ProductAdapter.PRODUCT_IMAGES.get(product.getName());
        int resolvedImage = imageRes != null ? imageRes : android.R.drawable.ic_menu_gallery;
        Glide.with(this)
            .load(resolvedImage)
            .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(8)))
            .into(ivProductImage);

        etQuantity.addTextChangedListener(new android.text.TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateTotal();
            }
            public void afterTextChanged(android.text.Editable s) {}
        });

        imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    sampleDesignUri = result.getData().getData();
                    ivSampleDesign.setVisibility(View.VISIBLE);
                    Glide.with(this).load(sampleDesignUri).into(ivSampleDesign);
                    tvSampleDesignName.setText(R.string.design_selected);
                }
            }
        );

        btnUploadDesign.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        btnPlaceOrder.setOnClickListener(v -> placeOrder());
    }

    private void updateTotal() {
        try {
            int qty = Integer.parseInt(etQuantity.getText().toString());
            double total = qty * product.getPrice();
            tvTotal.setText(getString(R.string.label_total_rs,
                    String.format(Locale.getDefault(), "%.2f", total)));
        } catch (NumberFormatException e) {
            tvTotal.setText(getString(R.string.label_total_rs,
                    String.format(Locale.getDefault(), "%.2f", product.getPrice())));
        }
    }

    private void placeOrder() {
        String qtyStr = etQuantity.getText().toString().trim();
        String specs = etSpecs.getText().toString().trim();

        if (qtyStr.isEmpty() || Integer.parseInt(qtyStr) < 1) {
            etQuantity.setError("Enter valid quantity");
            return;
        }

        int qty = Integer.parseInt(qtyStr);
        int selectedDeliveryId = rgDelivery.getCheckedRadioButtonId();
        RadioButton rbDelivery = findViewById(selectedDeliveryId);
        String deliveryType = rbDelivery != null ? rbDelivery.getText().toString() : "Pickup";

        double total = qty * product.getPrice();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

        Order order = new Order();
        order.setUserId(session.getUserId());
        order.setProductId(product.getProductId());
        order.setProductName(product.getName());
        order.setQuantity(qty);
        order.setTotalPrice(total);
        order.setStatus("Payment Pending");
        order.setOrderDate(date);
        order.setSpecifications(specs);
        order.setDeliveryType(deliveryType);

        OrderDAO orderDAO = new OrderDAO(this);
        long orderId = orderDAO.placeOrder(order);

        if (orderId > 0) {
            // Add notification
            NotificationDAO notifDAO = new NotificationDAO(this);
            String notifMsg = "Order " + orderId + ": Your order for " + product.getName() + " is placed and awaiting payment.";
            notifDAO.addNotification(new Notification(session.getUserId(), "Order Placed - Payment Pending", notifMsg, date));
            Toast.makeText(this, notifMsg, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, OrderSummaryActivity.class);
            intent.putExtra("product_name", product.getName());
            intent.putExtra("quantity", qty);
            intent.putExtra("total", total);
            intent.putExtra("order_id", (int) orderId);
            intent.putExtra("delivery_type", deliveryType);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to place order. Try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
