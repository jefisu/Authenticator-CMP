@file:OptIn(ExperimentalForeignApi::class)

package com.jefisu.authenticator.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.jefisu.authenticator.data.database.AccountDatabase.Companion.DATABASE_NAME
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual fun getAccountDatabaseBuilder(): RoomDatabase.Builder<AccountDatabase> {
    val dbFile = "${documentDirectory()}/$DATABASE_NAME"
    return Room.databaseBuilder<AccountDatabase>(name = dbFile)
}

private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}
