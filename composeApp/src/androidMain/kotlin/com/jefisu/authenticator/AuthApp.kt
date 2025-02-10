package com.jefisu.authenticator

import android.app.Application
import com.jefisu.authenticator.di.initKoin
import org.koin.android.ext.koin.androidContext

class AuthApp : Application() {
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        initKoin {
            androidContext(this@AuthApp)
        }
    }

    companion object {
        lateinit var INSTANCE: AuthApp
    }
}
