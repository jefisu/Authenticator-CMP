package com.jefisu.authenticator.domain.model

import arrow.optics.optics
import kotlinx.serialization.Serializable

@optics
@Serializable
data class TwoFactorAuthAccount(
    val name: String,
    val login: String,
    val secret: String,
    val issuer: Issuer?,
    val refreshPeriod: Int,
    val digitCount: Int,
    val algorithm: Algorithm,
    val id: Int? = null
) {
    companion object
}
