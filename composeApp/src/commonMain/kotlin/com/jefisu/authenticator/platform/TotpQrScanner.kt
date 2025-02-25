package com.jefisu.authenticator.platform

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class TotpQrScanner() {

    suspend fun extractTotpUri(imageBytes: ByteArray): String?
}
