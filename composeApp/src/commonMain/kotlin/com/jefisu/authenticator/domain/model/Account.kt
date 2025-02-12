package com.jefisu.authenticator.domain.model

import diglol.crypto.Hmac

data class Account(
    val name: String,
    val login: String,
    val secret: String,
    val issuer: Issuer?,
    val refreshPeriod: Int,
    val digitCount: Int,
    val algorithm: Hmac.Type = Hmac.Type.SHA1,
    private val _id: Int? = null
) {
    val id by lazy { _id ?: 0 }
}
