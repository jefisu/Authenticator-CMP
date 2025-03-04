@file:OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package com.jefisu.authenticator.presentation.totp.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import authenticator.composeapp.generated.resources.Res
import authenticator.composeapp.generated.resources.search_hint
import com.jefisu.authenticator.core.presentation.theme.colors
import com.jefisu.authenticator.presentation.util.TestTag
import org.jetbrains.compose.resources.stringResource

@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    val textColor = MaterialTheme.colors.textColor
    val inactiveTextColor = textColor.copy(alpha = .4f)
    val textStyle = LocalTextStyle.current

    val trailingIcon: @Composable RowScope.() -> Unit = {
        AnimatedVisibility(
            visible = query.isNotEmpty(),
            enter = fadeIn() + slideInHorizontally { it / 3 },
            exit = fadeOut() + slideOutHorizontally { it / 3 }
        ) {
            IconButton(
                onClick = { onQueryChange("") },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = LocalContentColor.current
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Clear text"
                )
            }
        }
    }

    Surface(
        contentColor = textColor,
        modifier = modifier
            .windowInsetsPadding(TopAppBarDefaults.windowInsets)
            .height(TopAppBarDefaults.TopAppBarExpandedHeight)
    ) {
        Column {
            Row(
                modifier = Modifier.height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically
            ) {
                leadingIcon?.invoke()
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier
                        .weight(1f)
                        .testTag(TestTag.SEARCH_TEXT_FIELD),
                    textStyle = textStyle.copy(color = textColor),
                    cursorBrush = SolidColor(textColor),
                    decorationBox = { innerTextField ->
                        if (query.isEmpty()) {
                            Text(
                                text = stringResource(Res.string.search_hint),
                                style = textStyle,
                                color = inactiveTextColor
                            )
                        }
                        innerTextField()
                    }
                )
                trailingIcon()
            }
            HorizontalDivider(
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
