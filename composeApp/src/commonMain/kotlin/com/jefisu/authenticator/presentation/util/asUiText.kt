package com.jefisu.authenticator.presentation.util

import authenticator.composeapp.generated.resources.Res
import authenticator.composeapp.generated.resources.invalid_login
import authenticator.composeapp.generated.resources.invalid_qr_code
import authenticator.composeapp.generated.resources.invalid_secret
import authenticator.composeapp.generated.resources.unknown_error
import com.jefisu.authenticator.core.presentation.util.UiText
import com.jefisu.authenticator.domain.util.Error
import com.jefisu.authenticator.domain.validation.AccountValidationError

fun Error.asUiText(): UiText {
    val resId = when (this) {
        AccountValidationError.INVALID_QR_CODE -> Res.string.invalid_qr_code
        AccountValidationError.INVALID_LOGIN -> Res.string.invalid_login
        AccountValidationError.INVALID_SECRET -> Res.string.invalid_secret
        else -> Res.string.unknown_error
    }
    return UiText.StringResource(resId)
}
