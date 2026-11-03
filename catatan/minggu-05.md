# Catatan Minggu 05: Abstraction

## Yang saya kerjakan

Saya mengisi `Aset.bisaDipinjam()` agar hanya mengembalikan `true` ketika status aset `TERSEDIA` dan kondisinya bukan `RUSAK_BERAT`. Saya juga menjadikan `Aset` sebagai abstract class dan membuat `hitungDenda(int)` abstrak agar setiap subclass wajib menyediakan perhitungan dendanya sendiri. Selanjutnya saya mengimplementasikan `DendaFlat` dan `DendaProgresif` sebagai dua aturan denda yang memakai kontrak `AturanDenda`.

## Keputusan desain

Untuk `DendaFlat`, hasilnya langsung didelegasikan ke `aset.hitungDenda(hari)`. Untuk `DendaProgresif`, saya memilih rumus bertahap: hari ke-0 sampai ke-7 sama dengan denda flat, lalu mulai hari ke-8 ditambah `(hari - 7) x denda satu hari`. Rumus ini dipilih karena mudah dipahami, tidak pernah lebih murah dari denda flat, nilainya tidak pernah turun, dan pada hari ke-30 pasti lebih mahal. Keputusan ini dicatat di `docs/asumsi.md` bagian A-05.

## Kendala

Awalnya `Aset` sudah memiliki method `hitungDenda` yang konkret, sehingga subclass belum benar-benar diwajibkan melakukan override. Saya mengubahnya menjadi abstract dan menambahkan `@Override` di `Ruang`, `Laptop`, dan `AlatLab` supaya kesalahan tanda tangan method cepat terdeteksi saat kompilasi.

## Penggunaan AI

- Alat yang dipakai: OpenCode (AI coding agent)
- Bagian yang dibantu AI: `Aset.bisaDipinjam()`, `Aset.hitungDenda()`, `DendaFlat.hitung()`, dan `DendaProgresif.hitung()`
- Prompt utama: 
- Yang saya ubah atau tolak dari saran AI, dan alasannya: Saya memilih rumus progresif sederhana dengan batas 7 hari, bukan rumus eksponensial, karena lebih mudah diuji dan tetap memenuhi seluruh syarat minimal tes.
- Satu hal yang saya pahami lebih baik sekarang: Abstract class dipakai ketika subclass berbagi state dan perilaku dasar seperti `Aset`, sedangkan interface seperti `AturanDenda` dipakai untuk kontrak perilaku yang bisa diganti-ganti.
