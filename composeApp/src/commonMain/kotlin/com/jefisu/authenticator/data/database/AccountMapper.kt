package com.jefisu.authenticator.data.database

import com.jefisu.authenticator.domain.model.Account

fun AccountEntity.toAccount(): Account {
    return Account(
        _id = id,
        login = login,
        secret = secret,
        issuer = issuer,
        refreshPeriod = refreshPeriod,
        digitCount = digitCount,
        name = name,
    )
}

fun Account.toAccountEntity(): AccountEntity {
    return AccountEntity(
        id = id,
        login = login,
        secret = secret,
        issuer = issuer,
        refreshPeriod = refreshPeriod,
        digitCount = digitCount,
        name = name
    )
}