package com.jefisu.authenticator.data.util

import com.jefisu.authenticator.domain.util.Error
import com.jefisu.authenticator.domain.util.Error.Companion.toError
import com.jefisu.authenticator.domain.util.Result
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
inline fun <D, E : Error> runSafelyResult(
    errors: Map<KClass<out Exception>, E> = emptyMap(),
    block: () -> D
): Result<D, E> {
    return try {
        Result.Success(block())
    } catch (e: Exception) {
        val error = errors.entries.find { it.key.isInstance(e) }?.value
        Result.Error((error ?: e.toError()) as E)
    }
}
