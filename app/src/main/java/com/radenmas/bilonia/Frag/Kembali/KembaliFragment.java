package com.radenmas.bilonia.Frag.Kembali;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.radenmas.bilonia.Adapter.DataRecycler;
import com.radenmas.bilonia.Adapter.FirebaseViewHolder;
import com.radenmas.bilonia.Frag.Book.BookFragment;
import com.radenmas.bilonia.R;
import com.squareup.picasso.Picasso;

public class KembaliFragment extends Fragment {
    private RecyclerView rvKembaliBook;
    private FirebaseRecyclerOptions<DataRecycler> options;
    private FirebaseRecyclerAdapter<DataRecycler, FirebaseViewHolder> adapter;
    private DatabaseReference dbKembaliBook;
    private long waktu;

    public KembaliFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_kembali, container, false);

        dbKembaliBook = FirebaseDatabase.getInstance().getReference("Kembali");
        dbKembaliBook.keepSynced(true);

        rvKembaliBook = view.findViewById(R.id.rvKembaliBook);
        rvKembaliBook.setHasFixedSize(true);
        rvKembaliBook.setLayoutManager(new LinearLayoutManager(getContext()));

        options = new FirebaseRecyclerOptions.Builder<DataRecycler>().setQuery(dbKembaliBook, DataRecycler.class).build();
        adapter = new FirebaseRecyclerAdapter<DataRecycler, FirebaseViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull FirebaseViewHolder firebaseViewHolder, int i, @NonNull DataRecycler dataRecycler) {
                 waktu = dataRecycler.getWaktu();
                Picasso.get().load(dataRecycler.getImg_book()).into(firebaseViewHolder.imgBook);
                firebaseViewHolder.tvTitle.setText(dataRecycler.getJudul());
                firebaseViewHolder.menuPopup.setOnClickListener(view1 -> {
                    PopupMenu popupMenu = new PopupMenu(view1.getContext(), view1);
                    popupMenu.inflate(R.menu.popup_kembali);
                    popupMenu.setOnMenuItemClickListener(menuItem -> {

                        switch (menuItem.getItemId()) {
                            case R.id.popup_detail:
                                Bundle bundle = new Bundle();
                                long waktu = dataRecycler.getWaktu();
                                bundle.putString("waktu", String.valueOf(waktu));

                                FragmentManager fm = getFragmentManager();
                                FragmentTransaction ft = fm.beginTransaction();
                                DetailKembaliFragment llf = new DetailKembaliFragment();
                                llf.setArguments(bundle);
                                ft.replace(R.id.contentMain, llf);
                                ft.commit();

                                break;

                            case R.id.popup_delete:
                                new AlertDialog.Builder(getContext())
                                        .setMessage("Hapus riwayat " + dataRecycler.getJudul())
                                        .setPositiveButton("Ya", (dialogInterface, i1) -> dbKembaliBook.child(""+dataRecycler.getWaktu()).removeValue().addOnCompleteListener(task -> {

                                            //hapus realtime database
                                            Query query = dbKembaliBook.orderByChild("waktu").equalTo(dataRecycler.getWaktu());
                                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                        dataSnapshot.getRef().removeValue();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                            Toast.makeText(getContext(), dataRecycler.getJudul() + " berhasil dihapus", Toast.LENGTH_SHORT).show();
                                        }))
                                        .setNegativeButton("Tidak", (dialogInterface, i1) -> {

                                        }).show();
                                break;
                        }
                        return true;
                    });
                    popupMenu.show();
                });
            }

            @NonNull
            @Override
            public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new FirebaseViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_kembali, parent, false));
            }
        };

        adapter.notifyDataSetChanged();

        rvKembaliBook.setAdapter(adapter);

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