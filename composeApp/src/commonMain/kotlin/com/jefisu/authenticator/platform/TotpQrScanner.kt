package com.jefisu.authenticator.platform

interface TotpQrScanner {
    suspend fun extractTotpUri(imageBytes: ByteArray): String?
}

expect class TotpQrScannerImpl() : TotpQrScanner {
    override suspend fun extractTotpUri(imageBytes: ByteArray): String?
}