# 🏡 API JAVA OOP | Aplikasi Pemesanan Vila

---

## 👥 Identitas Kami

| Nama                         | NIM         | Kelas |
|------------------------------|-------------|-------|
| I Putu Raditya Dharma Yoga   | 2405551026  | PBO D |
| Gerald Hizkia Turnip         | 2405551081  | PBO D |
| Elika Putri Wicaksana        | 2405551093  | PBO D |
| Mahaprama Danesh Hiswara     | 2405551094  | PBO D |

---

## 📌 Introducing

Ini adalah **backend API** sederhana berbasis **Java (tanpa Spring Boot)** yang dibuat untuk memenuhi tugas mata kuliah **Pemrograman Berorientasi Objek (PBO)**. Aplikasi ini mensimulasikan proses pemesanan vila dengan fitur CRUD untuk entitas:

- Customer
- Villa
- Room Type
- Booking
- Review
- Voucher

Data disimpan dalam **SQLite**, dan API dapat diuji menggunakan aplikasi seperti **Postman** melalui alamat: http://localhost:8080

---
## 🔧 Panduan Menjalankan Program API Pemesanan Vila
Program ini dibangun menggunakan **Java 17**. Karena menggunakan fitur modern seperti *switch expressions* dengan panah (`case "GET" ->`), maka wajib dijalankan menggunakan **JDK versi 17** atau lebih tinggi.

### ⚙️ Cara Kompilasi Program
Jalankan perintah berikut untuk mengompilasi program dari direktori root:

```bash
javac -d out -cp "lib/*" src/api/*.java src/controllers/*.java src/models/*.java src/exceptions/*.java src/util/*.java src/database/*.java
```
**Penjelasan:**  
- `d out`: menyimpan file hasil kompilasi ke folder out
- `cp "lib/*"`: menyertakan semua file .jar di dalam folder lib ke classpath
- `src/...`: menunjuk ke semua file Java di masing-masing package


>💡 Pastikan terminal saat menjalankan perintah berada di direktori project `\PBO-Tugas2`


### ▶️ Cara Menjalankan Program
Setelah berhasil dikompilasi, jalankan program sesuai dengan sistem operasi:

- **Windows**
```bash
java -cp "out;lib/*" api.API
```
- **macOs atau Linux**
```bash
java -cp "out:lib/*" api.API
```

### 📌 Header API Key
Semua request ke API wajib menyertakan header berikut:

| Key       | Value   |
|-----------|---------|
| x-api-key | UNKNOWN |

Tanpa header ini, server akan membalas dengan status 401 Unauthorized Dan pesan "Invalid API Key"

>📫 Jika menggunakan Postman, tambahkan header ini di tab Headers saat mengirim request.


---
## Dokumentasi API
## 👤 Customer
Customer merepresentasikan pengguna yang melakukan pemesanan vila. Customer dapat memiliki beberapa pemesanan (booking) dan memberikan review. Setiap vila berisi informasi mengenai:

| Field | Tipe Data dalam Database | Tipe Data dalam Program | Keterangan                                 |
|-------|--------------------------|-------------------------|--------------------------------------------|
| id    | `INT`                    | `Integer`               | Identitas unik dari masing-masing customer |
| name  | `TEXT`                   | `String`                | Nama lengkap customer                      |
| email | `TEXT`                   | `String`                | Alamat email customer                      |
| phone | `TEXT`                   | `String`                | Nomor telepon customer                     |

Berikut adalah daftar endpoint yang tersedia untuk entitas `Customer`

___

### 🔍 GET `/customers`

Menampilkan seluruh customer yang terdaftar.

### ✅ 1. Berhasil Menampilkan Seluruh Customer

Berhasil menampilkan seluruh customer yang terdaftar.

**Contoh Response:**
![Customer All - Berhasil.png](images/Customer%20All%20-%20Berhasil.png)

### ❌ 2. Gagal - Tidak Ada Customer Pada Database

Kondisi ini terjadi jika tidak terdapat data customer sama sekali dalam database.

---

### 🔍 GET `/customers/{id}`

Menampilkan informasi detail customer berdasarkan id.

### ✅ 1. Berhasil Menampilkan Data Customer

Berhasil menampilkan informasi detail customer berdasarkan id.

Contoh Response:
![Customer ID - Berhasil.png](images/Customer%20ID%20-%20Berhasil.png)

### ❌ 2. Gagal - ID Customer Tidak Ditemukan

Kondisi ini terjadi jika tidak terdapat Customer dengan id yang sesuai.


