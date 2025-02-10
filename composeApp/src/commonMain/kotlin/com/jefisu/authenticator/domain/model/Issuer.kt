package com.jefisu.authenticator.domain.model

interface Issuer {
    val identifier: String
    val url: String
}

data class CustomIssuer(
    override val identifier: String,
    override val url: String
) : Issuer
