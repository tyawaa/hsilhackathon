package com.example.hsilhackathon

import android.content.Context
import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import java.nio.FloatBuffer

class RFModelExecutor(context: Context) {

    private var ortEnv: OrtEnvironment? = null
    private var ortSession: OrtSession? = null

    init {
        try {
            ortEnv = OrtEnvironment.getEnvironment()
            val assetStream = context.assets.open("rf_hybrid_best_model.onnx")
            val bytes = assetStream.readBytes()
            assetStream.close()
            ortSession = ortEnv?.createSession(bytes, OrtSession.SessionOptions())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Takes a map of feature names to values (String or Float/Double)
    fun predict(features: Map<String, Any>): String {
        val env = ortEnv ?: return "Sistem Belum Siap"
        val session = ortSession ?: return "Sistem Belum Siap"

        val inputMap = HashMap<String, OnnxTensor>()

        try {
            // Cek apakah model membutuhkan float array tunggal (seperti 'X' atau 'float_input')
            val inputInfo = session.inputInfo
            if (inputInfo.size == 1) {
                // Semua feature harus digabung menjadi float array berdasarkan urutan rf_hybrid_feature_schema
                // Karena kita tidak punya schema parser langsung di contoh ini, kita fallback
                val firstInputName = inputInfo.keys.first()
                val featureValues = FloatArray(35) // Hardcoded to 35 for fallback mapping
                // For a real app, you would map `features` to a FloatArray using the schema JSON.
                // Assuming it's simple numeric extraction:
                var i = 0
                for ((_, v) in features) {
                    if (i < 35) {
                        featureValues[i] = if (v is Number) v.toFloat() else 0f
                        i++
                    }
                }
                val buffer = FloatBuffer.wrap(featureValues)
                val tensor = OnnxTensor.createTensor(env, buffer, longArrayOf(1, 35))
                inputMap[firstInputName] = tensor
            } else {
                // Model menerima multiple inputs (e.g. DataFrame style)
                for ((key, info) in inputInfo) {
                    val value = features[key]
                    if (value == null) {
                        // Fallback sesuai tipe
                        val tensor = if (info.info.toString().contains("STRING")) {
                            OnnxTensor.createTensor(env, arrayOf(""))
                        } else {
                            OnnxTensor.createTensor(env, FloatBuffer.wrap(floatArrayOf(0f)), longArrayOf(1, 1))
                        }
                        inputMap[key] = tensor
                    } else {
                        val tensor = if (value is String) {
                            OnnxTensor.createTensor(env, arrayOf(value))
                        } else {
                            val fVal = (value as Number).toFloat()
                            OnnxTensor.createTensor(env, FloatBuffer.wrap(floatArrayOf(fVal)), longArrayOf(1, 1))
                        }
                        inputMap[key] = tensor
                    }
                }
            }

            val result = session.run(inputMap)
            val outputInfo = result[0].value
            
            // Clean up tensors
            inputMap.values.forEach { it.close() }
            result.close()

            // Extract string or long prediction
            if (outputInfo is LongArray) {
                val labelOrder = arrayOf("CLM", "Kusta", "Scabies", "Tinea")
                val idx = outputInfo[0].toInt()
                return if (idx in labelOrder.indices) labelOrder[idx] else "Tidak diketahui"
            } else if (outputInfo is Array<*>) {
                return outputInfo[0].toString()
            }
            
            return outputInfo.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            // Cleanup on catch
            inputMap.values.forEach { it.close() }
            return "RF Error"
        }
    }

    fun close() {
        ortSession?.close()
        ortEnv?.close()
    }
}
