# Sequence Diagram — Pengajuan Peminjaman Aset

Diagram ini memodelkan alur pengajuan peminjaman aset pada sistem **Aset-Pinjam** menggunakan fragmen `alt` untuk memisahkan skenario **bentrok** dan skenario **sukses**.

## Sumber Diagram

Berkas sumber Mermaid mentah: [`sequence-peminjaman.mmd`](./sequence-peminjaman.mmd).

```mermaid
sequenceDiagram
    autonumber
    actor Pengguna as Peminjam
    participant Katalog as katalog : KatalogAset
    participant Baru as baru : Peminjaman
    participant Jadwal as jadwal : JadwalPeminjaman
    participant Aset as aset : Aset

    Pengguna->>Katalog: cari(kata)
    activate Katalog
    Katalog-->>Pengguna: List<Aset>
    deactivate Katalog

    Pengguna->>Baru: new Peminjaman(id, peminjam, aset, mulai, selesai)
    activate Baru
    Note right of Baru: Validasi data wajib & selesai.isAfter(mulai)<br/>Status awal = StatusPeminjaman.DIAJUKAN
    Baru-->>Pengguna: baru
    deactivate Baru

    Pengguna->>Jadwal: ajukan(baru)
    activate Jadwal

    Jadwal->>Baru: getAset()
    activate Baru
    Baru-->>Jadwal: aset
    deactivate Baru

    Jadwal->>Jadwal: bentrok(baru)
    activate Jadwal
    Note right of Jadwal: Cek apakah ada peminjaman aktif pada<br/>daftar dengan kode aset sama & waktu tumpang tindih
    deactivate Jadwal

    alt bentrok == true (Skenario Bentrok)
        Jadwal--xPengguna: throw PeminjamanBentrokException
        Note over Pengguna,Jadwal: Pengajuan ditolak, objek baru tidak ditambahkan ke daftar
    else tidak bentrok (Skenario Sukses)
        Jadwal->>Aset: getJenis()
        activate Aset
        Aset-->>Jadwal: jenis
        deactivate Aset

        opt jenis == JenisAset.LAPTOP dan laptopAktif >= kuotaLaptop()
            Jadwal--xPengguna: throw KuotaHabisException
        end

        Jadwal->>Jadwal: tambah(baru)
        activate Jadwal
        Note right of Jadwal: daftar.add(baru)
        deactivate Jadwal
        Jadwal-->>Pengguna: pengajuan berhasil disimpan
    end

    deactivate Jadwal
```

## Penjelasan Skenario

1. **Pencarian & Inisialisasi (`1–4`)**:
   - Peminjam mencari aset yang tersedia melalui `KatalogAset.cari(kata)`.
   - Peminjam membuat objek `Peminjaman` dengan status awal `StatusPeminjaman.DIAJUKAN`. Konstruktor memvalidasi kelengkapan data serta memastikan `selesai.isAfter(mulai)`.
2. **Pemeriksaan Jadwal (`5–8`)**:
   - Peminjam memanggil `JadwalPeminjaman.ajukan(baru)`.
   - `JadwalPeminjaman` mengambil informasi aset dan memeriksa potensi konflik jadwal melalui `bentrok(baru)`.
3. **Skenario Bentrok (`alt: bentrok == true`)**:
   - Apabila waktu peminjaman tumpang tindih untuk aset dengan kode yang sama, `JadwalPeminjaman` langsung melempar `PeminjamanBentrokException` dan objek `baru` tidak ditambahkan ke `daftar`.
4. **Skenario Sukses (`else: tidak bentrok`)**:
   - Sistem mengecek jenis aset melalui `aset.getJenis()`. Khusus jenis `JenisAset.LAPTOP`, apabila kuota aktif peminjam sudah mencapai batas `kuotaLaptop()`, sistem melempar `KuotaHabisException`.
   - Jika seluruh validasi terpenuhi, `JadwalPeminjaman` memanggil `tambah(baru)` untuk memasukkan peminjaman ke dalam `daftar` dengan status `DIAJUKAN`.
