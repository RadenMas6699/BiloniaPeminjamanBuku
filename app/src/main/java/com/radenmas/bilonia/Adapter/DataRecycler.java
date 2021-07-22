package com.radenmas.bilonia.Adapter;

public class DataRecycler {
    //data Book
    String id_book, img_book, judul, penulis, bahasa, genre, rak, nomor_rak;
    int tahun, halaman, stock;
    long waktu;

    //data Peminjam
    String peminjam, date_pinjam, date_kembali;
    int jml_pinjam;

    //data Rak
    String nama;

    public DataRecycler() {
    }

    public DataRecycler(String id_book, String img_book, String judul, String penulis, String bahasa, String genre, String rak, String nomor_rak, int tahun, int halaman, int stock, long waktu, String peminjam, String date_pinjam, String date_kembali, int jml_pinjam, String nama) {
        this.id_book = id_book;
        this.img_book = img_book;
        this.judul = judul;
        this.penulis = penulis;
        this.bahasa = bahasa;
        this.genre = genre;
        this.rak = rak;
        this.nomor_rak = nomor_rak;
        this.tahun = tahun;
        this.halaman = halaman;
        this.stock = stock;
        this.waktu = waktu;
        this.peminjam = peminjam;
        this.date_pinjam = date_pinjam;
        this.date_kembali = date_kembali;
        this.jml_pinjam = jml_pinjam;
        this.nama = nama;
    }

    public String getId_book() {
        return id_book;
    }

    public String getImg_book() {
        return img_book;
    }

    public String getJudul() {
        return judul;
    }

    public String getPenulis() {
        return penulis;
    }

    public String getBahasa() {
        return bahasa;
    }

    public String getGenre() {
        return genre;
    }

    public String getRak() {
        return rak;
    }

    public String getNomor_rak() {
        return nomor_rak;
    }

    public int getTahun() {
        return tahun;
    }

    public int getHalaman() {
        return halaman;
    }

    public int getStock() {
        return stock;
    }

    public long getWaktu() {
        return waktu;
    }

    public String getPeminjam() {
        return peminjam;
    }

    public String getDate_pinjam() {
        return date_pinjam;
    }

    public String getDate_kembali() {
        return date_kembali;
    }

    public int getJml_pinjam() {
        return jml_pinjam;
    }

    public String getNama() {
        return nama;
    }
}

