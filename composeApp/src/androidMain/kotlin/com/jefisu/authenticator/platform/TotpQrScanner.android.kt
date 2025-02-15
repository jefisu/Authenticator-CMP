package com.jefisu.authenticator.platform

import android.graphics.BitmapFactory
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

actual class TotpQrScanner {
    actual suspend fun extractTotpUri(imageBytes: ByteArray): String? {
        return withContext(Dispatchers.IO) {
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                ?: return@withContext null
            val scanner = BarcodeScanning.getClient()
            val inputImage = InputImage.fromBitmap(bitmap, 0)

            suspendCancellableCoroutine { continuation ->
                scanner.process(inputImage)
                    .addOnSuccessListener { barcodes ->
                        val uri = barcodes.firstOrNull()?.rawValue
                        continuation.resume(uri)
                    }
                    .addOnFailureListener {
                        continuation.resume(null)
                    }
            }
        }
    }
}
