package com.example.printxpress.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.printxpress.R;

public class PromotionsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Promotions & Offers");
    }
}
