package com.jefisu.authenticator.domain.model

data class Account(
    val name: String,
    val login: String,
    val secret: String,
    val issuer: Issuer,
    val id: Int,
    val refreshPeriod: Int,
    val digitCount: Int
)
