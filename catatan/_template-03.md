# Catatan Minggu 03: Inheritance

## Yang saya kerjakan
Saya membuat hierarki class dengan menetapkan `Aset` sebagai superclass, lalu membuat tiga subclass: `Ruang`, `Laptop`, dan `AlatLab`. Pada setiap subclass, saya menambahkan field spesifik beserta validasinya, memanggil `super()` di dalam konstruktor untuk menggunakan ulang inisialisasi class induk, serta meng-override method `getJenis()` agar mengembalikan representasi string yang sesuai (RUANG, LAPTOP, ALAT_LAB).

## Keputusan desain
Saya memutuskan bahwa@ atribut umum yang dimiliki semua aset seperti `id`, `nama`, dan status peminjaman tetap diletakkan di class induk (`Aset`). Atribut yang spesifik untuk tipe tertentu (seperti `kapasitas` pada ruang atau `nomorInventaris` pada laptop) diletakkan di subclass. 

## Kendala
Tantangan utamanya adalah memastikan validasi data dilakukan di tempat yang tepat agar tidak terjadi duplikasi kode. Saya mengatasinya dengan mendelegasikan pengecekan atribut dasar ke konstruktor `Aset` melalui pemanggilan `super()`, lalu melanjutkan pengecekan atribut spesifik setelahnya di dalam subclass.

## Penggunaan AI
- Alat yang dipakai: Gemini
- Bagian yang dibantu AI: Implementasi boilerplate untuk `Ruang`, `Laptop`, dan `AlatLab`, serta merumuskan contoh konseptual keterbatasan inheritance vs komposisi.
- Prompt utama: Prompt yang berisi instruksi penugasan Minggu 3 dengan detail aturan validasi dan syarat pengumpulan.
- Yang saya ubah atau tolak dari saran AI, dan alasannya: Saya menyesuaikan argumen di pemanggilan `super()` dengan atribut yang persis ada di class `Aset` saya, karena AI hanya membuat parameter asumsi generik (`id`, `nama`).
- Satu hal yang saya pahami lebih baik sekarang: Saya lebih paham bahwa inheritance murni berfungsi untuk hubungan *is-a* (adalah sebuah), dan sangat membatasi jika entitas mulai memiliki karakteristik yang tumpang tindih. Komposisi (*has-a*) sering menjadi solusi yang lebih baik