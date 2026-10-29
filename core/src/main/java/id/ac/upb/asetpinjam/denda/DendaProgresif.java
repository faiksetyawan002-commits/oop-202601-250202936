package id.ac.upb.asetpinjam.denda;

import id.ac.upb.asetpinjam.kontrak.AturanDenda;
import id.ac.upb.asetpinjam.model.Aset;

/**
 * Minggu 5: denda yang makin berat bila makin lama terlambat.
 *
 * <p>Rumus progresifnya KEPUTUSAN ANDA (catat di docs/asumsi.md). Syarat minimal yang diuji:
 * 0 hari = 0; tidak pernah lebih murah dari DendaFlat; tidak pernah turun saat hari bertambah.
 */
public class DendaProgresif implements AturanDenda {

    /** Sampai hari ini denda masih sama dengan denda flat. */
    private static final int HARI_DENDA_FLAT = 7;

    @Override
    public long hitung(Aset aset, int hariTerlambat) {
        long dendaFlat = aset.hitungDenda(hariTerlambat);
        if (hariTerlambat <= HARI_DENDA_FLAT) {
            return dendaFlat;
        }

        // Hari ke-8 dan seterusnya dikenai tarif harian tambahan,
        // sehingga totalnya selalu di atas denda flat dan terus naik.
        long tarifHarian = aset.hitungDenda(1);
        long hariTambahan = hariTerlambat - HARI_DENDA_FLAT;
        return dendaFlat + (hariTambahan * tarifHarian);
    }
}
