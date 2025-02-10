package com.jefisu.authenticator.core.presentation.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jefisu.authenticator.core.presentation.sharedtransition.LocalAnimatedContentScope
import com.jefisu.authenticator.presentation.qrscanner.QrScannerScreen
import com.jefisu.authenticator.presentation.totp.TotpScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destination.TotpScreen,
        modifier = Modifier.fillMaxSize()
    ) {
        composable<Destination.TotpScreen> {
            ProvideAnimatedContentScope {
                TotpScreen(
                    onNavigateQrScanner = { navController.navigate(Destination.QrScannerScreen) },
                    onNavigateEnterKeyManually = {}
                )
            }
        }
        composable<Destination.QrScannerScreen> {
            ProvideAnimatedContentScope {
                QrScannerScreen(
                    onNavigateBack = navController::navigateUp,
                    onNavigateToEnterKeyManually = {}
                )
            }
        }
    }
}

@Composable
private fun AnimatedContentScope.ProvideAnimatedContentScope(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalAnimatedContentScope provides this,
        content = content
    )
}
