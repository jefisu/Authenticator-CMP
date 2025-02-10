package com.jefisu.authenticator.presentation.qrscanner

import arrow.optics.optics
import com.jefisu.authenticator.core.presentation.util.UiText

@optics
data class QrScannerState(
    val flashlightOn: Boolean = false,
    val createdAccount: Boolean = false,
    val error: UiText? = null
) {
    companion object
}