---
### 📝 POST `/customers`

Menambahkan customer baru. Email dan nomor telepon akan divalidasi untuk format yang benar.

Gunakan format berikut untuk menambahkan customer:
```json 
{
  "name": "Maru",
  "email": "maru@example.com",
  "phone": "+6281234567890"
}
```

### ✅ 1. Berhasil Menambahkan Customer

Berhasil menambahkan data customer baru.

**Contoh Response:**


### ❌ 2. Gagal - Gagal Menambahkan Customer

Kondisi ini terjadi jika ada field yang kosong atau format email atau nomor telepon tidak sesuai.

**Contoh Response:**


---

### ✏️ PUT `/customers/{id}`

Memperbarui data customer yang sudah ada berdasarkan id.

### ✅ 1. Berhasil Memperbarui Data Customer

Berhasil memperbarui data customer berdasarkan id.

**Contoh Response:**


### ❌ 2. Gagal - ID Customer Tidak Ditemukan

Kondisi ini terjadi jika tidak terdapat data customer dengan id yang sesuai.

**Contoh Response:**

---

## 🏡 Villa
Villa dalam aplikasi pemesanan vila mempresentasikan properti atau penginapan yang tersedia untuk disewa. Vila menyediakan endpoint untuk melihat, menambahkan, mengubah, dan menghapus vila yang tersedia. Setiap vila berisi informasi mengenai:


| Field       | Tipe Data dalam Database | Tipe Data dalam Program | Keterangan                                                         |
|-------------|--------------------------|-------------------------|--------------------------------------------------------------------|
| id          | `INT`                    | `Integer`               | Identitas unik dari masing-masing vila.                            |
| name        | `TEXT`                   | `String`                | Nama dari vila.                                                    |
| description | `TEXT`                   | `String`                | Alamat atau wilayah tempat vila berada.                            |
| address     | `TEXT`                   | `String`                | Penjelasan ringkas tentang fasilitas, nuansa atau keunggulan vila. |

Berikut adalah daftar endpoint yant tersedia untuk entitas `Villa`

---
### 🔍 GET `/villas`
Endpoint ini digunakan untuk menampilkan daftar seluruh vila yang tersedia dalam database.

### ✅ 1. Berhasil Menampilkan Seluruh Vila
Jika data vila tersedia di dalam database, maka sistem akan mengembalikan daftar lengkap vila dalam format JSON.

**Contoh Response:**

![Berhasil Menampilkan Seluruh Vila](images/Villa%20-%20Berhasil%20Menampilkan%20Seluruh%20Vila.bmp)

### ❌ 2. Gagal - Tidak Ada Vila pada Database
Kondisi ini terjadi jika tidak terdapat data vila sama sekali dalam database.

> Meskipun jarang terjadi, skenario ini bisa muncul apabila seluruh data vila terhapus karena gangguan sistem atau kesalahan pengelolaan database.

**Contoh Response:**

![Tidak Ada Vila yang Tersedia](images/Villa%20-%20Tidak%20Ada%20Vila%20yang%20Tersedia%20dalam%20Database.png)

---
### 🔍 GET `/villas/{id}`
Endpoint ini digunakan untuk menampilkan **informasi detail** dari sebuah vila tertentu berdasarkan `id`.

### ✅ 1. Villa Berhasil Ditemukan
Jika `id` vila ditemukan dalam database, maka informasi lengkap mengenai vila tersebut akan ditampilkan.

**Contoh Response:**

![Villa Berhasil Ditemukan](images/Villa%20-%20Villa%20Berhasil%20Ditemukan.png)

### ❌ 2. Gagal - Villa Tidak Ditemukan
Jika `id` yang dimasukkan pada path tidak sesuai dengan data yang tersedia, maka sistem akan memberikan respons error.
> Hal ini dapat terjadi apabila pengguna memasukkan `id` yang tidak valid atau vila dengan `id` tersebut sudah dihapus.

**Contoh Response:**

![Villa Tidak Ditemukan](images/Villa%20-%20Villa%20Tidak%20Ditemukan.png)

---
### 🔍 GET `/villas?ci_date={checkin_date}&co_date={checkout_date}`
Endpoint ini digunakan untuk mencari vila yang tersedia berdasarkan rentang tanggal `check-in` dan `check-out`.

### ✅ 1. Vila yang Tersedia Berhasil Ditemukan
Jika terdapat vila yang tersedia pada rentang tanggal yang diberikan, maka daftar vila akan ditampilkan.

