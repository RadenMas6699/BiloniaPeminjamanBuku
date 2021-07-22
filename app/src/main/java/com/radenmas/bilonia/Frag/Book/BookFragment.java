package com.radenmas.bilonia.Frag.Book;

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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.radenmas.bilonia.Adapter.DataRecycler;
import com.radenmas.bilonia.Adapter.FirebaseViewHolder;
import com.radenmas.bilonia.R;
import com.squareup.picasso.Picasso;

public class BookFragment extends Fragment {

    private RecyclerView rvBook;
    private FirebaseRecyclerOptions<DataRecycler> options;
    private FirebaseRecyclerAdapter<DataRecycler, FirebaseViewHolder> adapter;
    private DatabaseReference dbBook;
    private String rak;

    public BookFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            rak = bundle.getString("rak");
        }

        TextView tvPrevRak = view.findViewById(R.id.tvPrevRak);
        tvPrevRak.setText(rak);

        dbBook = FirebaseDatabase.getInstance().getReference(rak);

        rvBook = view.findViewById(R.id.rvBook);
        rvBook.setHasFixedSize(true);
        rvBook.setLayoutManager(new LinearLayoutManager(getContext()));

        options = new FirebaseRecyclerOptions.Builder<DataRecycler>().setQuery(dbBook, DataRecycler.class).build();
        adapter = new FirebaseRecyclerAdapter<DataRecycler, FirebaseViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull FirebaseViewHolder firebaseViewHolder, int i, @NonNull DataRecycler dataRecycler) {
                Picasso.get().load(dataRecycler.getImg_book()).into(firebaseViewHolder.imgBook);
//                Glide.with(getActivity()).load(dataRecycler.getImg_book()).into(firebaseViewHolder.imgBook);
                firebaseViewHolder.tvTitle.setText(dataRecycler.getJudul());
                firebaseViewHolder.tvPenulis.setText(dataRecycler.getPenulis());
                firebaseViewHolder.tvHalaman.setText(dataRecycler.getHalaman()+"  Halaman");
                firebaseViewHolder.tvStock.setText("Stock  :  "+dataRecycler.getStock());

                firebaseViewHolder.listItem.setOnClickListener(view1 -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("rak", rak);
                    bundle.putString("idBook", dataRecycler.getId_book());

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    DetailBookFragment llf = new DetailBookFragment();
                    llf.setArguments(bundle);
                    ft.replace(R.id.contentMain, llf);
                    ft.commit();
                });
            }

            @NonNull
            @Override
            public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new FirebaseViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_book, parent, false));
            }
        };

        adapter.notifyDataSetChanged();

        rvBook.setAdapter(adapter);

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