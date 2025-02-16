@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.jefisu.authenticator.core.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jefisu.authenticator.core.presentation.sharedtransition.LocalAnimatedContentScope
import com.jefisu.authenticator.core.presentation.sharedtransition.LocalSharedTransitionScope
import com.jefisu.authenticator.core.presentation.sharedtransition.SharedTransitionKeys.TEXT_FIELD_HINT_KEY
import com.jefisu.authenticator.core.presentation.sharedtransition.sharedTransition
import com.jefisu.authenticator.core.presentation.theme.colors

@Composable
fun AnimatedTextFieldHint(
    text: String,
    onTextChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    iconTrailing: (@Composable () -> Unit)? = {
        ClearTextIconButton(onClick = { onTextChange("") })
    }
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val showHintAbove by remember(text) {
        derivedStateOf {
            isFocused || text.isNotEmpty()
        }
    }

    BasicTextField(
        value = text,
        onValueChange = { onTextChange(it) },
        modifier = modifier,
        interactionSource = interactionSource,
        singleLine = true,
        textStyle = textFieldTextStyle(),
        cursorBrush = SolidColor(MaterialTheme.colors.textColor),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = { innerTextField ->
            SharedTransitionLayout {
                AnimatedContent(
                    targetState = showHintAbove,
                    transitionSpec = { EnterTransition.None togetherWith ExitTransition.None },
                    label = "hintAnimation"
                ) { shouldShowHintAbove ->
                    CompositionLocalProvider(
                        LocalSharedTransitionScope provides this@SharedTransitionLayout,
                        LocalAnimatedContentScope provides this
                    ) {
                        Column {
                            HintBox(showHintAbove = shouldShowHintAbove, hint = hint)
                            Spacer(Modifier.height(2.dp))
                            InputBox(
                                text = text,
                                showHintAbove = shouldShowHintAbove,
                                hint = hint,
                                iconTrailing = iconTrailing,
                                innerTextField = innerTextField
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun HintBox(
    showHintAbove: Boolean,
    hint: String
) {
    val exteriorHintTextStyle = MaterialTheme.typography.labelLarge.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        color = MaterialTheme.colors.optCodeColor
    )

    Box(Modifier.padding(start = 2.dp)) {
        InvisibleTextAsPlaceholder(exteriorHintTextStyle)
        if (showHintAbove) {
            TextAsIndividualLetters(
                text = hint,
                style = exteriorHintTextStyle
            )
        }
    }
}

@Composable
private fun InputBox(
    text: String,
    showHintAbove: Boolean,
    hint: String,
    iconTrailing: (@Composable () -> Unit)?,
    innerTextField: @Composable () -> Unit
) {
    val interiorHintTextStyle = textFieldTextStyle().copy(
        color = MaterialTheme.colors.textColor.copy(alpha = .4f)
    )

    val trailingContent: @Composable RowScope.() -> Unit = {
        AnimatedVisibility(
            visible = text.isNotEmpty(),
            enter = fadeIn() + slideInHorizontally { it / 3 },
            exit = fadeOut() + slideOutHorizontally { it / 3 },
        ) {
            iconTrailing?.invoke()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colors.textColor.copy(alpha = .3f)
            )
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        if (!showHintAbove) {
            TextAsIndividualLetters(
                text = hint,
                style = interiorHintTextStyle
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                innerTextField()
            }
            trailingContent()
        }
    }
}

@Composable
private fun InvisibleTextAsPlaceholder(style: TextStyle) {
    Text(
        text = "",
        modifier = Modifier.alpha(0f),
        style = style
    )
}

@Composable
fun TextAsIndividualLetters(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        text.forEachIndexed { index, letter ->
            Text(
                text = "$letter",
                style = style,
                modifier = Modifier
                    .sharedTransition {
                        Modifier.sharedBounds(
                            sharedContentState = rememberSharedContentState(
                                key = "$TEXT_FIELD_HINT_KEY$index"
                            ),
                            animatedVisibilityScope = it,
                            boundsTransform = { _, _ ->
                                spring(
                                    stiffness = 25f * (text.length - index),
                                    dampingRatio = Spring.DampingRatioLowBouncy
                                )
                            }
                        )
                    }
            )
        }
    }
}

@Composable
private fun ClearTextIconButton(onClick: () -> Unit) {
    Icon(
        imageVector = Icons.Rounded.Close,
        contentDescription = "Clear text",
        tint = MaterialTheme.colors.textColor,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(
                    bounded = false,
                    radius = 16.dp
                ),
                onClick = onClick
            )
    )
}

@ReadOnlyComposable
@Composable
private fun textFieldTextStyle() = MaterialTheme.typography.bodyLarge.copy(
    color = MaterialTheme.colors.textColor.copy(alpha = .9f)
)