**Contoh Response:**

![Villa yang Tersedia Berhasil Ditemukan](images/Villa%20-%20Villa%20yang%20Tersedia%20Berhasil%20Ditemukan.png)

### ❌ 2. Gagal - Tidak Ada Vila yang Tersedia
Jika tidak ada vila yang tersedia dalam rentang tanggal tersebut, maka sistem akan mengembalikan pesan bahwa tidak ada hasil.

**Contoh Response:**

![Tidak Ada Vila yang Tersedia](images/Villa%20-%20Tidak%20ada%20Vila%20yang%20Tersedia.png)

### ❌ 3. Gagal - Format Tanggal Salah
Kesalahan ini muncul jika format tanggal yang diberikan tidak sesuai dengan format yang diharapkan (`YYYY-MM-DD`).

> Pastikan parameter `ci_date` dan `co_date` menggunakan format tanggal yang benar, seperti: `2025-07-05`.

**Contoh Response:**

![Format Tanggal Salah](images/Villa%20-%20Format%20Tanggal%20Salah.png)

### ❌ 4. Gagal - Tanggal Check Out Lebih Awal dari Tanggal Check In
Sistem akan menolak permintaan jika tanggal check-out lebih awal dari tanggal check-in, karena logika waktu tidak valid.

**Contoh Response:**

![Tanggal Check Out Lebih Awal dari Tanggal Check In](images/Villa%20-%20Tanggal%20Check%20Out%20Lebih%20Awal%20dari%20Tanggal%20Check%20In.png)

---
### 📝 POST `/villas`

Endpoint ini digunakan untuk **menambahkan data vila baru** ke dalam database. Permintaan dikirim dalam format JSON.

#### 📥 Format JSON yang Diperlukan:
```json
{
  "name": "Nama Vila",
  "description": "Deskripsi singkat tentang vila",
  "address": "Alamat lengkap vila"
}
```

> **Note:**  
> Tidak perlu menyertakan `id` dalam JSON karena field `id` dibuat otomatis oleh database menggunakan auto increment. Jika `id` disertakan, sistem akan memunculkan validasi dan menolak permintaan tersebut.

### ✅ 1. Vila Berhasil Ditambahkan

Permintaan berhasil diproses dan data vila baru telah ditambahkan ke dalam database. Sistem akan mengembalikan respons berisi data vila yang baru dibuat, termasuk `id` yang dihasilkan otomatis.

**Contoh Request dan Response:**

![Vila Berhasil Ditambahkan](images/Villa%20-%20Vila%20Berhasil%20Ditambahkan.png)

---

### ❌ 2. Gagal - Format JSON Tidak Valid

Gagal menambahkan vila karena format JSON yang dikirimkan **tidak sesuai** dengan struktur yang diharapkan (misalnya, ada koma yang salah, kurung kurawal tidak lengkap, atau menggunakan format non-JSON).

> Pastikan struktur JSON valid dan menggunakan tanda kutip ganda (`"`) untuk key dan value.

**Contoh JSON Tidak Valid:**
```json
{
  name: "Villa Nusa Indah",
  "description": "Villa tenang dengan pemandangan taman bunga sakura",
  "address": "Jalan Sakura No. 88, Ubud, Bali"
}
```

**Contoh Response:**

![Format JSON Tidak Valid](images/Villa%20-%20Format%20JSON%20Tidak%20Valid.png)

### ❌ 3. Gagal - Struktur Data Tidak Valid
Permintaan gagal karena struktur data JSON tidak sesuai dengan yang diharapkan. Beberapa penyebab umum meliputi:

- `name`, `description`, atau `address` bukan bertipe string (misalnya `integer`, `boolean`, atau `null`).
- Key JSON tidak sesuai dengan yang diharapkan (`name`, `description`, `address`).
- Field kosong atau hanya berisi spasi. 
- Menyertakan atribut tambahan yang tidak digunakan.

>Pastikan struktur JSON Anda sesuai dengan format dan tipe data yang telah ditentukan sistem.

**Contoh Response:**

![Struktur Data Tidak Valid](images/Villa%20-%20Struktur%20Data%20Tidak%20Valid.png)

### ❌ 4. Gagal - Validasi dari `POST /villas`

Permintaan akan ditolak jika terdapat pelanggaran terhadap aturan validasi berikut:

#### 1. Menyertakan Field `id`
Sistem menolak jika permintaan JSON menyertakan `id`, karena `id` diatur otomatis oleh database.

