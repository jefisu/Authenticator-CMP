package com.jefisu.authenticator.data.database

import com.jefisu.authenticator.domain.model.TwoFactorAuthAccount
import kotlinx.serialization.json.Json

fun AccountEntity.toAccount(decryptedJson: String): TwoFactorAuthAccount {
    val account = Json.decodeFromString<TwoFactorAuthAccount>(decryptedJson)
    return account.copy(id = id)
}

fun TwoFactorAuthAccount.toAccountEntity(encryptedBase64: String): AccountEntity {
    return AccountEntity(
        id = id,
        encryptedBase64 = encryptedBase64
    )
}