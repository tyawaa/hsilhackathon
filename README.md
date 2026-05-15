# HSIL Hackathon: AI Medical Diagnostic Tool

## Description / Overview
Aplikasi ini adalah solusi mobile Android offline-first untuk mendukung tenaga kesehatan di garis depan (*Nakes*) dalam mendiagnosis penyakit tropis dan non-tropis. Sistem menggunakan model Artificial Intelligence dan Machine Learning yang berjalan sepenuhnya di perangkat untuk menyediakan rekomendasi klinis cepat tanpa ketergantungan koneksi internet. Aplikasi ini dirancang dengan fokus pada keamanan data, privasi pasien, dan kepatuhan terhadap standar ISO 27001 serta UU PDP Indonesia.

## Software Requirements Specification (SRS)

### 1. Tujuan Sistem
Memberikan aplikasi diagnostik medis cerdas yang dapat:
* Melakukan pengambilan gambar kulit pasien menggunakan kamera perangkat.
* Menghasilkan analisis visual dengan model CNN untuk mendukung diagnosis awal.
* Menggabungkan data kuesioner klinis dengan hasil AI untuk rekomendasi diagnosis akhir.
* Beroperasi secara offline di daerah dengan konektivitas terbatas.
* Menyimpan data secara aman dan mematuhi perlindungan data pasien.

### 2. Ruang Lingkup Sistem
Aplikasi ini memberikan fitur untuk:
* Autentikasi dan otorisasi tenaga kesehatan.
* Alur persetujuan pasien sebelum pengambilan data.
* Deteksi dan analisis citra kulit menggunakan CameraX dan model TFLite.
* Kuesioner medis adaptif berdasarkan temuan awal AI.
* Model Random Forest ONNX untuk menyatukan prediksi visual dan klinis.
* Penyimpanan lokal terenkripsi untuk data pasien, riwayat konsultasi, dan hasil diagnosa.

### 3. Definisi dan Akronim
* Nakes: Tenaga Kesehatan
* CNN: Convolutional Neural Network
* TFLite: TensorFlow Lite
* ONNX: Open Neural Network Exchange
* UU PDP: Undang-Undang Perlindungan Data Pribadi
* Room: Android Jetpack Room Database
* SQLCipher: Enkripsi basis data SQLite

### 4. Para Pemangku Kepentingan
* Tenaga kesehatan (Nakes) sebagai pengguna utama.
* Pasien yang menjadi subjek pemeriksaan.
* Tim pengembangan aplikasi.
* Regulator keamanan dan privasi data kesehatan.

### 5. Kebutuhan Fungsional
#### 5.1 Autentikasi Pengguna
* Sistem harus memungkinkan tenaga kesehatan untuk masuk dengan akun yang valid.
* Sistem harus menolak akses jika kredensial tidak valid.

#### 5.2 Persetujuan Pasien
* Sistem harus menampilkan layar persetujuan yang wajib disetujui sebelum memulai pemeriksaan.
* Sistem harus mencatat waktu dan status persetujuan pasien.

#### 5.3 Pengambilan & Analisis Gambar
* Sistem harus memanfaatkan CameraX untuk menangkap gambar kulit pasien.
* Sistem harus memproses gambar secara lokal menggunakan model CNN TFLite.
* Sistem harus memberi peringatan jika tingkat keyakinan (confidence) di bawah 50% dan meminta pengambilan ulang.

#### 5.4 Kuesioner Klinis Dinamis
* Sistem harus menampilkan pertanyaan medis adaptif berdasarkan hasil awal analisis AI.
* Sistem harus mendukung alur percabangan pertanyaan untuk gejala dan kondisi yang berbeda.

#### 5.5 Rekomendasi Diagnosis
* Sistem harus menggabungkan hasil CNN dengan jawaban kuesioner ke dalam model Random Forest ONNX.
* Sistem harus menampilkan hasil diagnosis akhir dalam bentuk rekomendasi terstruktur.

#### 5.6 Manajemen Riwayat dan Data Lokal
* Sistem harus menyimpan hasil diagnosa dan metadata pemeriksaan secara lokal.
* Sistem harus memungkinkan tenaga kesehatan melihat riwayat konsultasi sebelumnya.

### 6. Kebutuhan Non-Fungsional
#### 6.1 Kinerja
* Aplikasi harus memberikan hasil analisis gambar dalam waktu kurang dari 10 detik pada perangkat kelas menengah.
* Aplikasi harus tetap responsif selama pemrosesan AI offline.

#### 6.2 Keamanan
* Data pasien dan hasil diagnosa harus disimpan dalam basis data terenkripsi dengan SQLCipher.
* Kunci kriptografi harus dikelola melalui Android Keystore.
* Aplikasi harus mematuhi persyaratan UU PDP dan ISO 27001 untuk perlindungan data.

