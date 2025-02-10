package com.jefisu.authenticator.presentation.util

import authenticator.composeapp.generated.resources.Res
import authenticator.composeapp.generated.resources.invalid_qr_code
import com.jefisu.authenticator.core.presentation.util.UiText
import com.jefisu.authenticator.domain.util.DataError

fun DataError.asUiText(): UiText {
    val resId = when (this) {
        DataError.Account.INVALID_QR_CODE -> Res.string.invalid_qr_code
    }
    return UiText.StringResource(resId)
}
