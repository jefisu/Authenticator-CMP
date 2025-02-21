package com.jefisu.authenticator.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(entities = [AccountEntity::class], version = 1)
@ConstructedBy(AccountDatabaseConstructor::class)
abstract class AccountDatabase : RoomDatabase() {
    abstract val accountDao: AccountDao

    companion object {
        const val DATABASE_NAME = "account.db"
    }
}

@Suppress("NO_ACTUAL_FOR_EXPECT", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object AccountDatabaseConstructor : RoomDatabaseConstructor<AccountDatabase> {
    override fun initialize(): AccountDatabase
}

expect fun getAccountDatabaseBuilder(): RoomDatabase.Builder<AccountDatabase>

fun getAccountDatabase(): AccountDatabase {
    return getAccountDatabaseBuilder()
        .fallbackToDestructiveMigrationOnDowngrade(false)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}