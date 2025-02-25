package com.jefisu.authenticator.presentation.util

import authenticator.composeapp.generated.resources.Res
import authenticator.composeapp.generated.resources.invalid_login
import authenticator.composeapp.generated.resources.invalid_parse_totp_uri
import authenticator.composeapp.generated.resources.invalid_qr_code
import authenticator.composeapp.generated.resources.invalid_secret
import authenticator.composeapp.generated.resources.qr_extraction_failed
import authenticator.composeapp.generated.resources.unknown_error
import com.jefisu.authenticator.core.presentation.util.UiText
import com.jefisu.authenticator.domain.util.Error
import com.jefisu.authenticator.domain.validation.AccountValidationError
import com.jefisu.authenticator.domain.validation.QrValidationError

fun Error.asUiText(): UiText {
    val resId = when (this) {
        AccountValidationError.INVALID_QR_CODE -> Res.string.invalid_qr_code
        AccountValidationError.INVALID_LOGIN -> Res.string.invalid_login
        AccountValidationError.INVALID_SECRET -> Res.string.invalid_secret
        QrValidationError.INVALID_QR_CODE -> Res.string.invalid_qr_code
        QrValidationError.QR_EXTRACTION_FAILED -> Res.string.qr_extraction_failed
        QrValidationError.INVALID_PARSE_TOTP_URI -> Res.string.invalid_parse_totp_uri
        else -> Res.string.unknown_error
    }
    return UiText.StringResource(resId)
}
