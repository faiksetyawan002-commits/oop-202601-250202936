# Asumsi dan Keputusan Desain

Isi bagian ini sendiri. Satu entri per keputusan. Dinilai dari kejelasan alasan dan konsistensi dengan kode dan tes Anda,
bukan dari jawaban "benar".

Format tiap entri:

```
## A-NN: judul singkat
- Pertanyaan: ...
- Keputusan: ...
- Alasan: ...
- Dampak ke kode: ... (kelas/metode)
- Tes yang membuktikan: ... (nama tes di folder mahasiswa/)
```

## Aturan ambigu dari docs/aturan-bisnis.md

Salin setiap pertanyaan ambigu (nomor 1 sampai 7) ke sini dan jawab. Contoh:

## A-01: batas waktu bentrok
- Pertanyaan: 09:00-11:00 dan 11:00-13:00 pada aset yang sama bentrok atau tidak?
- Keputusan: (isi)
- Alasan: (isi)
- Dampak ke kode: (isi)
- Tes yang membuktikan: (isi)

## Asumsi lain

Tambahkan di sini keputusan lain yang Anda ambil: rumus denda progresif, perubahan skema database, format error API, dan seterusnya.

## A-02: normalisasi kode dan nama aset
- Pertanyaan: Apakah kode dan nama dinormalisasi (trim, huruf besar)?
- Keputusan: Kode dan nama divalidasi tidak boleh null atau kosong (termasuk hanya spasi/whitespace dengan `.trim().isEmpty()`), namun nilai aslinya disimpan tanpa normalisasi huruf besar atau pemotongan karakter.
- Alasan: Menjaga integritas representasi data sesuai input asli pemanggil tanpa membuat asumsi format casing atau penomoran instansi.
- Dampak ke kode: Konstruktor `Aset(String, String)` dan metode `setNama(String)` pada `id.ac.upb.asetpinjam.model.Aset`.
- Tes yang membuktikan: `Minggu02AsetTest.menyimpanKodeDanNamaDenganNilaiAwal`, `Minggu02AsetTest.kodeKosongDitolak`, `Minggu02AsetTest.namaKosongDitolak`.

## A-05: rumus denda progresif
- Pertanyaan: Bagaimana denda progresif dihitung dibandingkan denda flat?
- Keputusan: Hari ke-0 sampai ke-7 memakai denda flat. Mulai hari ke-8, denda = denda flat + `(hari terlambat - 7) x denda satu hari aset tersebut`. Dengan begitu tarif harian menjadi dua kali lipat setelah minggu pertama.
- Alasan: Rumus ini sederhana, selalu tidak lebih murah dari denda flat, tidak pernah turun ketika hari bertambah, dan pada hari ke-30 pasti lebih mahal dari denda flat untuk semua jenis aset.
- Dampak ke kode: `DendaFlat.hitung` dan `DendaProgresif.hitung`.
- Tes yang membuktikan: `Minggu05AbstraksiTest.dendaFlatSamaDenganHitungDendaAset`, `Minggu05AbstraksiTest.dendaProgresifMemenuhiSyaratMinimal`.
