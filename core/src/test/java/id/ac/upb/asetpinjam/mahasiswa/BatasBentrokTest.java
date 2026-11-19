package id.ac.upb.asetpinjam.mahasiswa;

import static id.ac.upb.asetpinjam.TestSupport.pinjam;
import static org.assertj.core.api.Assertions.assertThat;

import id.ac.upb.asetpinjam.TestSupport;
import id.ac.upb.asetpinjam.config.Varian;
import id.ac.upb.asetpinjam.jadwal.JadwalPeminjaman;
import id.ac.upb.asetpinjam.model.Peminjaman;
import id.ac.upb.asetpinjam.model.StatusPeminjaman;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Tes buatan mahasiswa untuk keputusan batas waktu bentrok dan status jadwal (A-01 dan A-07).
 */
@Tag("week7")
class BatasBentrokTest {

    @Test
    void selesaiSamaDenganMulai() {
        JadwalPeminjaman jadwal = new JadwalPeminjaman(Varian.aktif().kuotaLaptop);
        jadwal.tambah(pinjam("1", TestSupport.peminjam("P1"), TestSupport.ruang("R-1"), 9, 11));

        Peminjaman berikutnya = pinjam("2", TestSupport.peminjam("P2"), TestSupport.ruang("R-1"), 11, 13);
        assertThat(jadwal.bentrok(berikutnya)).isFalse();
    }

    @Test
    void mulaiSamaDenganSelesaiTerbalik() {
        JadwalPeminjaman jadwal = new JadwalPeminjaman(Varian.aktif().kuotaLaptop);
        jadwal.tambah(pinjam("1", TestSupport.peminjam("P1"), TestSupport.ruang("R-1"), 11, 13));

        Peminjaman sebelumnya = pinjam("2", TestSupport.peminjam("P2"), TestSupport.ruang("R-1"), 9, 11);
        assertThat(jadwal.bentrok(sebelumnya)).isFalse();
    }

    @Test
    void rentangSamaPersisBentrok() {
        JadwalPeminjaman jadwal = new JadwalPeminjaman(Varian.aktif().kuotaLaptop);
        jadwal.tambah(pinjam("1", TestSupport.peminjam("P1"), TestSupport.ruang("R-1"), 9, 11));

        Peminjaman samaPersis = pinjam("2", TestSupport.peminjam("P2"), TestSupport.ruang("R-1"), 9, 11);
        assertThat(jadwal.bentrok(samaPersis)).isTrue();
    }

    @Test
    void statusDitolakDibatalkanDanDikembalikanTidakMenghalangiJadwal() {
        JadwalPeminjaman jadwal = new JadwalPeminjaman(Varian.aktif().kuotaLaptop);

        Peminjaman ditolak = pinjam("1", TestSupport.peminjam("P1"), TestSupport.ruang("R-1"), 9, 11);
        ditolak.setStatusDariPenyimpanan(StatusPeminjaman.DITOLAK);
        jadwal.tambah(ditolak);

        Peminjaman dibatalkan = pinjam("2", TestSupport.peminjam("P2"), TestSupport.ruang("R-2"), 9, 11);
        dibatalkan.setStatusDariPenyimpanan(StatusPeminjaman.DIBATALKAN);
        jadwal.tambah(dibatalkan);

        Peminjaman dikembalikan = pinjam("3", TestSupport.peminjam("P3"), TestSupport.ruang("R-3"), 9, 11);
        dikembalikan.setStatusDariPenyimpanan(StatusPeminjaman.DIKEMBALIKAN);
        jadwal.tambah(dikembalikan);

        assertThat(jadwal.bentrok(pinjam("4", TestSupport.peminjam("P4"), TestSupport.ruang("R-1"), 9, 11)))
                .isFalse();
        assertThat(jadwal.bentrok(pinjam("5", TestSupport.peminjam("P4"), TestSupport.ruang("R-2"), 9, 11)))
                .isFalse();
        assertThat(jadwal.bentrok(pinjam("6", TestSupport.peminjam("P4"), TestSupport.ruang("R-3"), 9, 11)))
                .isFalse();
    }
}
