package com.example.printxpress.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.printxpress.R;

public class PrintGuidelinesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_guidelines);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Guidelines & FAQs");
    }
}
