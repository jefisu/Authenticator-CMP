@file:OptIn(ExperimentalForeignApi::class)

package com.jefisu.authenticator.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.CoreImage.CIDetector
import platform.CoreImage.CIDetectorAccuracy
import platform.CoreImage.CIDetectorAccuracyHigh
import platform.CoreImage.CIDetectorTypeQRCode
import platform.CoreImage.CIImage
import platform.CoreImage.CIQRCodeFeature
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes
import platform.UIKit.UIImage
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class TotpQrScanner {
    actual suspend fun extractTotpUri(imageBytes: ByteArray): String? {
        return suspendCoroutine { continuation ->
            val uiImage = UIImage.imageWithData(imageBytes.toNSData()) ?: run {
                continuation.resume(null)
                return@suspendCoroutine
            }
            val ciImage = CIImage.imageWithCGImage(uiImage.CGImage)
            val detector = CIDetector.detectorOfType(
                type = CIDetectorTypeQRCode,
                context = null,
                options = mapOf(CIDetectorAccuracy to CIDetectorAccuracyHigh)
            )

            val features = detector?.featuresInImage(ciImage)
            val totpUri = features
                ?.filterIsInstance<CIQRCodeFeature>()
                ?.firstOrNull()
                ?.messageString

            continuation.resume(totpUri)
        }
    }

    private fun ByteArray.toNSData(): NSData {
        return usePinned { pinned ->
            NSData.dataWithBytes(pinned.addressOf(0), size.toULong())
        }
    }
}
