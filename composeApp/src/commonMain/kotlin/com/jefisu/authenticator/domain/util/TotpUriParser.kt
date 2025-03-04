package com.jefisu.authenticator.domain.util

import com.jefisu.authenticator.domain.model.TwoFactorAuthAccount

interface TotpUriParser {
    fun parse(uri: String): TwoFactorAuthAccount?
}
