import { useState } from 'react'
import './App.css'

function App() {
  const [responseSent, setResponseSent] = useState(false);

  const handleSendResponse = () => {
    setResponseSent(true);
    // In a real app, this sends data back to the server.
    // For this simulation, the Android GP app will show the mocked response based on timer.
    alert("Respons berhasil disimpan dan diteruskan ke PKM Wamena Kota!\nFoto akan segera terjadwal dihapus (24j).");
  }

  return (
    <div className="dashboard-container">
      <div className="sidebar">
        <div className="logo-area">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M22 12h-4l-3 9L9 3l-3 9H2"></path></svg>
          SCIRA Web
        </div>
        <div className="nav-item">📊 Dashboard</div>
        <div className="nav-item active">📥 Kasus Rujukan Baru</div>
        <div className="nav-item">📚 Riwayat Kasus</div>
        <div className="nav-item">⚙️ Pengaturan</div>
      </div>

      <div className="main-content">
        <div className="header">
          <h1>Tinjauan Rujukan (Second Opinion)</h1>
          <div className="profile-area">
            <span style={{fontWeight: 600}}>Dr. Syifa Astuti, Sp.DVE</span>
            <div className="profile-pic">SA</div>
          </div>
        </div>

        <div className="case-grid">
          {/* LEFT COLUMN: THE CASE DATA */}
          <div className="card">
            <div className="card-header">
              <div>
                <h2 style={{margin:0, fontSize:'20px'}}>CASE #CS-2026-0047</h2>
                <div style={{color:'var(--text-muted)', fontSize:'13px', marginTop:'4px'}}>
                  Diterima: 14 Apr 2026, 10:25 WIB • <b>PKM Wamena Kota, Papua</b>
                </div>
              </div>
              <span className="tag yellow">URGENCY: YELLOW</span>
            </div>

            <div className="meta-info">
              <div className="meta-item">
                <label>ID Pasien (Anonymized)</label>
                <p>PAT-7X9K2M</p>
              </div>
              <div className="meta-item">
                <label>Demografi</label>
                <p>34 tahun, Perempuan</p>
              </div>
            </div>

            <div className="section-title">Foto Klinis Terenkripsi</div>
            <div className="photo-gallery">
              <div className="photo-thumbnail">Zoom Image 1</div>
              <div className="photo-thumbnail">Zoom Image 2</div>
              <div className="photo-thumbnail">Zoom Image 3</div>
            </div>

            <div className="section-title">Hasil AI SCIRA</div>
            <div className="ai-result">
              <p style={{margin:'0 0 12px 0', fontSize:'14px'}}><b>Visual Group:</b> Scaly/Papular</p>
              
              <div className="bar-container">
                <div className="bar-label">#1 Tinea Corporis</div>
                <div className="bar-track">
                  <div className="bar-fill" style={{width: '71%', backgroundColor: '#34a853'}}></div>
                </div>
                <div className="bar-text">71%</div>
              </div>
              
              <div className="bar-container">
                <div className="bar-label">#2 Contact Dermatitis</div>
                <div className="bar-track">
                  <div className="bar-fill" style={{width: '24%', backgroundColor: '#fbbc04'}}></div>
                </div>
                <div className="bar-text">24%</div>
              </div>
            </div>

            <div className="section-title">Jawaban Kuesioner</div>
            <ul style={{fontSize:'14px', lineHeight:'1.8', color:'var(--text-dark)'}}>
              <li><b>Keluhan utama:</b> Ruam merah gatal di lengan, sudah 2 minggu</li>
              <li><b>Gatal terasa malam?:</b> Ya</li>
              <li><b>Keluarga dengan keluhan sama?:</b> Tidak</li>
              <li><b>Riwayat alergi?:</b> Tidak</li>
              <li><b>Sudah diobati?:</b> Ya (bedak antijamur OTC, tidak membaik)</li>
              <li><b>Lokasi & Bentuk:</b> Lengan kanan, batas tegas, melingkar tepi merah</li>
            </ul>

            <div className="section-title" style={{marginTop:'24px'}}>Catatan GP Pengirim</div>
            <div className="notes-box">
              "Pasien sudah coba Kalpanax selama 1 minggu penuh, tapi tidak ada perubahan signifikan. Mohon second opinion apakah perlu oral antifungi."
            </div>
          </div>

          {/* RIGHT COLUMN: THE DOCTOR RESPONSE FORM */}
          <div className="card">
            <h2 style={{margin:'0 0 24px 0', fontSize:'18px'}}>Form Respons Spesialis</h2>
            
            {responseSent ? (
              <div style={{textAlign:'center', padding:'40px 20px'}}>
                <div style={{fontSize:'48px', marginBottom:'16px'}}>✅</div>
                <h3>Respons Terkirim</h3>
                <p style={{color:'var(--text-muted)'}}>GP akan menerima notifikasi balasan Anda. Resolusi kasus terbuka untuk Feedback Loop AI.</p>
              </div>
            ) : (
              <>
                <div className="form-group">
                  <label>Diagnosis Dikonfirmasi</label>
                  <select className="form-control" defaultValue="Setuju dengan SCIRA (Tinea Corporis)">
                    <option>Setuju dengan SCIRA (Tinea Corporis)</option>
                    <option>Koreksi: Contact Dermatitis</option>
                    <option>Koreksi Lainnya (Manual Input)</option>
                  </select>
                </div>

                <div className="form-group">
                  <label>Rekomendasi Treatment</label>
                  <textarea className="form-control" defaultValue="1. Hentikan Kalpanax
2. Ganti ke Terbinafine 1% krim, 2x sehari, minimal 2 minggu
3. Jaga area tetap kering
4. Hindari pakaian ketat dan sintetis"></textarea>
                </div>

                <div className="form-group">
                  <label>Perlu Pemeriksaan Tambahan?</label>
                  <div className="checkbox-group">
                    <label className="checkbox-item"><input type="checkbox" defaultChecked /> KOH preparation</label>
                    <label className="checkbox-item"><input type="checkbox" /> Patch test</label>
                    <label className="checkbox-item"><input type="checkbox" /> Biopsi kulit</label>
                    <label className="checkbox-item"><input type="checkbox" /> Kultur jamur</label>
                  </div>
                </div>

                <div className="form-group">
                  <label>Rekomendasi Follow-up</label>
                  <select className="form-control" defaultValue="2 minggu">
                    <option>1 minggu</option>
                    <option>2 minggu</option>
                    <option>1 bulan</option>
                    <option>Segera rujuk ke RS</option>
                  </select>
                </div>

                <div className="form-group">
                  <label>Catatan Opsional (Alasan Opini)</label>
                  <textarea className="form-control" placeholder="Tuliskan rasionalisasi medis Anda untuk GP..."></textarea>
                </div>

                <button className="btn btn-primary" onClick={handleSendResponse}>
                  Kirim Respons ke GP
                </button>
              </>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}

export default App