**Contoh:**
```json
{
    "id": 1,
  "name": "Villa Nusa Indah",
  "description": "Villa tenang dengan pemandangan taman bunga sakura",
  "address": "Jalan Sakura No. 88, Ubud, Bali"
}
```
**Contoh Response:**

![Menyertakan Field ID](images/Villa%20-%20Menyertakan%20Field%20ID.png)

#### 2. Tidak Mengisi Nilai pada Field
Field `name`, `description`, dan `address` merupakan atribut wajib diisi. Jika salah satu field dikosongkan atau tidak diberikan nilai, maka sistem akan menolak permintaan dan menampilkan pesan kesalahan.

**Contoh JSON tidak valid:**
```json
{
  "name": "",
  "description": "Deskripsi",
  "address": "Alamat"
}
```
**Contoh Response:**

![Tidak Mengisi Nilai pada Field](images/Villa%20-%20Tidak%20Mengisi%20Nilai%20pada%20FIeld.png)

---
### 🔄 PUT `/villa/{id}`
Endpoint ini digunakan untuk **mengubah data vila** yang sudah ada di dalam database berdasarkan `id` vila. Data yang dikirimkan harus dalam format JSON sesuai struktur yang ditentukan.

#### 📥 Format JSON yang Diperlukan:
```json
{
  "name": "Nama Baru Vila",
  "description": "Deskripsi singkat baru tentang vila",
  "address": "Alamat lengkap baru vila"
}
```
> **Note:**  
> Tidak perlu menyertakan `id` dalam JSON karena `id` digunakan dari path parameter (`/villas/{id}`) dan diatur otomatis oleh database. Jika `id` disertakan dalam body JSON, maka sistem akan menolak permintaan karena tidak sesuai dengan aturan validasi.

### ✅ 1. Berhasil Mengubah Vila

Permintaan berhasil diproses dan data vila berhasil diperbarui. Respons akan menampilkan data lama dan data baru sebagai bukti perubahan yang telah dilakukan.

**Contoh Request dan Response:**

![Berhasil Mengubah Vila](images/Villa%20-%20Berhasil%20Mengubah%20Vila.png)

### ❌ 2. Gagal - Data Vila Tidak Ditemukan

Kesalahan ini terjadi apabila `id` vila yang diberikan tidak ditemukan di dalam database.

> Pastikan `id` yang digunakan sudah benar dan vila dengan `id` tersebut memang masih tersedia.

**Contoh Response:**

![Data Vila Tidak Ditemukan](images/Villa%20-%20Update%20-%20Data%20Vila%20Tidak%20Ditemukan.png)

### ❌ 3. Gagal - Format JSON Tidak Valid
Kesalahan terjadi karena **format JSON tidak valid secara sintaksis**, seperti:
- Tidak menggunakan tanda kutip ganda (`"`)
- Struktur JSON rusak (kurung kurawal tidak lengkap, koma berlebih, dll.)

**Contoh JSON Tidak Valid:**
```json
{
  name: "Villa Kenanga Baru",
  "description": Villa direnovasi dengan kolam renang dan rooftop baru,
  "address": "Jalan Laut Selatan No. 99"
}
```

**Contoh Response:**

![Format JSON Tidak Valid](images/Villa%20-%20Update%20-%20Format%20JSON%20Tidak%20Valid.png)

### ❌ 4. Gagal - Struktur Data Tidak Valid
Permintaan akan gagal jika struktur data JSON tidak sesuai dengan format yang diharapkan. Beberapa penyebab umum:
- Field `name`, `description`, atau `address` bukan tipe data `string`
- Key tidak sesuai dengan spesifikasi (`name`, `description`, `address`)

**Contoh Response:**

![Struktur Data Tidak Valid](images/Villa%20-%20Update%20-%20Struktur%20Data%20Tidak%20Valid.png)

### ❌ 5. Gagal - Validasi dari `PUT /villas`
Permintaan akan ditolak jika terdapat pelanggaran terhadap aturan validasi berikut:

#### 1. Menyertakan Field `id`
Sistem akan menolak jika permintaan JSON menyertakan field `id`, karena `id` digunakan dari path parameter dan tidak boleh dikirim di dalam body JSON.

**Contoh:**
```json
{
  "id": 1,
  "name": "Villa Kenanga Baru",
  "description": "Villa direnovasi dengan kolam renang dan rooftop baru",
  "address": "Jalan Laut Selatan No. 99"
}
```

**Contoh Response:**

