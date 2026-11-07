package com.example.printxpress.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.printxpress.R;
import com.example.printxpress.dao.UserDAO;
import com.example.printxpress.models.User;
import com.example.printxpress.utils.SessionManager;
import com.example.printxpress.utils.ValidationUtils;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private UserDAO userDAO;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        View scrollRoot = findViewById(R.id.loginScrollRoot);
        ViewCompat.setOnApplyWindowInsetsListener(scrollRoot, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), Math.max(bars.bottom, ime.bottom));
            return insets;
        });

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegister = findViewById(R.id.tvRegister);

        userDAO = new UserDAO(this);
        session = new SessionManager(this);

        btnLogin.setOnClickListener(v -> login());
        tvRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void login() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (!ValidationUtils.isValidEmail(email)) {
            etEmail.setError("Enter a valid email");
            return;
        }
        if (!ValidationUtils.isNotEmpty(password)) {
            etPassword.setError("Password is required");
            return;
        }

        User user = userDAO.loginUser(email, password);
        if (user != null) {
            session.createSession(user.getUserId(), user.getName(), user.getEmail());
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }
}
