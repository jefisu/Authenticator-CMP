package com.jefisu.authenticator.domain.model

data class Account(
    val name: String,
    val login: String,
    val secret: String,
    val issuer: Issuer?,
    val refreshPeriod: Int,
    val digitCount: Int,
    val id: Int? = null
)
