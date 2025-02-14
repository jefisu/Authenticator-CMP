package com.jefisu.authenticator.data.database

import com.jefisu.authenticator.domain.model.TwoFactorAuthAccount

fun AccountEntity.toAccount(): TwoFactorAuthAccount {
    return TwoFactorAuthAccount(
        id = id,
        login = login,
        secret = secret,
        issuer = issuer,
        refreshPeriod = refreshPeriod,
        digitCount = digitCount,
        name = name,
        algorithm = algorithm
    )
}

fun TwoFactorAuthAccount.toAccountEntity(): AccountEntity {
    return AccountEntity(
        id = id,
        login = login,
        secret = secret,
        issuer = issuer,
        refreshPeriod = refreshPeriod,
        digitCount = digitCount,
        name = name,
        algorithm = algorithm
    )
}