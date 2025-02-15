package com.jefisu.authenticator.presentation.totp.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import authenticator.composeapp.generated.resources.Res
import authenticator.composeapp.generated.resources.ic_authentication_logo
import authenticator.composeapp.generated.resources.not_defined
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import com.jefisu.authenticator.core.presentation.theme.colors
import com.jefisu.authenticator.domain.model.TwoFactorAuthAccount
import com.jefisu.authenticator.presentation.totp.TotpCode
import com.jefisu.authenticator.presentation.util.getLogoUrl
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TotpCodeItem(
    totpCode: TotpCode,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val isFinalPartOfTime = remember(totpCode.remainingTime) {
        totpCode.remainingTime <= 10
    }

    val backgroundColor by animateColorAsState(
        targetValue = with(MaterialTheme.colors) {
            if (isFinalPartOfTime) optCodeFinishingBackgroundColor else optCodeBackgroundColor
        }
    )
    val contentColor by animateColorAsState(
        targetValue = with(MaterialTheme.colors) {
            if (isFinalPartOfTime) optCodeFinishingColor else optCodeColor
        }
    )

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colors.cardColor)
            .clickable(
                onClick = { onClick?.invoke() },
                enabled = onClick != null,
                indication = ripple(),
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(16.dp)
    ) {
        Row {
            ServiceIcon(totpCode.account)
            Spacer(Modifier.width(16.dp))
            AccountInfo(
                account = totpCode.account,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(Modifier.weight(1f))
            TimeIndicator(
                remainingTime = { totpCode.remainingTime },
                maxTime = totpCode.account.refreshPeriod,
                backgroundColor = backgroundColor,
                contentColor = contentColor
            )
        }
        Spacer(Modifier.height(16.dp))
        TotpCode(
            code = { totpCode.code },
            numDigits = totpCode.account.digitCount,
            backgroundColor = backgroundColor,
            contentColor = contentColor
        )
    }
}

@Composable
private fun ServiceIcon(
    account: TwoFactorAuthAccount,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(46.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.subtitleColor.copy(0.6f),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        val imageModifier = Modifier.fillMaxSize(0.6f)
        if (LocalInspectionMode.current) {
            AsyncImage(null, null, imageModifier)
        } else {
            SubcomposeAsyncImage(
                model = account.issuer?.url?.let(::getLogoUrl),
                contentDescription = null,
                modifier = imageModifier,
                loading = {
                    CircularProgressIndicator()
                },
                error = {
                    Image(
                        painter = painterResource(Res.drawable.ic_authentication_logo),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            )
        }
    }
}

@Composable
private fun AccountInfo(
    account: TwoFactorAuthAccount,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = account.issuer?.identifier ?: stringResource(Res.string.not_defined),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colors.textColor
        )
        Text(
            text = account.login,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colors.subtitleColor
        )
    }
}

@Composable
private fun TimeIndicator(
    remainingTime: () -> Int,
    maxTime: Int,
    backgroundColor: Color,
    contentColor: Color
) {
    val textMeasurer = rememberTextMeasurer()
    val progress by animateFloatAsState(
        targetValue = remainingTime().coerceAtMost(maxTime) / maxTime.toFloat(),
        animationSpec = tween(
            durationMillis = 1000,
            easing = LinearEasing
        )
    )
    val textStyle = MaterialTheme.typography.bodyMedium

    Canvas(Modifier.size(30.dp)) {
        val style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)

        drawCircle(color = backgroundColor, style = style)
        drawArc(
            color = contentColor,
            startAngle = -90f,
            sweepAngle = 360f * progress,
            useCenter = false,
            style = style
        )

        val textLayoutResult = textMeasurer.measure(
            text = remainingTime().toString(),
            style = textStyle.copy(color = contentColor)
        )
        drawText(
            textLayoutResult = textLayoutResult,
            topLeft = Offset(
                x = center.x - textLayoutResult.size.width / 2f,
                y = center.y - textLayoutResult.size.height / 2f
            )
        )
    }
}

@Composable
private fun TotpCode(
    code: () -> String,
    numDigits: Int,
    backgroundColor: Color,
    contentColor: Color,
    otpSize: Dp = 36.dp
) {
    val textMeasurer = rememberTextMeasurer()
    val textStyle = MaterialTheme.typography.titleLarge

    Canvas(
        Modifier
            .fillMaxWidth()
            .height(otpSize)
    ) {
        val otpSizePx = otpSize.toPx()
        val spaceSmall = 8.dp.toPx()
        val spaceLarge = 16.dp.toPx()

        val groupCount = numDigits / 3
        val totalWidth = (otpSizePx * numDigits) +
                (groupCount * spaceLarge) +
                ((numDigits - groupCount - 1) * spaceSmall)

        val scaleFactor = if (totalWidth > size.width) 0.8f else 1f
        val otpCenter = Offset(otpSizePx / 2, otpSizePx / 2)
        var currentOffset = 0f

        scale(scale = scaleFactor, pivot = otpCenter) {
            code().take(numDigits).forEachIndexed { index, char ->
                drawRoundRect(
                    color = backgroundColor,
                    cornerRadius = CornerRadius(8.dp.toPx()),
                    size = Size(otpSizePx, otpSizePx),
                    topLeft = Offset(x = currentOffset, y = 0f)
                )

                val textResult = textMeasurer.measure(
                    text = char.toString(),
                    style = textStyle.copy(color = contentColor)
                )

                drawText(
                    textLayoutResult = textResult,
                    topLeft = Offset(
                        x = currentOffset + otpCenter.x - (textResult.size.width / 2),
                        y = otpCenter.y - textResult.size.height / 2
                    )
                )

                val isGroupSeparator = (index + 1) % 3 == 0
                val space = if (isGroupSeparator) spaceLarge else spaceSmall
                currentOffset += otpSizePx + space
            }
        }
    }
}
