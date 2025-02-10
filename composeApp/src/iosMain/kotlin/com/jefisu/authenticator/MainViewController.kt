package com.jefisu.authenticator

import androidx.compose.ui.window.ComposeUIViewController
import com.jefisu.authenticator.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }
