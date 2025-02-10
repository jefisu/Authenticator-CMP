package com.jefisu.authenticator.domain.util

sealed interface DataError : Error {

    enum class Account : DataError {
        INVALID_QR_CODE
    }
}
