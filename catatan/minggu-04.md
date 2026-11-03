# Catatan Minggu 04: Polymorphism

## Yang saya kerjakan
Saya menerapkan konsep polymorphism melalui overriding dan overloading pada sistem aset-pinjam. Pada bagian overriding, saya mengimplementasikan method `hitungDenda(int)` pada subclass `Ruang` (multiplier 1x), `Laptop` (multiplier 2x), dan `AlatLab` (multiplier 4x) yang dieksekusi secara polimorfis (*dynamic binding*) saat dipanggil melalui tipe induk `Aset`. Pada bagian overloading, saya membuat dua variasi method `cari` di `KatalogAset`: pencarian berdasarkan kata kunci nama secara *case-insensitive*, serta pencarian dengan tambahan filter `JenisAset`.

## Keputusan desain
Seluruh aturan perhitungan denda dan pencarian katalog sudah ditentukan secara pasti dalam panduan praktikum sehingga tidak ada penambahan asumsi di `docs/asumsi.md`. Untuk menghindari duplikasi kode (*DRY*), rumus dasar denda dan validasi hari negatif diletakkan di class induk `Aset` melalui method `hitungDendaDenganMultiplier`, sehingga subclass hanya perlu meneruskan multiplier masing-masing. Pada `KatalogAset`, overload `cari(kata, jenis)` memanfaatkan hasil dari `cari(kata)` lalu menyaringnya berdasarkan `JenisAset` agar logika pencocokan teks nama tidak berulang.

## Kendala
Kendala yang dihadapi adalah memastikan pencarian string pada `KatalogAset` bersifat *case-insensitive* serta menangani kemungkinan masukan `null` atau string kosong pada parameter `kata` dan `jenis`. Solusinya adalah menambahkan validasi `IllegalArgumentException` di awal method dan mengonversi string ke huruf kecil (`toLowerCase()`) sebelum mencocokkan teks dengan method `contains()`.

## Penggunaan AI
- Alat yang dipakai: Google Gemini
- Bagian yang dibantu AI: Penulisan method `hitungDenda` di subclass `Aset` dan pembuatan method overloading `cari` pada `KatalogAset`
- Prompt utama:
  1. Tolong buatkan implementasi method hitungDenda(int hariTerlambat) di subclass Ruang, Laptop, dan AlatLab dengan multiplier 1x, 2x, dan 4x dari tarif varian, serta tolak hari negatif dengan IllegalArgumentException.
  2. Buatkan dua method overloading cari di class KatalogAset: cari(String kata) untuk pencarian nama case-insensitive dan cari(String kata, JenisAset jenis) yang hanya menyaring jenis aset tertentu.
  
- Yang saya ubah atau tolak dari saran AI, dan alasannya: Saran awal AI menuliskan ulang perulangan pencarian nama pada overload `cari(kata, jenis)`. Saya mengubahnya agar `cari(kata, jenis)` memanggil method `cari(kata)` terlebih dahulu lalu memfilter jenisnya menggunakan stream agar kode lebih modular dan tidak menduplikasi validasi teks.
- Satu hal yang saya pahami lebih baik sekarang: Perbedaan nyata antara *overriding* (polimorfisme runtime di mana method yang dieksekusi ditentukan oleh objek konkret saat berjalan) dan *overloading* (polimorfisme compile-time di mana penentuan method didasarkan pada tanda tangan parameter saat kompilasi).
