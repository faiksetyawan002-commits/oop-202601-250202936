# Catatan Minggu 04: Polymorphism

## Yang saya kerjakan

Saya mengimplementasikan konsep polimorfisme melalui mekanisme *method overriding* dan *method overloading*. Pada class `Aset` dan ketiga subclass-nya (`Ruang`, `Laptop`, dan `AlatLab`), saya menerapkan *overriding* pada method `hitungDenda(int)` di mana masing-masing subclass menerapkan multiplier tarif yang berbeda (Ruang 1x, Laptop 2x, AlatLab 4x) yang dieksekusi melalui *dynamic binding*. Selain itu, saya mengimplementasikan *overloading* pada class `KatalogAset` melalui dua variasi method `cari`: `cari(String kata)` untuk mencari aset berdasarkan nama secara *case-insensitive*, dan `cari(String kata, JenisAset jenis)` untuk menyaring hasil pencarian berdasarkan kata kunci sekaligus jenis asetnya.

## Keputusan desain

Pada minggu ini seluruh aturan bisnis sudah ditentukan secara pasti dalam spesifikasi sehingga tidak ada asumsi baru yang perlu dicatat di `docs/asumsi.md`. Untuk menjaga prinsip DRY (*Don't Repeat Yourself*), logika validasi hari negatif serta rumus dasar perhitungan denda dipusatkan di method helper `hitungDendaDenganMultiplier` pada class induk `Aset`, sehingga tiap subclass hanya perlu meneruskan nilai multiplier yang sesuai. Pada `KatalogAset`, method `cari(kata, jenis)` memanfaatkan kembali pemanggilan `cari(kata)` sebelum memfilter berdasarkan jenis aset, sehingga logika pemrosesan string nama tetap berada di satu tempat.

## Kendala

Tantangan yang dihadapi adalah memastikan pencarian string pada `KatalogAset` tidak sensitif terhadap huruf besar/kecil (*case-insensitive*) dan menangani kemungkinan parameter bernilai null atau kosong dengan melempar `IllegalArgumentException`. Hal ini diselesaikan dengan memvalidasi parameter di awal method serta memanfaatkan Java Stream API dengan `toLowerCase()` untuk pencocokan nama yang aman dan konsisten.

## Penggunaan AI

- Alat yang dipakai: Antigravity (AI coding assistant)
- Bagian yang dibantu AI: Penyusunan berkas refleksi `catatan/minggu-04.md`, evaluasi implementasi overriding `hitungDenda` pada subclass `Aset`, dan validasi overloading method `cari(...)` di `KatalogAset`
- Prompt utama:
  1. "kerjakan @[docs/minggu-04.md] yang catatan saja"
  2. "core/src/main/java/id/ac/upb/asetpinjam/katalog/KatalogAset.java: dua overload cari(...)"
- Yang saya ubah atau tolak dari saran AI, dan alasannya: Memastikan method overload `cari(kata, jenis)` mendelegasikan penyaringan awal ke method `cari(kata)` agar tidak terjadi duplikasi logika lowercase dan validasi kata kunci.
- Satu hal yang saya pahami lebih baik sekarang: Perbedaan fundamental antara *method overriding* yang ditentukan pada saat runtime melalui *dynamic method dispatch* (runtime polymorphism) dan *method overloading* yang ditentukan pada saat kompilasi berdasarkan signature argumen (compile-time polymorphism).

