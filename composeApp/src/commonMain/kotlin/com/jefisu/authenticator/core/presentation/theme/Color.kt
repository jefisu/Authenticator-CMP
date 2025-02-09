package com.jefisu.authenticator.core.presentation.theme

import androidx.compose.ui.graphics.Color

data class Colors(
    val backgroundColor: Color,
    val textColor: Color,
    val iconColor: Color,
    val subtitleColor: Color,
    val cardColor: Color,
    val optCodeBackgroundColor: Color,
    val optCodeFinishingBackgroundColor: Color,
    val optCodeColor: Color,
    val optCodeFinishingColor: Color
)

internal val LightBackgroundColor = Color(0xFFe6e6e6)
internal val LightTextColor = Color(0xFF2d2d2d)
internal val LightIconColor = Color(0xFF444444)
internal val LightSubtitleColor = Color(0xFF9e9e9e)
internal val LightCardColor = Color.White
internal val LightOptCodeBackgroundColor = Color(0xFFeff7ff)
internal val LightOptCodeFinishingBackgroundColor = Color(0xFFfef2ee)
internal val LightOptCodeColor = Color(0xFF1665be)
internal val LightOptCodeFinishingColor = Color(0xFFe56059)

internal val DarkBackgroundColor = Color(0xFF212121)
internal val DarkTextColor = Color(0xFFfdfdfd)
internal val DarkIconColor = Color.White
internal val DarkSubtitleColor = Color(0xFF9ea0a4)
internal val DarkCardColor = Color(0xFF2f3135)
internal val DarkOptCodeBackgroundColor = Color(0xFF212121)
internal val DarkOptCodeFinishingBackgroundColor = Color(0xFF1e2121)
internal val DarkOptCodeColor = Color.White
internal val DarkOptCodeFinishingColor = Color(0xFFe46758)
