package id.ac.upb.asetpinjam.katalog;

import id.ac.upb.asetpinjam.model.Aset;
import id.ac.upb.asetpinjam.model.JenisAset;
import java.util.ArrayList;
import java.util.List;

/** Katalog aset di memori. tambah() dan semua() DISEDIAKAN; Anda mengerjakan dua overload cari() (minggu 4). */
public class KatalogAset {

    private final List<Aset> daftar = new ArrayList<>();

    public void tambah(Aset aset) {
        daftar.add(aset);
    }

    public List<Aset> semua() {
        return List.copyOf(daftar);
    }

    /** TODO minggu 4: aset yang namanya memuat kata (tanpa membedakan huruf besar/kecil). */
    public List<Aset> cari(String kata) {
        if (kata == null || kata.isEmpty()) {
            throw new IllegalArgumentException("Kata tidak boleh null atau kosong");
        }
        String katacari = kata.toLowerCase();
        return daftar.stream()
                .filter(aset -> aset.getNama().toLowerCase().contains(katacari))
                .toList();
    }

    /** TODO minggu 4: seperti cari(kata) tetapi hanya untuk jenis yang diminta (overloading). */
    public List<Aset> cari(String kata, JenisAset jenis) {
    if (jenis == null) {
        throw new IllegalArgumentException("Jenis tidak boleh null");
    }
    return cari(kata).stream()
            .filter(aset -> aset.getJenis() == jenis)
            .toList();
    }
}
