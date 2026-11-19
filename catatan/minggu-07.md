# Catatan Minggu 07: Koleksi

## Yang saya kerjakan
Pada Minggu 7, saya mengimplementasikan empat method koleksi dan penjadwalan pada kelas `JadwalPeminjaman`, yaitu `bentrok(Peminjaman)`, `semuaUrutMulai()`, `perAset()`, dan `hapus(String)`. Saya juga menulis pengujian batas waktu bentrok serta status peminjaman pada `core/src/test/java/id/ac/upb/asetpinjam/mahasiswa/BatasBentrokTest.java` dan mendokumentasikan keputusan batas waktu beserta status jadwal pada `docs/asumsi.md` (bagian A-01 dan A-07).

## Keputusan desain
1. **Struktur data internal**: Saya mempertahankan `List<Peminjaman> daftar` sebagai penyimpanan utama (*single source of truth*) karena method `tambah(Peminjaman)` dan `daftarMutable()` sudah disediakan dosen berbasis `List`. Menambah `Map` terpisah sebagai penyimpanan paralel berisiko menyebabkan ketidaksinkronan jika ada pemanggilan langsung ke `tambah()` atau `daftarMutable()`. Untuk kebutuhan spesifik:
   - `Set<StatusPeminjaman>` (`EnumSet.of(DITOLAK, DIBATALKAN, DIKEMBALIKAN)`) dipakai pada `bentrok()` untuk mengecek status yang tidak lagi menempati jadwal secara efisien.
   - `Comparator.comparing(Peminjaman::getMulai).thenComparing(Peminjaman::getSelesai).thenComparing(Peminjaman::getId)` dipakai di `semuaUrutMulai()` dan mengembalikan `List` baru tanpa mengubah urutan asli `daftar`.
   - `Map<String, List<Peminjaman>>` (`LinkedHashMap`) dibangun di `perAset()` menggunakan `Collectors.groupingBy` berdasarkan `getAset().getKode()`.
2. **Keputusan batas waktu (A-01) dan status jadwal (A-07)**: Rentang waktu peminjaman diperlakukan sebagai interval setengah terbuka `[mulai, selesai)`, sehingga peminjaman 09:00–11:00 dan 11:00–13:00 pada aset yang sama **tidak bentrok** (`baru.mulai < ada.selesai && ada.mulai < baru.selesai`). Peminjaman berstatus `DITOLAK`, `DIBATALKAN`, dan `DIKEMBALIKAN` juga tidak dianggap menempati jadwal pada pengecekan `bentrok()`.

## Kendala
Kendala utama adalah memastikan logika pengecekan irisan rentang waktu menangani seluruh kasus (tumpang tindih sebagian, terkandung penuh, berurutan tepat di batas waktu, dan terpisah jauh) tanpa menyisakan celah kondisi terbalik, serta memastikan `semuaUrutMulai()` mengembalikan salinan daftar yang tidak memodifikasi penyimpanan internal `daftar`. Masalah ini diselesaikan dengan rumus irisan simetris dua interval setengah terbuka (`baru.mulai.isBefore(ada.selesai) && ada.mulai.isBefore(baru.selesai)`), menguji kedua arah urutan waktu di `BatasBentrokTest`, serta menggunakan `.stream().sorted(URUT_MULAI).toList()`.

## Penggunaan AI
- Alat yang dipakai: Antigravity (Google Gemini)
- Bagian yang dibantu AI: Implementasi `bentrok`, `semuaUrutMulai`, `perAset`, dan `hapus` di `JadwalPeminjaman`, penulisan tes di `BatasBentrokTest`, pengisian `docs/asumsi.md`, serta penyusunan `catatan/minggu-07.md`.
- Prompt utama:
  1. Implementasikan method `bentrok`, `semuaUrutMulai`, `perAset`, dan `hapus` pada `JadwalPeminjaman` dengan aturan interval waktu setengah terbuka `[mulai, selesai)` dan pengabaian status tidak aktif.
  2. Buat unit test JUnit 5 di `BatasBentrokTest` untuk menguji kasus batas waktu bentrok (persis bersambung, tumpang tindih sebagian, terkandung penuh) dan variasi status peminjaman.
- Yang saya ubah atau tolak dari saran AI, dan alasannya: Tetap memakai satu `List<Peminjaman>` sebagai sumber data tunggal dibandingkan menyimpan `Map` duplikat di level field agar tidak berbenturan dengan `tambah()` dan `daftarMutable()` bawaan dosen, serta melengkapi `Comparator` pada `semuaUrutMulai()` dengan *tie-breaker* `getSelesai()` dan `getId()` agar pengurutan stabil untuk waktu mulai yang sama.
- Satu hal yang saya pahami lebih baik sekarang: Cara memilih koleksi Java yang tepat sesuai kebutuhan (`List` untuk urutan daftar, `EnumSet`/`Set` untuk himpunan status unik, dan `LinkedHashMap`/`Map` untuk pengelompokan per kunci), cara merangkai `Comparator` dengan `thenComparing`, serta cara memodelkan irisan jadwal sebagai interval setengah terbuka `[mulai, selesai)`.
