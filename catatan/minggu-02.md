# Catatan Minggu 02: Struktur OOP: Class, Object, Encapsulation

## Yang saya kerjakan

Saya mengimplementasikan encapsulation pada class `Aset` dengan menjadikan seluruh field (`kode`, `nama`, `status`, dan `kondisi`) bersifat `private`. Saya membuat konstruktor yang menginisialisasi kode, nama, status awal `TERSEDIA`, serta kondisi awal `BAIK` disertai validasi agar kode dan nama tidak null atau kosong. Selain itu, saya menambahkan getter untuk seluruh field serta setter untuk nama, status, dan kondisi dengan validasi nilai null/kosong, tanpa menyediakan setter untuk kode agar sifat keunikannya tidak dapat diubah sembarangan.

## Keputusan desain

Untuk penanganan string pada kode dan nama, saya memutuskan untuk memeriksa kekosongan string termasuk jika hanya berisi spasi (`trim().isEmpty()`) dan melemparkan `IllegalArgumentException`. Namun, nilai kode dan nama tidak dinormalisasi (tidak dipaksa uppercase atau di-trim) agar menjaga integritas format input asli pengguna. Keputusan ini dicatat di `docs/asumsi.md` bagian A-02. Field `kode` juga sengaja tidak dibuatkan setter demi melindungi identitas unik aset setelah objek diinstansiasi.

## Kendala

Tantangan yang dihadapi adalah memastikan semua kondisi nilai null dan string kosong (termasuk spasi kosong) tertangkap oleh validasi baik di konstruktor maupun di setter `setNama`. Saya mengatasinya dengan menggunakan pengecekan `nama == null || nama.trim().isEmpty()`, serta memastikan `setStatus` dan `setKondisi` menolak nilai null dengan pesan kesalahan yang jelas.

## Penggunaan AI

- Alat yang dipakai: Antigravity (AI coding assistant)
- Bagian yang dibantu AI: Validasi konstruktor `Aset`, setter (`setNama`, `setStatus`, `setKondisi`), dan dokumentasi asumsi
- Prompt utama:
  1. "core/src/main/java/id/ac/upb/asetpinjam/model/Aset.java: field, konstruktor, getter, setter dengan validasi."
  2. "Kode dan nama tidak boleh null atau kosong (IllegalArgumentException). Nilai awal: status TERSEDIA, kondisi BAIK. Semua field private. Kode tidak boleh punya setter. setStatus dan setKondisi menolak null."
- Yang saya ubah atau tolak dari saran AI, dan alasannya: Memastikan validasi string kosong pada `setNama` konsisten memakai `.trim().isEmpty()` dan tidak hanya `.isEmpty()`, agar masukan string yang hanya berisi spasi juga ditolak sebagaimana di konstruktor.
- Satu hal yang saya pahami lebih baik sekarang: Encapsulation bukan sekadar membuat field `private` dan menambahkan getter/setter, melainkan melindungi integritas data internal objek dengan mencegah mutasi tak terkontrol dan menegakkan invarian validasi sejak objek dibuat.

