package com.jefisu.authenticator.util

import com.jefisu.authenticator.domain.model.Algorithm
import com.jefisu.authenticator.domain.model.TwoFactorAuthAccount

object TestUtil {
    const val VALID_TOTP_URI =
        "otpauth://totp/test@exemple.com?secret=ABCD1234EFGH5678IJKL9012MNOP3456&issuer=Example&algorithm=SHA1&digits=6&period=30"

    val VALID_ACCOUNT = TwoFactorAuthAccount(
        name = "Test",
        login = "test@example.com",
        secret = "ABCD1234EFGH5678IJKL9012MNOP3456",
        issuer = null,
        refreshPeriod = 30,
        digitCount = 6,
        algorithm = Algorithm.SHA1
    )

    val INVALID_QR_BYTES = byteArrayOf(0x1, 0x2, 0x3)
    val EMPTY_QR_BYTES = byteArrayOf()
    const val INVALID_TOTP_URI = "otp://malformed-uri"
    val VALID_QR_BYTES = byteArrayOf(0x4, 0x5, 0x6)
}
