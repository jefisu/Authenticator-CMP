package com.jefisu.authenticator.data.database

import androidx.room.TypeConverter
import com.jefisu.authenticator.domain.model.Algorithm

class AlgorithmConverter {

    @TypeConverter
    fun algorithmToOrdinal(algorithm: Algorithm) = algorithm.ordinal

    @TypeConverter
    fun ordinalToAlgorithm(ordinal: Int) = Algorithm.entries[ordinal]
}