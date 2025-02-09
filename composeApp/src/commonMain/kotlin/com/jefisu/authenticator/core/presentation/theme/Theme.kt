package com.jefisu.authenticator.core.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf

private val lightColors = Colors(
    backgroundColor = LightBackgroundColor,
    textColor = LightTextColor,
    iconColor = LightIconColor,
    subtitleColor = LightSubtitleColor,
    cardColor = LightCardColor,
    optCodeBackgroundColor = LightOptCodeBackgroundColor,
    optCodeFinishingBackgroundColor = LightOptCodeFinishingBackgroundColor,
    optCodeColor = LightOptCodeColor,
    optCodeFinishingColor = LightOptCodeFinishingColor
)

private val darkColors = Colors(
    backgroundColor = DarkBackgroundColor,
    textColor = DarkTextColor,
    iconColor = DarkIconColor,
    subtitleColor = DarkSubtitleColor,
    cardColor = DarkCardColor,
    optCodeBackgroundColor = DarkOptCodeBackgroundColor,
    optCodeFinishingBackgroundColor = DarkOptCodeFinishingBackgroundColor,
    optCodeColor = DarkOptCodeColor,
    optCodeFinishingColor = DarkOptCodeFinishingColor
)

internal val LocalColors = compositionLocalOf { lightColors }

internal val MaterialTheme.colors: Colors
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current

internal val LocalThemeIsDark = compositionLocalOf { true }

@Composable
internal fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (isDarkTheme) darkColors else lightColors
    val materialThemeColors = MaterialTheme.colorScheme.copy(
        primary = colors.textColor,
        background = colors.backgroundColor,
        surface = colors.backgroundColor
    )

    CompositionLocalProvider(
        LocalThemeIsDark provides isDarkTheme,
        LocalColors provides colors,
        LocalContentColor provides colors.textColor
    ) {
        SystemAppearance(!isDarkTheme)
        MaterialTheme(
            colorScheme = materialThemeColors,
            typography = clashGroteskTypography()
        ) {
            Surface(
                contentColor = colors.textColor,
                content = content
            )
        }
    }
}

@Composable
internal expect fun SystemAppearance(isDark: Boolean)
