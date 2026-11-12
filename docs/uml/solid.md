# Analisis Prinsip SOLID pada Kode Aset-Pinjam

Dokumen ini menjelaskan penerapan prinsip **SOLID** yang terlihat secara nyata pada arsitektur dan implementasi kode di `core/src/main/java/id/ac/upb/asetpinjam/`, beserta perbaikan (*refactor*) kecil yang dilakukan pada Minggu 6.

---

## 1. Open-Closed Principle (OCP)

> *Software entities should be open for extension, but closed for modification.*

### Kelas yang Menerapkan
- `id.ac.upb.asetpinjam.kontrak.AturanDenda` (interface)
- `id.ac.upb.asetpinjam.denda.DendaFlat` dan `id.ac.upb.asetpinjam.denda.DendaProgresif`
- `id.ac.upb.asetpinjam.denda.LayananDenda`
- `id.ac.upb.asetpinjam.model.Aset` beserta subclass `Ruang`, `Laptop`, dan `AlatLab`

### Alasan & Penerapan di Kode
1. **Ekstensi Aturan Denda**: Jika kampus menambah skema denda baru (misalnya denda hari libur atau denda mahasiswa tingkat akhir), pengembang cukup membuat class baru yang mengimplementasikan interface `AturanDenda` (seperti halnya `DendaFlat` dan `DendaProgresif` yang sudah ada) tanpa perlu memodifikasi `LayananDenda` maupun kode aturan denda lainnya.
2. **Ekstensi Jenis Aset**: `Aset` mendefinisikan method abstrak `getJenis()` dan `hitungDenda(int hariTerlambat)` serta helper `hitungDendaDenganMultiplier(int, long)`. Penambahan jenis aset baru (misalnya `Proyektor` atau `Kendaraan`) cukup mewarisi `Aset` tanpa harus mengubah kode di `KatalogAset`, `Peminjaman`, atau implementasi `AturanDenda`.

---

## 2. Dependency Inversion Principle (DIP)

> *High-level modules should not depend on low-level modules. Both should depend on abstractions.*

### Kelas yang Menerapkan
- `id.ac.upb.asetpinjam.denda.LayananDenda` -> bergantung pada interface `AturanDenda` (bukan langsung pada `DendaFlat` atau `DendaProgresif`)
- `id.ac.upb.asetpinjam.dao.JdbcPeminjamanDAO` -> bergantung pada interface `AsetDAO` (bukan langsung pada `JdbcAsetDAO`)
- Kontrak peminjaman -> bergantung pada abstraksi `DapatDipinjam` dan `Aset`

### Alasan & Penerapan di Kode
1. **Constructor Injection**: `LayananDenda(AturanDenda aturan)` dan `JdbcPeminjamanDAO(Supplier<Connection>, AsetDAO)` menerima abstraksi melalui konstruktor, bukan melakukan instansiasi `new DendaFlat()` atau `new JdbcAsetDAO(...)` di dalam class.
2. **Meningkatkan Testability**: Karena ketergantungan disuntikkan via interface, pengujian unit terhadap `LayananDenda` maupun layanan bisnis tidak terikat pada satu algoritma konkret atau koneksi database asli. Kita dapat menyuntikkan lambda/stub (contoh: `(aset, hari) -> 42L`) atau implementasi tiruan (*mock*) secara langsung.

---

## 3. Single Responsibility Principle (SRP)

> *A class should have one, and only one, reason to change.*

### Kelas yang Menerapkan
- `id.ac.upb.asetpinjam.model.Aset` & subclass (`Ruang`, `Laptop`, `AlatLab`)
- `id.ac.upb.asetpinjam.katalog.KatalogAset`
- `id.ac.upb.asetpinjam.denda.DendaFlat` & `DendaProgresif`
- `id.ac.upb.asetpinjam.jadwal.JadwalPeminjaman`

### Alasan & Penerapan di Kode
- `Aset` hanya mengurus state aset (`kode`, `nama`, `status`, `kondisi`), validasi invarian aset, dan status kelayakan pinjam.
- `KatalogAset` hanya mengurus penyimpanan koleksi serta mekanisme pencarian aset (`cari`).
- `DendaFlat` dan `DendaProgresif` masing-masing hanya memiliki satu tanggung jawab: menghitung nominal denda sesuai strateginya.
- `JadwalPeminjaman` berfokus pada pencatatan dan validasi jadwal peminjaman.

---

## 4. Liskov Substitution Principle (LSP) & Interface Segregation Principle (ISP)

- **LSP**: Seluruh subclass `Aset` (`Ruang`, `Laptop`, `AlatLab`) dapat menggantikan tipe `Aset` di dalam `KatalogAset`, `Peminjaman`, dan `AturanDenda.hitung(Aset, int)` tanpa merusak ekspektasi kontrak (misal: `hari < 0` selalu melempar `IllegalArgumentException`, `0` hari selalu mengembalikan `0L`, dan `bisaDipinjam()` konsisten mengecek `TERSEDIA` serta bukan `RUSAK_BERAT`).
- **ISP**: Interface dibuat ramping dan fokus pada satu peran (`DapatDipinjam` hanya berisi `bisaDipinjam()`; `AturanDenda` hanya berisi `hitung(Aset, int)`), sehingga implementor tidak dipaksa mengimplementasikan method yang tidak relevan.

---

## Ringkasan Refactor Kecil (Minggu 6)

Untuk memperjelas penerapan prinsip SOLID dan menjaga konsistensi validasi kontrak di kode:
1. **`Aset.java`**: Mengekstrak logika pengecekan string null/kosong yang sebelumnya berulang di konstruktor dan `setNama` ke dalam method helper `private static void validasiTidakKosong(String nilai, String namaField)` agar validasi tetap terpusat (*DRY*).
2. **`DendaFlat.java` & `DendaProgresif.java`**: Menambahkan validasi eksplisit `if (aset == null) throw new IllegalArgumentException("Aset tidak boleh null");` pada kedua implementasi `AturanDenda` agar kontrak prasyarat interface konsisten di semua kelas turunan (**LSP**).
3. **`AlatLab.java`**: Menyelaraskan validasi input `laboratorium` menggunakan `.trim().isEmpty()` seperti pada `Laptop` dan `Aset` sehingga string berisi spasi saja ditolak secara seragam.
