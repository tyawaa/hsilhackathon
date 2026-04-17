package com.example.hsilhackathon

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScanAIActivity : AppCompatActivity() {

    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cnnModelExecutor: CNNModelExecutor
    private lateinit var previewView: PreviewView

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(this, "Izin kamera ditolak.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_ai)

        val btnCapture = findViewById<ImageButton>(R.id.btnCapture)
        val btnBack = findViewById<ImageView>(R.id.btnBackCam)
        val pbAnalisis = findViewById<ProgressBar>(R.id.pbAnalisis)
        previewView = findViewById(R.id.cameraPreview)
        
        cnnModelExecutor = CNNModelExecutor(this)
        cameraExecutor = Executors.newSingleThreadExecutor()

        val namaPasien = intent.getStringExtra("NAMA_PASIEN") ?: "Pasien"

        btnBack.setOnClickListener {
            finish()
        }

        btnCapture.setOnClickListener {
            val imageCapture = imageCapture ?: return@setOnClickListener

            btnCapture.visibility = View.INVISIBLE
            pbAnalisis.visibility = View.VISIBLE
            Toast.makeText(this, "AI Sedang Menganalisis Gambar...", Toast.LENGTH_SHORT).show()

            imageCapture.takePicture(
                ContextCompat.getMainExecutor(this),
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            val bitmap = imageProxyToBitmap(image)
                            image.close()

                            val result = cnnModelExecutor.predict(bitmap)

                            withContext(Dispatchers.Main) {
                                // Cek confidence
                                if (result.top1LabelScore < 0.50f) {
                                    Toast.makeText(this@ScanAIActivity, "Kepercayaan AI rendah (${(result.top1LabelScore*100).toInt()}%). Tolong foto ulang.", Toast.LENGTH_LONG).show()
                                    btnCapture.visibility = View.VISIBLE
                                    pbAnalisis.visibility = View.GONE
                                } else {
                                    val intentLanjut = Intent(this@ScanAIActivity, QuestionnaireLanjutanActivity::class.java)
                                    intentLanjut.putExtras(intent)
                                    intentLanjut.putExtra("CNN_TOP1_LABEL", result.top1Label)
                                    intentLanjut.putExtra("CNN_TOP2_LABEL", result.top2Label)
                                    intentLanjut.putExtra("CNN_TOP3_LABEL", result.top3Label)
                                    intentLanjut.putExtra("CNN_TOP1_CONF", result.top1LabelScore)
                                    intentLanjut.putExtra("CNN_TOP2_CONF", result.top2LabelScore)
                                    intentLanjut.putExtra("CNN_TOP3_CONF", result.top3LabelScore)
                                    intentLanjut.putExtra("CNN_TOP1_GROUP", result.top1Group)
                                    intentLanjut.putExtra("CNN_TOP2_GROUP", result.top2Group)
                                    startActivity(intentLanjut)
                                    finish()
                                }
                            }
                        }
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.e("CameraX", "Photo capture failed: ${exception.message}", exception)
                        btnCapture.visibility = View.VISIBLE
                        pbAnalisis.visibility = View.GONE
                    }
                }
            )
        }

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                Log.e("CameraX", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        // Handle rotation
        val matrix = Matrix()
        matrix.postRotate(image.imageInfo.rotationDegrees.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        baseContext, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        cnnModelExecutor.close()
    }
}
