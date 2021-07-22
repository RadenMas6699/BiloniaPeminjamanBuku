package com.radenmas.bilonia.Adapter;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.radenmas.bilonia.R;

public class FirebaseViewHolder extends RecyclerView.ViewHolder {

    public final TextView tvTitle, tvPenulis, tvHalaman, tvStock, tvPeminjam, tvMulai, tvKembali, tvKembalikan;
    public final ImageView imgBook, menuPopup;
    public final RelativeLayout listItem;

    public FirebaseViewHolder(View itemView) {
        super(itemView);

        //item book
        listItem = itemView.findViewById(R.id.listItem);
        imgBook = itemView.findViewById(R.id.imgBook);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvPenulis = itemView.findViewById(R.id.tvPenulis);
        tvHalaman = itemView.findViewById(R.id.tvHalaman);
        tvStock = itemView.findViewById(R.id.tvStock);

        //item pinjam
        tvPeminjam = itemView.findViewById(R.id.tvPeminjam);
        tvMulai = itemView.findViewById(R.id.tvMulai);
        tvKembali = itemView.findViewById(R.id.tvKembali);
        tvKembalikan = itemView.findViewById(R.id.tvKembalikan);

        //item kembali
        menuPopup = itemView.findViewById(R.id.menuPopup);
    }
}
