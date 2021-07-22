package com.radenmas.bilonia.Frag.Book;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.radenmas.bilonia.Adapter.DataRecycler;
import com.radenmas.bilonia.Adapter.FirebaseViewHolder;
import com.radenmas.bilonia.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RakFragment extends Fragment implements View.OnClickListener {
    private static final int RESULT_OK = -1;
    private RecyclerView rvRakBook;
    private FloatingActionButton floatingAddBook;
    private FirebaseRecyclerOptions<DataRecycler> options;
    private FirebaseRecyclerAdapter<DataRecycler, FirebaseViewHolder> adapter;
    private DatabaseReference dbBook, dbRakBook;
    private Uri filePath;
    private ProgressDialog progressDialog;

    private ArrayList<String> listRak;
    private ArrayAdapter<String> adapterRak;


    public RakFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_rak, container, false);

        floatingAddBook = view.findViewById(R.id.floatingAddBook);
        floatingAddBook.setOnClickListener(this);

        dbBook = FirebaseDatabase.getInstance().getReference();
        dbRakBook = FirebaseDatabase.getInstance().getReference("Rak");
        dbRakBook.keepSynced(true);

        rvRakBook = view.findViewById(R.id.rvRakBook);
        rvRakBook.setHasFixedSize(true);
        rvRakBook.setLayoutManager(new LinearLayoutManager(getContext()));

        options = new FirebaseRecyclerOptions.Builder<DataRecycler>().setQuery(dbRakBook, DataRecycler.class).build();
        adapter = new FirebaseRecyclerAdapter<DataRecycler, FirebaseViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull FirebaseViewHolder firebaseViewHolder, int i, @NonNull DataRecycler dataRecycler) {
                int img;
                switch (i) {
                    case 0:
                        img = R.drawable.ic_rak_a;
                        break;
                    case 1:
                        img = R.drawable.ic_rak_b;
                        break;
                    case 2:
                        img = R.drawable.ic_rak_c;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + i);
                }
                Picasso.get().load(img).into(firebaseViewHolder.imgBook);
                firebaseViewHolder.tvTitle.setText(dataRecycler.getNama());

                dbBook.child(dataRecycler.getNama()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String countBook = String.valueOf(snapshot.getChildrenCount());
                        firebaseViewHolder.tvStock.setText(countBook + "  Buku");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                firebaseViewHolder.listItem.setOnClickListener(view1 -> {

                    Bundle bundle = new Bundle();
                    bundle.putString("rak", dataRecycler.getNama());

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    BookFragment llf = new BookFragment();
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

        rvRakBook.setAdapter(adapter);

        return view;
    }

    @Override
    public void onClick(View view) {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.MyBottomSheetDialogTheme);
        dialog.setContentView(R.layout.bottom_add_book);
        dialog.setCanceledOnTouchOutside(true);

        EditText etJudul = dialog.findViewById(R.id.etJudul);
        EditText etPenulis = dialog.findViewById(R.id.etPenulis);
        EditText etTahun = dialog.findViewById(R.id.etTahun);
        EditText etHalaman = dialog.findViewById(R.id.etHalaman);
        EditText etBahasa = dialog.findViewById(R.id.etBahasa);
        EditText etGenre = dialog.findViewById(R.id.etGenre);
        AutoCompleteTextView etRak = dialog.findViewById(R.id.etRak);
        EditText etNoRak = dialog.findViewById(R.id.etNoRak);
        EditText etStock = dialog.findViewById(R.id.etStock);

        dbRakBook.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    DataRecycler data = item.getValue(DataRecycler.class);
                    listRak.add(data.getNama());
                }
                adapterRak.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listRak = new ArrayList<>();
        adapterRak = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,
                listRak);
        etRak.setAdapter(adapterRak);

        MaterialButton btnChooseImg = dialog.findViewById(R.id.btnChooseImg);
        MaterialButton btnAddBook = dialog.findViewById(R.id.btnAddBook);

        btnChooseImg.setOnClickListener(view12 -> {
            ChooseFoto();
        });

        btnAddBook.setOnClickListener(view1 -> {

            InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(etJudul.getApplicationWindowToken(), 0);
            manager.hideSoftInputFromWindow(etPenulis.getApplicationWindowToken(), 0);
            manager.hideSoftInputFromWindow(etTahun.getApplicationWindowToken(), 0);
            manager.hideSoftInputFromWindow(etHalaman.getApplicationWindowToken(), 0);
            manager.hideSoftInputFromWindow(etBahasa.getApplicationWindowToken(), 0);
            manager.hideSoftInputFromWindow(etGenre.getApplicationWindowToken(), 0);
            manager.hideSoftInputFromWindow(etRak.getApplicationWindowToken(), 0);
            manager.hideSoftInputFromWindow(etNoRak.getApplicationWindowToken(), 0);
            manager.hideSoftInputFromWindow(etStock.getApplicationWindowToken(), 0);

            String judul = etJudul.getText().toString().trim();
            String penulis = etPenulis.getText().toString().trim();
            String tahun = etTahun.getText().toString().trim();
            String halaman = etHalaman.getText().toString().trim();
            String bahasa = etBahasa.getText().toString().trim();
            String genre = etGenre.getText().toString().trim();
            String rak = etRak.getText().toString().trim();
            String nomor_rak = etNoRak.getText().toString().trim();
            String stock = etStock.getText().toString().trim();

            int intHalaman = Integer.parseInt(halaman);
            int intTahun = Integer.parseInt(tahun);
            int intStock = Integer.parseInt(stock);

            if (judul.isEmpty()) {
                etJudul.setError("Judul buku kosong");
            } else if (penulis.isEmpty()) {
                etPenulis.setError("Penulis buku kosong");
            } else if (tahun.isEmpty()) {
                etTahun.setError("Tahun terbit kosong");
            } else if (halaman.isEmpty()) {
                etHalaman.setError("Jumlah halaman kosong");
            } else if (bahasa.isEmpty()) {
                etBahasa.setError("Bahasa buku kosong");
            } else if (genre.isEmpty()) {
                etGenre.setError("Genre buku kosong");
            } else if (rak.isEmpty()) {
                etRak.setError("Rak buku kosong");
            } else if (nomor_rak.isEmpty()) {
                etNoRak.setError("No rak buku kosong");
            } else if (stock.isEmpty()) {
                etStock.setError("Stock buku kosong");
            } else if (filePath == null) {
                Toast.makeText(getContext(), "Pilih foto buku", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_layout);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                String idBook = dbBook.push().getKey();

                StorageReference storageReference = FirebaseStorage.getInstance().getReference("Book").child(idBook);
                storageReference.putFile(filePath).addOnSuccessListener(taskSnapshot -> {
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        Map<String, Object> data_book = new HashMap<>();
                        data_book.put("id_book", idBook);
                        data_book.put("judul", judul);
                        data_book.put("img_book", String.valueOf(uri));
                        data_book.put("penulis", penulis);
                        data_book.put("tahun", intTahun);
                        data_book.put("halaman", intHalaman);
                        data_book.put("bahasa", bahasa);
                        data_book.put("genre", genre);
                        data_book.put("rak", rak);
                        data_book.put("nomor_rak", nomor_rak);
                        data_book.put("stock", intStock);

                        dbBook.child(rak).child(idBook).setValue(data_book);
                    });
                    new Handler().postDelayed(() -> {
                        etJudul.setText("");
                        etPenulis.setText("");
                        etTahun.setText("");
                        etHalaman.setText("");
                        etBahasa.setText("");
                        etGenre.setText("");
                        etRak.setText("");
                        etNoRak.setText("");
                        etStock.setText("");

                        progressDialog.dismiss();

                        Toast.makeText(getContext(), "Buku berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    }, 1000);

                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Gagal tambah buku", Toast.LENGTH_SHORT).show();
                });
            }
        });

        dialog.show();
    }

    private void ChooseFoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), 71);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 71 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
        }
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