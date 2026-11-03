# Catatan Minggu 05: Abstraction

## Yang saya kerjakan
Saya menerapkan konsep abstraksi dengan mengisi method `bisaDipinjam()` pada class `Aset` agar hanya mengembalikan `true` saat status bernilai `TERSEDIA` dan kondisi bukan `RUSAK_BERAT`. Selanjutnya, saya mengubah class `Aset` menjadi `abstract class` serta menjadikan method `hitungDenda(int)` sebagai `abstract method` sehingga setiap subclass diwajibkan menyediakan implementasi perhitungannya masing-masing. Terakhir, saya mengimplementasikan interface `AturanDenda` pada class `DendaFlat` dan `DendaProgresif`.

## Keputusan desain
Untuk `DendaFlat`, perhitungan langsung didelegasikan ke method `aset.hitungDenda(hariTerlambat)`. Sedangkan untuk `DendaProgresif`, saya menerapkan skema bertahap: hari ke-0 hingga ke-7 mengikuti tarif denda flat, lalu mulai hari ke-8 dan seterusnya dikenakan tarif denda harian tambahan (`(hari - 7) x denda satu hari`). Keputusan ini dipilih karena rumusnya konsisten, tidak pernah lebih murah dari denda flat, nilainya monoton naik, dan pada hari ke-30 dipastikan lebih mahal dari denda flat. Keputusan ini dicatat di `docs/asumsi.md` pada bagian A-05.

## Kendala
Kendala yang dihadapi adalah merancang rumus denda progresif yang memenuhi seluruh batas pengujian tanpa menghasilkan nilai yang turun atau lebih murah daripada denda flat, serta memastikan class `Aset` yang diubah menjadi abstract tidak merusak pengujian minggu sebelumnya. Kendala ini diselesaikan dengan memanfaatkan delegasi `TestSupport` ke subclass konkret (`Ruang`) pada pengujian dan memilih formula penambahan tarif harian linier bertahap setelah batas 7 hari.

## Penggunaan AI
- Alat yang dipakai: Google Gemini
- Bagian yang dibantu AI: Logika `Aset.bisaDipinjam()`, abstraksi method `hitungDenda()`, serta perumusan logika perhitungan `DendaFlat` dan `DendaProgresif`
- Prompt utama:
  1. `"Implementasikan method bisaDipinjam() pada class Aset agar hanya true jika status TERSEDIA dan kondisi bukan RUSAK_BERAT, lalu jadikan class Aset dan method hitungDenda(int) menjadi abstract."`
  2. `"Buatkan implementasi interface AturanDenda pada DendaFlat dan DendaProgresif dengan syarat: 0 hari = 0, DendaFlat mendelegasikan ke hitungDenda aset, dan DendaProgresif tidak pernah lebih murah dari flat serta lebih mahal pada hari ke-30."`
- Yang saya ubah atau tolak dari saran AI, dan alasannya: Saya menolak saran AI yang menggunakan rumus eksponensial/pangkat untuk denda progresif karena berisiko mengalami *integer overflow* dan sulit diprediksi nilainya; saya memilih rumus penambahan linier bertahap setelah hari ke-7 yang jauh lebih stabil dan mudah diverifikasi.
- Satu hal yang saya pahami lebih baik sekarang: `abstract class` digunakan ketika kelas-kelas turunan berbagi atribut state dan perilaku dasar yang sama (hubungan *is-a*), sedangkan `interface` seperti `AturanDenda` digunakan untuk mendefinisikan kontrak perilaku yang dapat dipertukarkan tanpa bergantung pada hierarki pewarisan (pola Strategy).
