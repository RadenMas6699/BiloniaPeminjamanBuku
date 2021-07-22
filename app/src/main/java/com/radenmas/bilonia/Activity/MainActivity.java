package com.radenmas.bilonia.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.radenmas.bilonia.Auth.LoginActivity;
import com.radenmas.bilonia.Frag.Kembali.DetailKembaliFragment;
import com.radenmas.bilonia.Frag.Kembali.KembaliFragment;
import com.radenmas.bilonia.Frag.Pinjam.PinjamFragment;
import com.radenmas.bilonia.Frag.Book.RakFragment;
import com.radenmas.bilonia.R;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup radioGroupMain;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private RadioButton rbBook, rbPinjam, rbKembali;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rbBook = findViewById(R.id.rbBook);
        rbPinjam = findViewById(R.id.rbPinjam);
        rbKembali = findViewById(R.id.rbKembali);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();

        SharedPreferences myaccount = getSharedPreferences("myAccount", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myaccount.edit();
        editor.putString("uid", "user");
        editor.apply();

        rbBook.setBackground(getResources().getDrawable(R.drawable.bg_btn));
        rbBook.setTextColor(getResources().getColor(R.color.purple_700));
        rbPinjam.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue));
        rbPinjam.setTextColor(getResources().getColor(R.color.white));
        rbKembali.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue));
        rbKembali.setTextColor(getResources().getColor(R.color.white));

        getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, new RakFragment()).commit();

        radioGroupMain = findViewById(R.id.radioGroupMain);
        radioGroupMain.setOnCheckedChangeListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rbBook:
                rbBook.setBackground(getResources().getDrawable(R.drawable.bg_btn));
                rbBook.setTextColor(getResources().getColor(R.color.purple_700));
                rbPinjam.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue));
                rbPinjam.setTextColor(getResources().getColor(R.color.white));
                rbKembali.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue));
                rbKembali.setTextColor(getResources().getColor(R.color.white));

                getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, new RakFragment()).commit();
                break;
            case R.id.rbPinjam:
                rbBook.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue));
                rbBook.setTextColor(getResources().getColor(R.color.white));
                rbPinjam.setBackground(getResources().getDrawable(R.drawable.bg_btn));
                rbPinjam.setTextColor(getResources().getColor(R.color.purple_700));
                rbKembali.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue));
                rbKembali.setTextColor(getResources().getColor(R.color.white));

                getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, new PinjamFragment()).commit();
                break;
            case R.id.rbKembali:
                rbBook.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue));
                rbBook.setTextColor(getResources().getColor(R.color.white));
                rbPinjam.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue));
                rbPinjam.setTextColor(getResources().getColor(R.color.white));
                rbKembali.setBackground(getResources().getDrawable(R.drawable.bg_btn));
                rbKembali.setTextColor(getResources().getColor(R.color.purple_700));

                getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, new KembaliFragment()).commit();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Tekan sekali lagi untuk keluar aplikasi", Toast.LENGTH_SHORT).show();
        i = i + 1;
        if (i > 1) {
            finish();
        }
    }

    public void Logout(View view) {
        new AlertDialog.Builder(this)
                .setMessage("Anda yakin ingin keluar ?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    fAuth.signOut();
                    SharedPreferences myaccount = this.getSharedPreferences("myAccount", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = myaccount.edit();
                    editor.clear();
                    editor.apply();

                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                })
                .setNegativeButton("Tidak", (dialog, which) -> {

                })
                .show();
    }
}