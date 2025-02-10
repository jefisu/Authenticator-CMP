package com.jefisu.authenticator.core.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {

    @Serializable
    data object TotpScreen : Destination
}
