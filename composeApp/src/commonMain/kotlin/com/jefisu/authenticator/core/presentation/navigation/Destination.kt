package com.jefisu.authenticator.core.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {

    @Serializable
    data object TotpScreen : Destination

    @Serializable
    data object QrScannerScreen : Destination

    @Serializable
    data class AddKeyManuallyScreen(val id: Int?) : Destination
}
