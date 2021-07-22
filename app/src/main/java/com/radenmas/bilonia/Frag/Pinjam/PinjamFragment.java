package com.radenmas.bilonia.Frag.Pinjam;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.radenmas.bilonia.Adapter.DataRecycler;
import com.radenmas.bilonia.Adapter.FirebaseViewHolder;
import com.radenmas.bilonia.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class PinjamFragment extends Fragment {
    private RecyclerView rvPinjamBook;
    private FirebaseRecyclerOptions<DataRecycler> options;
    private FirebaseRecyclerAdapter<DataRecycler, FirebaseViewHolder> adapter;
    private DatabaseReference dbPinjamBook;
    private int intStock;

    public PinjamFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pinjam, container, false);

        dbPinjamBook = FirebaseDatabase.getInstance().getReference("Pinjam");

        rvPinjamBook = view.findViewById(R.id.rvPinjamBook);
        rvPinjamBook.setHasFixedSize(true);
        rvPinjamBook.setLayoutManager(new LinearLayoutManager(getContext()));

        options = new FirebaseRecyclerOptions.Builder<DataRecycler>().setQuery(dbPinjamBook, DataRecycler.class).build();
        adapter = new FirebaseRecyclerAdapter<DataRecycler, FirebaseViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull FirebaseViewHolder firebaseViewHolder, int i, @NonNull DataRecycler dataRecycler) {
                Picasso.get().load(dataRecycler.getImg_book()).into(firebaseViewHolder.imgBook);
//                Glide.with(getActivity()).load(dataRecycler.getImg_book()).into(firebaseViewHolder.imgBook);
                firebaseViewHolder.tvTitle.setText(dataRecycler.getJudul());
                firebaseViewHolder.tvPenulis.setText(dataRecycler.getPenulis());
                firebaseViewHolder.tvHalaman.setText(dataRecycler.getHalaman() + "  Halaman");

                firebaseViewHolder.tvPeminjam.setText(dataRecycler.getPeminjam());
                firebaseViewHolder.tvMulai.setText(dataRecycler.getDate_pinjam());
                firebaseViewHolder.tvKembali.setText(dataRecycler.getDate_kembali());

                firebaseViewHolder.tvKembalikan.setOnClickListener(view1 -> {

                    //Set Data Kembali
                    DatabaseReference dbKembaliBook = FirebaseDatabase.getInstance().getReference("Kembali");
                    dbKembaliBook.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long waktu  = dataRecycler.getWaktu();
                            Map<String, Object> data_book = new HashMap<>();
                            data_book.put("id_book", dataRecycler.getId_book());
                            data_book.put("waktu",waktu);
                            data_book.put("judul", dataRecycler.getJudul());
                            data_book.put("img_book", dataRecycler.getImg_book());
                            data_book.put("penulis", dataRecycler.getPenulis());
                            data_book.put("date_kembali", dataRecycler.getDate_kembali());

                            dbKembaliBook.child(String.valueOf(waktu)).setValue(data_book);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    int jmlPinjam = dataRecycler.getJml_pinjam();

                    //get Data Stock
                    DatabaseReference dbBook = FirebaseDatabase.getInstance().getReference(dataRecycler.getRak()).child(dataRecycler.getId_book());
                    dbBook.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String stock = snapshot.child("stock").getValue().toString();
                            intStock = Integer.parseInt(stock);

                            int hasil = jmlPinjam + intStock;

                            //add Jumlah Stock
                            Map<String, Object> stock_book = new HashMap<>();
                            stock_book.put("stock", hasil);
                            dbBook.updateChildren(stock_book);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    //hapus data Buku Pinjam
                    long waktu = dataRecycler.getWaktu();
                    dbPinjamBook.child(String.valueOf(waktu)).removeValue();
                });

            }

            @NonNull
            @Override
            public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new FirebaseViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_pinjam, parent, false));
            }
        };

        adapter.notifyDataSetChanged();

        rvPinjamBook.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}