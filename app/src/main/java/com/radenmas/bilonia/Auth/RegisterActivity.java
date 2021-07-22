package com.radenmas.bilonia.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.radenmas.bilonia.R;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements OnSuccessListener<AuthResult>, OnFailureListener {
    private EditText etUsername, etEmail, etPassword;
    private CheckBox tvPolicy;
    private DatabaseReference dbUser;
    private FirebaseAuth auth;
    private String uid, username, email, pass;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvPolicy = findViewById(R.id.tvPolicy);
        auth = FirebaseAuth.getInstance();
        dbUser = FirebaseDatabase.getInstance().getReference();
    }

    public void Signup(View view) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(etUsername.getApplicationWindowToken(), 0);
        manager.hideSoftInputFromWindow(etEmail.getApplicationWindowToken(), 0);
        manager.hideSoftInputFromWindow(etPassword.getApplicationWindowToken(), 0);

        username = etUsername.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        pass = etPassword.getText().toString().trim();

        if (username.isEmpty()) {
            etUsername.setError("Username empty");
        } else if (email.isEmpty()) {
            etEmail.setError("Email empty");
        } else if (pass.isEmpty()) {
            etPassword.setError("Password empty");
        } else if (!tvPolicy.isChecked()) {
            Snackbar.make(view, "Harap ceklis persetujuan", Snackbar.LENGTH_SHORT).show();
        } else {
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_layout);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            auth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(this).addOnFailureListener(this);
        }
    }

    @Override
    public void onSuccess(AuthResult authResult) {
        progressDialog.dismiss();
        uid = auth.getUid();
        dbUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Object> dataUser = new HashMap<>();
                dataUser.put("uid", uid);
                dataUser.put("username", username);
                dataUser.put("email", email);
                dbUser.child("User").child(uid).setValue(dataUser);
                etUsername.setText("");
                etEmail.setText("");
                etPassword.setText("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        progressDialog.dismiss();
        Toast.makeText(this, "Register Failure", Toast.LENGTH_SHORT).show();
    }

    public void Login(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}