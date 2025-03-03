package com.jefisu.authenticator.di

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.jefisu.authenticator.data.database.AccountDatabase
import org.koin.dsl.module

val testModule = module {
    single {
        Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AccountDatabase::class.java
        ).build()
    }
}