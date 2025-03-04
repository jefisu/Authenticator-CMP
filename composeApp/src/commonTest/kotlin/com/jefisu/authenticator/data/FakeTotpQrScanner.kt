package com.jefisu.authenticator.data

import com.jefisu.authenticator.platform.TotpQrScanner

class FakeTotpQrScanner : TotpQrScanner {

    private var uri: String? = null

    fun shouldReturn(newUri: String?) {
        uri = newUri
    }

    override suspend fun extractTotpUri(imageBytes: ByteArray): String? {
        return uri
    }
}
