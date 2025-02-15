package com.jefisu.authenticator.domain.validation

import com.jefisu.authenticator.domain.util.Error

enum class QrValidationError : Error {
    INVALID_QR_CODE,
    QR_EXTRACTION_FAILED,
    INVALID_PARSE_TOTP_URI
}