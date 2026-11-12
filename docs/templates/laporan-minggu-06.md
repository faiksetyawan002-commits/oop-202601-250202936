# Laporan Singkat Minggu 6: UML dan SOLID

## 1. Tujuan dan Ruang Lingkup
Pada Minggu 6, tujuan praktikum adalah memodelkan sistem **Aset-Pinjam** ke dalam diagram UML yang konsisten dengan kode sumber aktual serta mendokumentasikan penerapan prinsip **SOLID** di dalam kode. Ruang lingkup pekerjaan meliputi:
- Penyusunan **Class Diagram** (`docs/uml/class-diagram.mmd` dan `docs/uml/class-diagram.md`) yang memuat seluruh paket, kelas/interface/enum/record, atribut beserta tipe dan *visibility*, method, relasi, dan *multiplicity*.
- Penyusunan **Sequence Diagram** (`docs/uml/sequence-peminjaman.mmd` dan `docs/uml/sequence-peminjaman.md`) untuk alur pengajuan peminjaman pada skenario sukses dan skenario bentrok dengan fragmen `alt`.
- Penyusunan dokumen analisis prinsip **SOLID** (`docs/uml/solid.md`) dan *refactor* kecil di kode untuk menonjolkan penerapan prinsip desain tersebut.
- Penyusunan catatan refleksi praktikum (`catatan/minggu-06.md`).

---

## 2. Desain
### A. Class Diagram
Arsitektur dibagi menjadi 9 paket utama (`kontrak`, `model`, `denda`, `katalog`, `jadwal`, `config`, `factory`, `dao`, dan `exception`). Kelas abstrak `Aset` mengimplementasikan interface `DapatDipinjam` dan diturunkan menjadi `Ruang`, `Laptop`, dan `AlatLab`. Hubungan `KatalogAset` dengan `Aset` serta `JadwalPeminjaman` dengan `Peminjaman` dimodelkan sebagai **aggregation** (`o--`) dengan *multiplicity* `1` ke `0..*`, karena objek `Aset` dan `Peminjaman` diinstansiasi di luar wadahnya dan dapat berdiri sendiri secara independen.

### B. Sequence Diagram Pengajuan Peminjaman
Alur pengajuan dimulai ketika `Peminjam` mencari aset melalui `KatalogAset.cari(kata)`, menginstansiasi objek `Peminjaman` (status awal `DIAJUKAN`), lalu memanggil `JadwalPeminjaman.ajukan(baru)`. Di dalam `ajukan()`, sistem memeriksa konflik jadwal via `bentrok(baru)` dan menggunakan fragmen `alt`:
- **Skenario Bentrok (`bentrok == true`)**: `JadwalPeminjaman` melempar `PeminjamanBentrokException` dan objek tidak dimasukkan ke `daftar`.
- **Skenario Sukses (`tidak bentrok`)**: Khusus `JenisAset.LAPTOP`, sistem memastikan jumlah laptop aktif tidak melebihi `kuotaLaptop()` (atau melempar `KuotaHabisException`), kemudian memanggil `tambah(baru)` untuk menyimpan peminjaman ke dalam `daftar`.

### C. Penerapan Prinsip SOLID dan Refactor Kecil
1. **Open-Closed Principle (OCP)**: Interface `AturanDenda` dan kelas abstrak `Aset` memungkinkan penambahan aturan denda baru (mis. `DendaProgresif`) dan jenis aset baru tanpa memodifikasi kode pemanggil (`LayananDenda`, `KatalogAset`, `Peminjaman`).
2. **Dependency Inversion Principle (DIP)**: `LayananDenda` dan `JdbcPeminjamanDAO` bergantung pada abstraksi (`AturanDenda` dan `AsetDAO`) yang disuntikkan melalui konstruktor, bukan kelas konkret, sehingga sangat meningkatkan *testability*.
3. **SRP, LSP, & ISP**: Pemisahan tanggung jawab per kelas, interface yang ramping (`DapatDipinjam` dan `AturanDenda`), serta *refactor* kecil:
   - Ekstraksi `validasiTidakKosong(String, String)` di `Aset` agar validasi string null/kosong tidak terduplikasi antara konstruktor dan `setNama`.
   - Penambahan validasi `aset == null` di `DendaFlat` dan `DendaProgresif` demi konsistensi kontrak **LSP**.
   - Penyeragaman `.trim().isEmpty()` untuk validasi `laboratorium` pada `AlatLab`.

---

## 3. Hasil
Seluruh kompilasi dan pengujian otomatis dari Minggu 1 hingga Minggu 6 berjalan hijau tanpa error atau kegagalan menggunakan perintah `.\scripts\uji.ps1` maupun `mvn -B -ntp clean test "-Dgroups=week1 | week2 | week3 | week4 | week5 | week6"`:

```text
[INFO] Running id.ac.upb.asetpinjam.Minggu01Test
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running id.ac.upb.asetpinjam.Minggu02AsetTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running id.ac.upb.asetpinjam.Minggu03HierarkiTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running id.ac.upb.asetpinjam.Minggu04PolimorfismeTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running id.ac.upb.asetpinjam.Minggu05AbstraksiTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running id.ac.upb.asetpinjam.Minggu06DokumenTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] Results:
[INFO] Tests run: 35, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

*(Tautan commit dapat diperbarui setelah `git commit` dengan format `minggu-06: ...` dan `git push` dilakukan.)*

---

## 4. Keputusan dan Asumsi
- Keputusan desain terkait normalisasi input `Aset` (validasi tanpa mengubah format teks asli) dan rumus `DendaProgresif` (bertahap setelah hari ke-7) tetap merujuk pada [`docs/asumsi.md`](asumsi.md) bagian **A-02** dan **A-05**.
- Untuk Minggu 6, diputuskan menggunakan **Mermaid** (`.mmd` disertai dokumen `.md`) agar sumber diagram dapat di-*version control* dengan rapi dan langsung dirender oleh GitHub. Prinsip SOLID yang difokuskan adalah **OCP** dan **DIP** (disertai **SRP**, **LSP**, dan **ISP**) sebagaimana didokumentasikan pada [`docs/uml/solid.md`](uml/solid.md).

---

## 5. Kendala dan Solusi
1. **Menjaga konsistensi diagram dengan kode nyata**: Agar diagram tidak menjadi sketsa ideal semata, seluruh 33 berkas sumber Java diperiksa langsung (nama paket, *visibility* `-`/`+`/`#`, tipe kembalian, dan generik) sebelum dituangkan ke dalam Class dan Sequence Diagram.
2. **Penentuan relasi aggregation vs composition di UML**: Relasi wadah koleksi seperti `KatalogAset` dan `JadwalPeminjaman` terhadap isinya sempat berpotensi keliru sebagai composition; hal ini diselesaikan dengan meninjau siklus hidup objek di kode (`tambah(Aset)` / `ajukan(Peminjaman)` menerima referensi dari luar), sehingga dimodelkan tepat sebagai aggregation (`o--`).

---

## 6. Refleksi
Jika mengulang pengerjaan modul ini dari awal, saya akan menyertakan diagram UML sejak tahap awal desain kelas (minggu 2–3) dan memperbaruinya secara inkremental setiap minggu, sehingga evaluasi prinsip SOLID (khususnya pemisahan tanggung jawab dan ketergantungan antar paket) dapat terdeteksi lebih cepat sebelum kode berkembang semakin besar.
