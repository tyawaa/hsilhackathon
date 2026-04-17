package com.example.hsilhackathon

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

class CNNModelExecutor(context: Context) {
    private var interpreter: Interpreter? = null
    
    // Asumsikan model memiliki ukuran input 224x224
    private val INPUT_SIZE = 224
    
    // Label arrays berdasarkan urutan yang diharapkan. 
    // Anda bisa mengganti urutannya sesuai dengan `cnn_label_order.json`
    private val labelClasses = arrayOf("CLM", "Kusta", "Scabies", "Tinea")
    private val groupClasses = arrayOf("Bercak Merah", "Bintil Merah")

    init {
        try {
            val assetFileDescriptor = context.assets.openFd("cnn_model_fp32.tflite")
            val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
            val fileChannel = fileInputStream.channel
            val startOffset = assetFileDescriptor.startOffset
            val declaredLength = assetFileDescriptor.declaredLength
            val modelBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
            
            val options = Interpreter.Options()
            options.setNumThreads(4)
            interpreter = Interpreter(modelBuffer, options)
        } catch (e: Exception) {
            e.printStackTrace()
            // Model belum tersedia, kita tidak throw error agar bisa dijalankan manual
        }
    }

    data class CNNResult(
        val top1Label: String, val top1LabelScore: Float,
        val top2Label: String, val top2LabelScore: Float,
        val top3Label: String, val top3LabelScore: Float,
        val top1Group: String, val top1GroupScore: Float,
        val top2Group: String, val top2GroupScore: Float
    )

    fun predict(bitmap: Bitmap): CNNResult {
        // Jika model tidak load, kembalikan mock data
        if (interpreter == null) {
            return CNNResult("Kusta", 0.8f, "CLM", 0.15f, "Tinea", 0.05f, "Bercak Merah", 0.9f, "Bintil Merah", 0.1f)
        }

        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true)
        val inputBuffer = convertBitmapToByteBuffer(resizedBitmap)
        
        // Output dari multitask model asumsi 2 output array
        val labelOutput = Array(1) { FloatArray(labelClasses.size) }
        val groupOutput = Array(1) { FloatArray(groupClasses.size) }
        
        val outputMap = HashMap<Int, Any>()
        outputMap[0] = labelOutput    // Asumsi index 0 adalah output label class
        outputMap[1] = groupOutput    // Asumsi index 1 adalah output group class
        
        try {
            interpreter?.runForMultipleInputsOutputs(arrayOf(inputBuffer), outputMap)
        } catch (e: Exception) {
            e.printStackTrace()
            return CNNResult("Kusta", 0.8f, "CLM", 0.15f, "Tinea", 0.05f, "Bercak Merah", 0.9f, "Bintil Merah", 0.1f)
        }
        
        val lOut = labelOutput[0]
        val sortedLabels = lOut.indices.map { Pair(labelClasses[it], lOut[it]) }.sortedByDescending { it.second }
        
        val gOut = groupOutput[0]
        val sortedGroups = gOut.indices.map { Pair(groupClasses[it], gOut[it]) }.sortedByDescending { it.second }

        return CNNResult(
            top1Label = sortedLabels.getOrNull(0)?.first ?: "",
            top1LabelScore = sortedLabels.getOrNull(0)?.second ?: 0f,
            top2Label = sortedLabels.getOrNull(1)?.first ?: "",
            top2LabelScore = sortedLabels.getOrNull(1)?.second ?: 0f,
            top3Label = sortedLabels.getOrNull(2)?.first ?: "",
            top3LabelScore = sortedLabels.getOrNull(2)?.second ?: 0f,
            top1Group = sortedGroups.getOrNull(0)?.first ?: "",
            top1GroupScore = sortedGroups.getOrNull(0)?.second ?: 0f,
            top2Group = sortedGroups.getOrNull(1)?.first ?: "",
            top2GroupScore = sortedGroups.getOrNull(1)?.second ?: 0f
        )
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * INPUT_SIZE * INPUT_SIZE * 3)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(INPUT_SIZE * INPUT_SIZE)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel = 0
        for (i in 0 until INPUT_SIZE) {
            for (j in 0 until INPUT_SIZE) {
                val `val` = intValues[pixel++]
                // Normalize 0-1
                byteBuffer.putFloat(((`val` shr 16) and 0xFF) / 255.0f)
                byteBuffer.putFloat(((`val` shr 8) and 0xFF) / 255.0f)
                byteBuffer.putFloat((`val` and 0xFF) / 255.0f)
            }
        }
        return byteBuffer
    }
    
    fun close() {
        interpreter?.close()
    }
}
