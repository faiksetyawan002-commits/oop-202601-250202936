# Catatan Minggu 06: UML dan SOLID

## Yang saya kerjakan
Saya memodelkan arsitektur sistem Aset-Pinjam ke dalam Class Diagram (`docs/uml/class-diagram.mmd` dan `docs/uml/class-diagram.md`) yang mencakup seluruh paket, atribut, method, tipe, visibility, relasi, dan multiplicity sesuai kode sumber aktual. Saya juga membuat Sequence Diagram pengajuan peminjaman (`docs/uml/sequence-peminjaman.mmd` dan `docs/uml/sequence-peminjaman.md`) menggunakan fragmen `alt` untuk memisahkan skenario bentrok dan skenario sukses. Selain itu, saya menyusun analisis prinsip SOLID pada `docs/uml/solid.md` dan melakukan refactor kecil pada `Aset`, `AlatLab`, `DendaFlat`, serta `DendaProgresif` untuk memperjelas penerapan prinsip SRP, LSP, OCP, dan DIP.

## Keputusan desain
Saya memilih format Mermaid (`.mmd` disertai dokumen Markdown `.md`) agar diagram memiliki berkas sumber sekaligus dapat langsung dirender di GitHub dan IDE. Pada analisis SOLID, saya menyoroti **Open-Closed Principle (OCP)** melalui interface `AturanDenda` dan kelas abstrak `Aset`, serta **Dependency Inversion Principle (DIP)** melalui injeksi abstraksi `AturanDenda` pada `LayananDenda` dan `AsetDAO` pada `JdbcPeminjamanDAO`, dilengkapi dengan penjelasan **SRP**, **LSP**, dan **ISP**. Saya juga mengekstrak helper `validasiTidakKosong` di `Aset` dan menambahkan validasi null yang seragam di implementasi `AturanDenda`.

## Kendala
Kendala utama adalah memastikan seluruh detail diagram (nama kelas, tipe generik, visibility `#`/`-`/`+`, arah panah relasi, serta multiplicity) benar-benar selaras dengan kode Java yang ada dan tidak sekadar sketsa konseptual. Hal ini diatasi dengan memverifikasi seluruh file sumber pada paket `model`, `kontrak`, `denda`, `katalog`, `jadwal`, `config`, `factory`, `dao`, dan `exception` satu per satu sebelum memetakan diagram.

## Penggunaan AI
- Alat yang dipakai: Google Gemini
- Bagian yang dibantu AI: Penyusunan Mermaid Class Diagram, Sequence Diagram pengajuan peminjaman, analisis prinsip SOLID pada `docs/uml/solid.md`, refactor kecil validasi, serta penyusunan laporan minggu 6
- Prompt utama:
  1. `"Buatkan Class Diagram dan Sequence Diagram pengajuan peminjaman (fragmen alt untuk skenario sukses dan bentrok) dengan Mermaid yang konsisten dengan seluruh kode Java Aset-Pinjam."`
  2. `"Tunjukkan minimal dua prinsip SOLID di kode Aset-Pinjam, dokumentasikan di docs/uml/solid.md beserta alasan dan kelas terkait, lalu lakukan refactor kecil bila perlu."`
- Yang saya ubah atau tolak dari saran AI, dan alasannya: Saya memastikan relasi `KatalogAset` terhadap `Aset` dan `JadwalPeminjaman` terhadap `Peminjaman` dimodelkan sebagai aggregation (`o--`), bukan composition (`*--`), karena objek `Aset` dan `Peminjaman` dibuat di luar wadah tersebut dan siklus hidupnya independen. Saya juga meninjau ulang seluruh tanda tangan method dan visibility di Class Diagram agar sama persis dengan kode sumber.
- Satu hal yang saya pahami lebih baik sekarang: Perbedaan antara aggregation dan composition pada pemodelan UML, mengapa OCP memudahkan penambahan fitur baru tanpa mengubah kode lama yang sudah teruji, serta bagaimana DIP meningkatkan *testability* saat pengujian unit.
