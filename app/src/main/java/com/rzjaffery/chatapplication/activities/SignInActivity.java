package com.rzjaffery.chatapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.rzjaffery.chatapplication.R;
import com.rzjaffery.chatapplication.model.User;
import com.rzjaffery.chatapplication.activities.ChatListActivity;

public class SignInActivity extends AppCompatActivity {

    private EditText etEmail, etPassword, etName;
    private Button btnLogin, btnRegister;
    private ProgressBar progress;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etName = findViewById(R.id.et_name);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        progress = findViewById(R.id.progress);

        auth = FirebaseAuth.getInstance();

        // If already signed in, go to ChatList
        if (auth.getCurrentUser() != null) {
            goChatList();
        }

        btnLogin.setOnClickListener(v -> loginUser());
        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Enter email & password", Toast.LENGTH_SHORT).show();
            return;
        }

        progress.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            progress.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                goChatList();
            } else {
                Toast.makeText(this, "Login failed: " + task.getException().getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void registerUser() {
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        String name = etName.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Enter name, email & password", Toast.LENGTH_SHORT).show();
            return;
        }

        progress.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            progress.setVisibility(View.GONE);
            if (task.isSuccessful() && auth.getCurrentUser() != null) {
                String uid = auth.getCurrentUser().getUid();
                User user = new User(uid, name, email);
                FirebaseDatabase.getInstance().getReference("Users").child(uid).setValue(user);
                goChatList();
            } else {
                Toast.makeText(this, "Register failed: " + task.getException().getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void goChatList() {
        Intent i = new Intent(this, ChatListActivity.class);
        startActivity(i);
        finish();
    }
}
