package com.jefisu.authenticator.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.jefisu.authenticator.domain.model.Algorithm
import com.jefisu.authenticator.domain.model.Issuer

@Entity(
    indices = [Index(value = ["secret"], unique = true)]
)
data class AccountEntity(
    val name: String,
    val login: String,
    val secret: String,
    val issuer: Issuer?,
    val algorithm: Algorithm,
    val refreshPeriod: Int,
    val digitCount: Int,
    @PrimaryKey(autoGenerate = true) val id: Int? = null
)
