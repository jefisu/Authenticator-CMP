package com.jefisu.authenticator.presentation.util

import com.jefisu.authenticator.domain.model.Algorithm

fun Algorithm.displayName(): String {
    return when (this) {
        Algorithm.SHA1 -> "SHA1"
        Algorithm.SHA256 -> "SHA256"
        Algorithm.SHA384 -> "SHA384"
        Algorithm.SHA512 -> "SHA512"
    }
}
