package com.radenmas.bilonia.Frag.Kembali;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.radenmas.bilonia.Adapter.DataRecycler;
import com.radenmas.bilonia.Adapter.FirebaseViewHolder;
import com.radenmas.bilonia.R;
import com.squareup.picasso.Picasso;

public class DetailKembaliFragment extends Fragment {
    private DatabaseReference dbDetailKembaliBook;
    private ImageView imgBack, imgBook;
    private String waktu, title, penulis, date_kembali, img_book;
    private TextView tvTitle, tvPenulis, tvHalaman;

    public DetailKembaliFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_kembali, container, false);

        imgBack = view.findViewById(R.id.back);
        imgBook = view.findViewById(R.id.imgBook);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvPenulis = view.findViewById(R.id.tvPenulis);
        tvHalaman = view.findViewById(R.id.tvHalaman);

        imgBack.setOnClickListener(view1 -> {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            KembaliFragment llf = new KembaliFragment();
            ft.replace(R.id.contentMain, llf);
            ft.commit();
        });

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            waktu = bundle.getString("waktu");
        }

        dbDetailKembaliBook = FirebaseDatabase.getInstance().getReference("Kembali").child(waktu);
        dbDetailKembaliBook.keepSynced(true);

        dbDetailKembaliBook.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                title = snapshot.child("judul").getValue().toString();
                penulis = snapshot.child("penulis").getValue().toString();
                date_kembali = snapshot.child("date_kembali").getValue().toString();
                img_book = snapshot.child("img_book").getValue().toString();

                Picasso.get().load(img_book).into(imgBook);
                tvTitle.setText(title);
                tvPenulis.setText(penulis);
                tvHalaman.setText("Dikembalikan pada tanggal " + date_kembali);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}