package com.example.printxpress.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.printxpress.R;
import com.example.printxpress.dao.UserDAO;
import com.example.printxpress.models.User;
import com.example.printxpress.utils.SessionManager;
import com.example.printxpress.utils.ValidationUtils;

public class RegisterActivity extends AppCompatActivity {
    private EditText etName, etEmail, etPhone, etPassword, etConfirmPassword, etAddress;
    private UserDAO userDAO;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etAddress = findViewById(R.id.etAddress);
        Button btnRegister = findViewById(R.id.btnRegister);
        TextView tvLogin = findViewById(R.id.tvLogin);

        userDAO = new UserDAO(this);
        session = new SessionManager(this);

        btnRegister.setOnClickListener(v -> register());
        tvLogin.setOnClickListener(v -> finish());
    }

    private void register() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (!ValidationUtils.isNotEmpty(name)) { etName.setError("Name is required"); return; }
        if (!ValidationUtils.isValidEmail(email)) { etEmail.setError("Enter valid email"); return; }
        if (!ValidationUtils.isValidPhone(phone)) { etPhone.setError("Enter valid 10-digit phone"); return; }
        if (!ValidationUtils.isValidPassword(password)) { etPassword.setError("Password must be at least 6 characters"); return; }
        if (!password.equals(confirmPassword)) { etConfirmPassword.setError("Passwords do not match"); return; }

        if (userDAO.isEmailExists(email)) {
            Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(name, email, phone, password, address);
        long userId = userDAO.registerUser(user);
        if (userId > 0) {
            Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Registration failed. Try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
