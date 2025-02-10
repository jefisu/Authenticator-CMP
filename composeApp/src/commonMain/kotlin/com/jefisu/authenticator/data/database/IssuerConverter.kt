package com.jefisu.authenticator.data.database

import androidx.room.TypeConverter
import com.jefisu.authenticator.domain.model.Issuer
import com.jefisu.authenticator.domain.util.DefaultIssuer

class IssuerConverter {

    @TypeConverter
    fun fromIssuerToString(issuer: Issuer) = issuer.identifier

    @TypeConverter
    fun fromStringToIssuer(identifier: String) = DefaultIssuer.getIssuer(identifier)
}