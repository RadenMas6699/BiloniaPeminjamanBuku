package com.radenmas.bilonia.Frag.Book;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.radenmas.bilonia.Auth.RegisterActivity;
import com.radenmas.bilonia.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DetailBookFragment extends Fragment implements View.OnClickListener {
    private DatabaseReference dbBook, dbPinjam;
    private String rak, idBook;
    private int jmlPinjam = 0;
    private int intStock, intHalaman;
    private String title, penulis, tahun, halaman, bahasa, genre, name_rak, nomor_rak, stock, img_book;

    private TextView tvPrevRak, tvDetailTitleBook, tvDetailPenulis, tvDetailTahun, tvDetailHalaman, tvDetailBahasa, tvDetailGenre,
            tvPerStock, tvDetailRak, tvDetailNoRak;
    private ImageView imgDetailBook, imgDatePinjam, imgDateKembali;
    private SeekBar seekbarVolume;
    private EditText etPinjamTo, etDatePinjam, etDateKembali;
    private MaterialButton btnPinjamkanBook;
    private ProgressDialog progressDialog;

    private int mYear, mMonth, mDay;

    public DetailBookFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_book, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            rak = bundle.getString("rak");
            idBook = bundle.getString("idBook");
        }

        dbBook = FirebaseDatabase.getInstance().getReference();

        tvPrevRak = view.findViewById(R.id.tvPrevRak);
        tvDetailTitleBook = view.findViewById(R.id.tvDetailTitleBook);
        tvDetailPenulis = view.findViewById(R.id.tvDetailPenulis);
        tvDetailTahun = view.findViewById(R.id.tvDetailTahun);
        tvDetailHalaman = view.findViewById(R.id.tvDetailHalaman);
        tvDetailBahasa = view.findViewById(R.id.tvDetailBahasa);
        tvDetailGenre = view.findViewById(R.id.tvDetailGenre);
        tvPerStock = view.findViewById(R.id.tvPerStock);
        tvDetailRak = view.findViewById(R.id.tvDetailRak);
        tvDetailNoRak = view.findViewById(R.id.tvDetailNoRak);
        imgDetailBook = view.findViewById(R.id.imgDetailBook);
        seekbarVolume = view.findViewById(R.id.seekbarVolume);
        etPinjamTo = view.findViewById(R.id.etPinjamTo);
        imgDatePinjam = view.findViewById(R.id.imgDatePinjam);
        imgDateKembali = view.findViewById(R.id.imgDateKembali);
        etDatePinjam = view.findViewById(R.id.etDatePinjam);
        etDateKembali = view.findViewById(R.id.etDateKembali);
        btnPinjamkanBook = view.findViewById(R.id.btnPinjamkanBook);

        tvPrevRak.setText(rak);

        dbBook = FirebaseDatabase.getInstance().getReference(rak).child(idBook);
        dbBook.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                title = snapshot.child("judul").getValue().toString();
                penulis = snapshot.child("penulis").getValue().toString();
                tahun = snapshot.child("tahun").getValue().toString();
                halaman = snapshot.child("halaman").getValue().toString();
                bahasa = snapshot.child("bahasa").getValue().toString();
                genre = snapshot.child("genre").getValue().toString();
                name_rak = snapshot.child("rak").getValue().toString();
                nomor_rak = snapshot.child("nomor_rak").getValue().toString();
                stock = snapshot.child("stock").getValue().toString();
                img_book = snapshot.child("img_book").getValue().toString();

                intStock = Integer.parseInt(stock);
                intHalaman = Integer.parseInt(halaman);

                Picasso.get().load(img_book).into(imgDetailBook);
                tvDetailTitleBook.setText(title);
                tvDetailPenulis.setText(":  " + penulis);
                tvDetailTahun.setText(":  " + tahun);
                tvDetailHalaman.setText(":  " + halaman);
                tvDetailBahasa.setText(":  " + bahasa);
                tvDetailGenre.setText(":  " + genre);

                seekbarVolume.setMax(intStock);
                tvPerStock.setText("0/" + stock);
                seekbarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        tvPerStock.setText(i + "/" + stock);
                        jmlPinjam = i;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                tvDetailRak.setText(":  " + name_rak);
                tvDetailNoRak.setText(":  " + nomor_rak);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imgDatePinjam.setOnClickListener(view14 -> {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view1, year, monthOfYear, dayOfMonth) -> etDatePinjam.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year), mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        imgDateKembali.setOnClickListener(view13 -> {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view12, year, monthOfYear, dayOfMonth) -> etDateKembali.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year), mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        btnPinjamkanBook.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        String pinjamTo = etPinjamTo.getText().toString();
        String datePinjam = etDatePinjam.getText().toString();
        String dateKembali = etDateKembali.getText().toString();

        if (jmlPinjam == 0) {
            Snackbar.make(view, "Atur jumlah buku yang dipinjam", Snackbar.LENGTH_SHORT).show();
        } else if (pinjamTo.isEmpty()) {
            Snackbar.make(view, "Masukkan nama peminjam", Snackbar.LENGTH_SHORT).show();
        } else if (datePinjam.isEmpty()) {
            Snackbar.make(view, "Atur tanggal peminjaman", Snackbar.LENGTH_SHORT).show();
        } else if (dateKembali.isEmpty()) {
            Snackbar.make(view, "Atur tanggal pengembalian", Snackbar.LENGTH_SHORT).show();
        } else {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_layout);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            pinjam(jmlPinjam, pinjamTo, datePinjam, dateKembali);
        }
    }

    private void pinjam(int jmlPinjam, String pinjamTo, String datePinjam, String dateKembali) {
        Map<String, Object> stock_book = new HashMap<>();
        stock_book.put("stock", intStock - jmlPinjam);
        dbBook.updateChildren(stock_book);

        dbPinjam = FirebaseDatabase.getInstance().getReference("Pinjam");

        long waktu = Calendar.getInstance().getTimeInMillis();

        Map<String, Object> data_book = new HashMap<>();
        data_book.put("id_book", idBook);
        data_book.put("waktu", waktu);
        data_book.put("judul", title);
        data_book.put("img_book", img_book);
        data_book.put("penulis", penulis);
        data_book.put("halaman", intHalaman);
        data_book.put("rak", name_rak);
        data_book.put("jml_pinjam", jmlPinjam);
        data_book.put("peminjam", pinjamTo);
        data_book.put("date_pinjam", datePinjam);
        data_book.put("date_kembali", dateKembali);

        dbPinjam.child(String.valueOf(waktu)).setValue(data_book);

        new Handler().postDelayed(() -> {
            seekbarVolume.setProgress(0);
            etPinjamTo.setText("");
            etDatePinjam.setText("");
            etDateKembali.setText("");
            progressDialog.dismiss();
        }, 1500);

    }
}