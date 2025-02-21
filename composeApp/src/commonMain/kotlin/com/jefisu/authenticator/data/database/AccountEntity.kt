package com.jefisu.authenticator.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AccountEntity(
    val encryptedBase64: String,
    @PrimaryKey(autoGenerate = true) val id: Int? = null
)
