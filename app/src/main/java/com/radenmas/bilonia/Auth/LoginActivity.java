package com.radenmas.bilonia.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.radenmas.bilonia.Activity.MainActivity;
import com.radenmas.bilonia.R;

public class LoginActivity extends AppCompatActivity implements OnSuccessListener<AuthResult>, OnFailureListener {
    private EditText etEmail, etPassword;
    private String id, email, password;
    private DatabaseReference dbUser;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        dbUser = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            SharedPreferences myaccount = this.getSharedPreferences("myAccount", Context.MODE_PRIVATE);
            String uid = myaccount.getString("uid", "");

            if (!uid.isEmpty()) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }
    }

    public void Login(View view) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(etEmail.getApplicationWindowToken(), 0);
        manager.hideSoftInputFromWindow(etPassword.getApplicationWindowToken(), 0);

        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Email empty");
        } else if (password.isEmpty()) {
            etPassword.setError("Password empty");
        } else {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_layout);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(this).addOnFailureListener(this);
        }
    }

    public void Register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    @Override
    public void onSuccess(AuthResult authResult) {
        //progres dialog
        progressDialog.dismiss();
        Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        progressDialog.dismiss();
        Toast.makeText(this, "Login Failure", Toast.LENGTH_SHORT).show();
    }
}