![Menyertakan Field ID](images/Villa%20-%20Update%20-%20Menyertakan%20Field%20ID.png)

#### 2. Tidak Mengisi Nilai pada Field
Field `name`, `description`, dan `address` merupakan atribut wajib. Jika salah satu field dikosongkan atau hanya berisi string kosong, maka sistem akan memunculkan pesan kesalahan.

**Contoh JSON Tidak Valid:**

```json
{
  "name": "",
  "description": "",
  "address": ""
}
```

**Contoh Response:**

![Tidak Mengisi Nilai pada Field](images/Villa%20-%20Update%20-%20Tidak%20Mengisi%20Nilai%20pada%20Field.png)

---

### 🗑️ DELETE `/villas/{id}`
Endpoint ini digunakan untuk **menghapus data sebuah vila** berdasarkan `id` yang diberikan. Jika vila dengan `id` tersebut ditemukan di database, maka data akan dihapus secara permanen.

### ✅ 1. Vila Berhasil Dihapus
Permintaan berhasil diproses dan vila dengan `id` yang dimaksud berhasil dihapus dari database.

**Contoh Response:**

![Vila Berhasil Dihapus](images/Villa%20-%20Vila%20Berhasil%20Dihapus.png)

### ❌ 2. Gagal - Data Vila Tidak Ditemukan
Kesalahan ini terjadi apabila `id` vila yang diberikan tidak ditemukan di dalam database. Sistem akan menampilkan pesan error bahwa data tidak tersedia untuk dihapus.

**Contoh Response:**

![Villa - Delete - Data Vila Tidak Ditemukan](images/Villa%20-%20Delete%20-%20Data%20Vila%20Tidak%20Ditemukan.png)

---

## 📝 Review

Review adalah fitur yang memungkinkan pelanggan untuk memberikan penilaian dan umpan balik setelah melakukan pemesanan vila. Setiap review berisi informasi mengenai:

| **Field**   | **Tipe Data dalam Database** | **Tipe Data dalam Program** | **Keterangan**                                        |
|-------------|------------------------------|------------------------------|--------------------------------------------------------|
| booking     | INT                          | Integer                      | ID booking yang menjadi referensi review              |
| star        | INT                          | Integer                      | Penilaian bintang (nilai antara 1–5)                  |
| title       | TEXT                         | String                       | Ringkasan singkat kesan pelanggan                     |
| content     | TEXT                         | String                       | Ulasan lengkap mengenai pengalaman pelanggan          |

> ⚠️ **Fitur review hanya dapat digunakan jika entitas `Customer`, `Villa & Room Type`, dan `Booking` sudah terisi dengan benar.**

Berikut adalah daftar endpoint yang tersedia untuk entitas `Review`.

---

### 🔍 GET `/villas/{id}/reviews`

Mengambil semua review yang diberikan untuk **satu vila** berdasarkan ID vila.

### ✅ 1. Review Berhasil Ditemukan

Berhasil menampilkan semua review untuk villa dengan ID tertentu.

**Contoh Response:**

![Review Berhasil](images/img.png)



### ❌ 2. Gagal - Villa Tidak Ditemukan

Jika ID villa pada path tidak ada di database, maka sistem akan memberikan pesan error seperti berikut:

**Contoh Response:**

![Villa Tidak Ditemukan](images/img2.png)

---

### 🔍 GET `/customers/{id}/reviews`

Mengambil semua review yang pernah ditulis oleh satu customer berdasarkan ID customer.

### ✅ 1. Review Berhasil Ditemukan

Mengambil semua review yang pernah ditulis oleh satu customer berdasarkan ID customer.

**Contoh Response:**

![Review Berhasil](images/img_1.png)

### ❌ 2. Gagal – Customer Tidak Ditemukan atau Belum Menulis Review

Jika ID customer tidak ditemukan di database, atau belum pernah menulis review, maka sistem akan memberikan pesan error seperti berikut:

**Contoh Response:**

![Customer Tidak Ditemukan](images/img_2.png)

---

### 📝 POST `/customers/{customerId}/bookings/{bookingId}/reviews`
Menambahkan review baru dari seorang customer untuk booking tertentu. Endpoint ini hanya bisa digunakan jika customer dan booking tersebut sudah ada di database, dan booking memang milik customer tersebut.

Gunakan format berikut saat menambahkan review:

```json 
{
  "star": 5, 
  "title": "Judul Review",
  "content": "Isi dari Review"
}
```

### ✅ 1. Review Berhasil Ditambahkan

