package com.jefisu.authenticator.domain.util

typealias RootError = Error

sealed interface Result<out D, out E> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E : RootError>(val error: E) : Result<Nothing, E>
}

inline fun <D, E> Result<D, E>.onSuccess(block: (D) -> Unit): Result<D, E> {
    if (this is Result.Success) block(data)
    return this
}

inline fun <D, E> Result<D, E>.onError(block: (E) -> Unit): Result<D, E> {
    if (this is Result.Error) block(error)
    return this
}
