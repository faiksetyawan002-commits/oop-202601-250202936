package id.ac.upb.asetpinjam.jadwal;

import id.ac.upb.asetpinjam.model.Peminjaman;
import id.ac.upb.asetpinjam.model.StatusPeminjaman;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Jadwal peminjaman di memori.
 *
 * <p>Minggu 7: bentrok, semuaUrutMulai, perAset, hapus. Minggu 9: ajukan (melempar exception).
 * tambah() DISEDIAKAN dan sengaja TIDAK memeriksa apa pun.
 */
public class JadwalPeminjaman {

    private static final Set<StatusPeminjaman> STATUS_TIDAK_MENEMPATI_JADWAL = EnumSet.of(
            StatusPeminjaman.DITOLAK, StatusPeminjaman.DIBATALKAN, StatusPeminjaman.DIKEMBALIKAN);

    private static final Comparator<Peminjaman> URUT_MULAI = Comparator.comparing(Peminjaman::getMulai)
            .thenComparing(Peminjaman::getSelesai)
            .thenComparing(Peminjaman::getId);

    private final List<Peminjaman> daftar = new ArrayList<>();
    private final int kuotaLaptop;

    public JadwalPeminjaman(int kuotaLaptop) {
        this.kuotaLaptop = kuotaLaptop;
    }

    protected int kuotaLaptop() {
        return kuotaLaptop;
    }

    protected List<Peminjaman> daftarMutable() {
        return daftar;
    }

    /** Menambah apa adanya tanpa pemeriksaan. */
    public void tambah(Peminjaman p) {
        daftar.add(p);
    }

    /**
     * Memeriksa apakah {@code baru} bentrok dengan peminjaman aktif pada aset yang sama.
     * Interval waktu diperlakukan setengah terbuka {@code [mulai, selesai)}, sehingga
     * selesai tepat sama dengan mulai tidak dianggap bentrok.
     */
    public boolean bentrok(Peminjaman baru) {
        if (baru == null) {
            throw new IllegalArgumentException("Peminjaman tidak boleh null");
        }
        String kodeAset = baru.getAset().getKode();
        return daftar.stream()
                .filter(ada -> !STATUS_TIDAK_MENEMPATI_JADWAL.contains(ada.getStatus()))
                .filter(ada -> ada.getAset().getKode().equals(kodeAset))
                .anyMatch(ada -> baru.getMulai().isBefore(ada.getSelesai())
                        && ada.getMulai().isBefore(baru.getSelesai()));
    }

    /** Semua peminjaman diurutkan menaik menurut waktu mulai. */
    public List<Peminjaman> semuaUrutMulai() {
        return daftar.stream().sorted(URUT_MULAI).toList();
    }

    /** Peminjaman dikelompokkan menurut kode aset. */
    public Map<String, List<Peminjaman>> perAset() {
        return daftar.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getAset().getKode(), LinkedHashMap::new, Collectors.toList()));
    }

    /** Menghapus peminjaman berdasarkan id. Mengembalikan true jika ada yang terhapus. */
    public boolean hapus(String id) {
        if (id == null || id.isBlank()) {
            return false;
        }
        return daftar.removeIf(p -> p.getId().equals(id));
    }

    /**
     * TODO minggu 9: periksa lalu tambahkan.
     * Bentrok: PeminjamanBentrokException. Peminjam sudah memegang {@link #kuotaLaptop()} laptop yang
     * aktif (status DISETUJUI atau DIPINJAM) dan mengajukan laptop lagi: KuotaHabisException.
     */
    public void ajukan(Peminjaman baru) {
        throw new UnsupportedOperationException("TODO minggu 9: ajukan");
    }
}