Berhasil menambahkan review untuk booking milik customer dengan ID tertentu.

**Contoh Request:**

![Review Berhasil Ditambahkan](images/img_3.png)

### ❌ 2. Gagal – Booking Tidak Ditemukan atau Bukan Milik Customer

Jika booking tidak ditemukan atau tidak dimiliki oleh customer dengan ID yang sesuai, sistem akan memberikan pesan kesalahan seperti:

**Contoh Request:**

![Gagal – Booking Tidak Ditemukan atau Bukan Milik Customer](images/img_4.png)

### ❌ 3. Gagal – Validasi Review
Jika nilai yang dikirim tidak valid (misal star < 1 atau > 5 atau title kosong), maka sistem akan menolak data.

**Contoh Response:**

![Gagal – Validasi Review](images/img_5.png)

---

## 🎟️ Voucher
Voucher digunakan untuk memberikan potongan harga kepada pelanggan dengan kode tertentu yang berlaku dalam rentang tanggal tertentu. Setiap voucher berisi informasi mengenai:

| Field       | Tipe Data dalam Database | Tipe Data dalam Program | Keterangan                               |
|-------------|--------------------------|-------------------------|------------------------------------------|
| id          | `INT`                    | `Integer`               | Identitas unik dari masing-masing voucher |
| code        | `TEXT`                   | `String`                | 	Kode unik voucher yang digunakan pelanggan untuk mendapatkan diskon.                                         |
| description | `TEXT`                   | `String`                | Deskripsi singkat mengenai kegunaan atau syarat dari voucher.                                         |
| discount    | `REAL`                   | `Double`                | Persentase potongan harga                                         |
| startDate   | `TEXT`                   | `String`                | Tanggal mulai berlakunya voucher                                         |
| endDate     | `TEXT`                   | `String`                | Tanggal akhir masa berlaku voucher                                         |

Berikut adalah daftar endpoint yang tersedia untuk entitas `Voucher`

---

### 🔍 GET `/vouchers`

Menampilkan seluruh voucher yang terdaftar.

### ✅ 1. Berhasil Menampilkan Seluruh Voucher

Berhasil menampilkan seluruh voucher yang terdaftar.

**Contoh Response:**


### ❌ 2. Gagal - Tidak Ada Voucher Pada Database

Kondisi ini terjadi jika tidak terdapat data voucher sama sekali dalam database.

**Contoh Response:**

---

### 🔍 GET `/vouchers/{id}`

Menampilkan informasi detail voucher berdasarkan id.

### ✅ 1. Berhasil Menampilkan Data Voucher

Berhasil menampilkan informasi detail voucher berdasarkan id.

**Contoh Response:**


### ❌ 2. Gagal - ID Voucher Tidak Ditemukan

Kondisi ini terjadi jika tidak terdapat voucher dengan id yang sesuai.

**Contoh Response:**


---

### 📝 POST `/vouchers`

Menambahkan voucher baru. Pastikan semua data diisi dengan benar, karena sistem akan memvalidasi setiap field.

Gunakan format berikut untuk menambahkan customer:
```json 
{
  "code": "DISKON25",
  "description": "Diskon 25% untuk semua pemesanan di bulan Juli",
  "discount": 25.0,
  "startDate": "2025-03-01",
  "endDate": "2025-03-31"
}

```

### ✅ 1. Berhasil Menambahkan Voucher

Berhasil menambahkan data voucher baru.

**Contoh Response:**


### ❌ 2. Gagal - Gagal Menambahkan Voucher

Kondisi ini terjadi jika ada field yang kosong atau format tidak sesuai.

**Contoh Response:**


---

### ✏️ PUT `/vouchers/{id}`

Memperbarui data voucher yang sudah ada berdasarkan id.

### ✅ 1. Berhasil Memperbarui Data Voucher

Berhasil memperbarui data voucher berdasarkan id.

**Contoh Response:**


### ❌ 2. Gagal - ID Voucher Tidak Ditemukan

Kondisi ini terjadi jika tidak terdapat data voucher dengan id yang sesuai.

**Contoh Response:**

---

### 🗑️ DELETE `/vouchers/{id}`

Menghapus data voucher dari database berdasarkan id.

### ✅ 1. Berhasil Menghapus Voucher

Berhasil menghapus data voucher berdasarkan id.

**Contoh Response:**


### ❌ 2. Gagal - ID Voucher Tidak Ditemukan

Kondisi ini terjadi jika tidak terdapat data voucher dengan id yang sesuai.

**Contoh Response:**

---