#### 6.3 Privasi
* Data pasien tidak boleh dikirim ke server eksternal tanpa izin eksplisit.
* Aplikasi harus melindungi informasi sensitif dengan mekanisme otentikasi yang kuat.

#### 6.4 Ketersediaan
* Aplikasi harus dapat berjalan penuh secara offline tanpa koneksi internet.
* Sinkronisasi atau fitur cloud hanya boleh dilakukan jika koneksi tersedia dan pasien menyetujui.

#### 6.5 Portabilitas
* Aplikasi harus berjalan pada perangkat Android yang mendukung CameraX dan API level yang direkomendasikan.

### 7. Antarmuka Pengguna
* Layar login untuk tenaga kesehatan.
* Layar persetujuan pasien dengan teks dan tombol persetujuan.
* Layar kamera in-app untuk menangkap gambar kulit.
* Layar penilaian kuesioner dinamis.
* Layar hasil diagnosis akhir yang menampilkan rekomendasi dan ringkasan.
* Layar riwayat konsultasi sebelumnya.

### 8. Antarmuka Sistem Eksternal
* Model TFLite dan ONNX harus dimuat dari direktori aplikasi internal.
* Aplikasi harus menggunakan Android Keystore untuk enkripsi lokal.
* Fitur kamera harus terhubung dengan API CameraX.

### 9. Batasan dan Asumsi
* Sistem diasumsikan dijalankan pada perangkat Android yang memiliki kamera dan penyimpanan lokal yang cukup.
* Sistem diasumsikan digunakan oleh tenaga kesehatan yang sudah terlatih.
* Aplikasi tidak menggantikan diagnosis medis profesional; hasilnya bersifat rekomendasi pendukung keputusan.


## Installation
To set up and run this project locally, follow these steps:
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/hsilhackathon.git
   ```
2. Open the project in **Android Studio**.
3. Allow Gradle to sync and download all necessary dependencies.
4. Build and Run the application on a physical Android device or an emulator equipped with camera capabilities.

## Usage
1. **Health Worker Login**: Secure authentication for authorized *Nakes*.
2. **Informed Consent**: A mandatory patient consent workflow must be completed prior to any data collection.
3. **AI Scan (CameraX)**: Utilize the in-app camera to capture images of skin conditions. The app performs real-time AI image analysis. *(Note: Scans with a confidence score below 50% will prompt the user for a retake).*
4. **Dynamic Questionnaire**: Complete an adaptive clinical questionnaire that branches based on the AI's initial visual prediction (e.g., differentiating between *Bercak Merah* and *Bintil Merah*).
5. **Final Assessment**: The app seamlessly aggregates the visual CNN output with the questionnaire data into a Random Forest model to generate a structured diagnostic recommendation.

## Features
*   **Offline-First Native Android Architecture**: Fully functional in remote environments without an active internet connection.
*   **Edge AI Diagnostics**:
    *   **CNN (TensorFlow Lite)**: For powerful, real-time visual image analysis.
    *   **Random Forest (ONNX)**: For logical clinical decision support and synthesizing final diagnoses.
*   **Enterprise-Grade Security & Privacy**:
    *   Local database encryption using **Room + SQLCipher**.
    *   Cryptographic key management via **Android Keystore**.
    *   ISO 27001 & UU PDP compliant workflows, featuring comprehensive audit logging and secure data access.
*   **Dynamic Workflows**: Smart, context-aware clinical screening questionnaires.
*   **Telemedicine Referral Ready**: Architecture supports secure syncing and referrals to specialists when connectivity is restored.

## Tech Stack / Built With
*   **Language**: Kotlin
*   **Platform**: Android SDK
*   **AI/ML**: TensorFlow Lite (TFLite), ONNX Runtime, CameraX
*   **Local Storage**: Room Database, SQLCipher
*   **Security Tools**: Android Keystore, SSL Pinning

## Contributors
| Name | Role | GitHub |
|------|------|--------|
| **RavelGS** | AI Implementation & Model Integration | [![GitHub](https://img.shields.io/badge/-RavelGS-181717?logo=github&logoColor=white)](https://github.com/RavelGS) |
| **tyawaa** | Frontend & Backend Data Integration | [![GitHub](https://img.shields.io/badge/-tyawaa-181717?logo=github&logoColor=white)](https://github.com/tyawaa) |
| **RizzCode10** | UI / UX Design & Workflows | [![GitHub](https://img.shields.io/badge/-RizzCode10-181717?logo=github&logoColor=white)](https://github.com/RizzCode10) |
