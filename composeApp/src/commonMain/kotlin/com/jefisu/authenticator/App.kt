@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.jefisu.authenticator

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.jefisu.authenticator.core.presentation.navigation.AppNavHost
import com.jefisu.authenticator.core.presentation.sharedtransition.LocalSharedTransitionScope
import com.jefisu.authenticator.core.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() = AppTheme {
    SharedTransitionLayout {
        CompositionLocalProvider(
            LocalSharedTransitionScope provides this,
            content = { AppNavHost() }
        )
    }
}
