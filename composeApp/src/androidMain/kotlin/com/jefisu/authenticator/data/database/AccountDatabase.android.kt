package com.jefisu.authenticator.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.jefisu.authenticator.AuthApp
import com.jefisu.authenticator.data.database.AccountDatabase.Companion.DATABASE_NAME

actual fun getAccountDatabaseBuilder(): RoomDatabase.Builder<AccountDatabase> {
    val context = AuthApp.INSTANCE
    val dbFile = context.getDatabasePath(DATABASE_NAME)
    return Room.databaseBuilder<AccountDatabase>(context, dbFile.absolutePath)
}
