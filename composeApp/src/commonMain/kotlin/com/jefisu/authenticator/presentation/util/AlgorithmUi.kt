package com.jefisu.authenticator.presentation.util

import diglol.crypto.Hmac

fun Hmac.Type.displayName(): String {
    return when (this) {
        Hmac.Type.SHA1 -> "SHA1"
        Hmac.Type.SHA256 -> "SHA256"
        Hmac.Type.SHA384 -> "SHA384"
        Hmac.Type.SHA512 -> "SHA512"
        else -> throw IllegalArgumentException("Invalid Hmac type")
    }
}
