package com.jefisu.authenticator.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class Algorithm(
    val length: Int,
) {
    SHA1(length = 32),
    SHA256(length = 52),
    SHA384(length = 77),
    SHA512(length = 103)
}
