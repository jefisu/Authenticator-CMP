package com.jefisu.authenticator.domain.util

interface Error {

    data object Unknown : Error

    companion object {
        fun Exception.toError(): Error {
            printStackTrace()
            return Unknown
        }
    }
}
