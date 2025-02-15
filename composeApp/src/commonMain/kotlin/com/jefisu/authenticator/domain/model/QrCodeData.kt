package com.jefisu.authenticator.domain.model

sealed interface QrCodeData {
    data class TotpUri(val uri: String) : QrCodeData
    data class ImageBytes(val bytes: ByteArray?) : QrCodeData
}
