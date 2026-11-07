package com.example.printxpress.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.net.Uri;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.printxpress.R;
import com.example.printxpress.dao.UserDAO;
import com.example.printxpress.models.User;
import com.example.printxpress.utils.SessionManager;
import com.example.printxpress.utils.ValidationUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

public class ProfileActivity extends AppCompatActivity {
    private EditText etName, etPhone, etAddress;
    private ImageView ivProfileImage;
    private SessionManager session;
    private UserDAO userDAO;
    private User currentUser;
    private Uri selectedImageUri = null;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle("My Profile");

        View scrollRoot = findViewById(R.id.profileScrollRoot);
        ViewCompat.setOnApplyWindowInsetsListener(scrollRoot, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), Math.max(bars.bottom, ime.bottom));
            return insets;
        });

        session = new SessionManager(this);
        userDAO = new UserDAO(this);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        TextView tvEmail = findViewById(R.id.tvEmail);
        ivProfileImage = findViewById(R.id.ivProfileImage);

        currentUser = userDAO.getUserById(session.getUserId());
        if (currentUser != null) {
            etName.setText(currentUser.getName());
            tvEmail.setText(currentUser.getEmail());
            etPhone.setText(currentUser.getPhone());
            etAddress.setText(currentUser.getAddress());
        }

        // Load saved profile image
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String savedUri = prefs.getString("profile_image_uri_" + session.getUserId(), null);
        if (savedUri != null) {
            selectedImageUri = Uri.parse(savedUri);
            Glide.with(this).load(selectedImageUri).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(ivProfileImage);
        }

        // Initialize Image Picker
        imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    Glide.with(this).load(selectedImageUri).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(ivProfileImage);
                }
            }
        );

        ivProfileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        findViewById(R.id.btnSaveProfile).setOnClickListener(v -> saveProfile());
    }

    private void saveProfile() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (!ValidationUtils.isNotEmpty(name)) { etName.setError("Name required"); return; }
        if (!ValidationUtils.isValidPhone(phone)) { etPhone.setError("Enter valid 10-digit phone"); return; }

        currentUser.setName(name);
        currentUser.setPhone(phone);
        currentUser.setAddress(address);

        if (userDAO.updateUser(currentUser)) {
            // Save profile image URI if selected
            if (selectedImageUri != null) {
                SharedPreferences.Editor editor = getSharedPreferences("AppPrefs", MODE_PRIVATE).edit();
                editor.putString("profile_image_uri_" + session.getUserId(), selectedImageUri.toString());
                editor.apply();
            }
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }
}
