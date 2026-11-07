package com.example.printxpress.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.printxpress.R;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.printxpress.adapters.DesignAdapter;
import java.util.Arrays;
import java.util.List;

public class DesignGalleryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_gallery);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Design Gallery");

        RecyclerView recyclerDesigns = findViewById(R.id.recyclerDesigns);
        recyclerDesigns.setLayoutManager(new GridLayoutManager(this, 2));

        List<String> mockDesigns = Arrays.asList(
                "Elegant Business Card",
                "Promotional Agency Flyer",
                "Grand Event Banner",
                "Custom Printed T-Shirt",
                "Branded Menu Poster",
                "Premium Logo Print"
        );

        DesignAdapter adapter = new DesignAdapter(mockDesigns);
        recyclerDesigns.setAdapter(adapter);
    }
}
