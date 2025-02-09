package com.jefisu.authenticator.core.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import authenticator.composeapp.generated.resources.ClashGrotesk_Bold
import authenticator.composeapp.generated.resources.ClashGrotesk_Extralight
import authenticator.composeapp.generated.resources.ClashGrotesk_Light
import authenticator.composeapp.generated.resources.ClashGrotesk_Medium
import authenticator.composeapp.generated.resources.ClashGrotesk_Regular
import authenticator.composeapp.generated.resources.ClashGrotesk_Semibold
import authenticator.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

private val clashGroteskFontFamily = @Composable {
    FontFamily(
        Font(Res.font.ClashGrotesk_Bold, FontWeight.Bold),
        Font(Res.font.ClashGrotesk_Extralight, FontWeight.ExtraLight),
        Font(Res.font.ClashGrotesk_Light, FontWeight.Light),
        Font(Res.font.ClashGrotesk_Medium, FontWeight.Medium),
        Font(Res.font.ClashGrotesk_Regular, FontWeight.Normal),
        Font(Res.font.ClashGrotesk_Semibold, FontWeight.SemiBold)
    )
}

@Composable
internal fun clashGroteskTypography() = Typography().run {
    val fontFamily = clashGroteskFontFamily()
    copy(
        displayLarge = displayLarge.copy(fontFamily = fontFamily),
        displayMedium = displayMedium.copy(fontFamily = fontFamily),
        displaySmall = displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = titleLarge.copy(fontFamily = fontFamily),
        titleMedium = titleMedium.copy(fontFamily = fontFamily),
        titleSmall = titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = bodySmall.copy(fontFamily = fontFamily),
        labelLarge = labelLarge.copy(fontFamily = fontFamily),
        labelMedium = labelMedium.copy(fontFamily = fontFamily),
        labelSmall = labelSmall.copy(fontFamily = fontFamily)
    )
}
