package com.jefisu.authenticator.presentation.addkeymanually.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jefisu.authenticator.core.presentation.components.Switch
import com.jefisu.authenticator.core.presentation.theme.colors

@Composable
fun ExpandableSettings(
    expanded: Boolean,
    onExpandToggle: () -> Unit,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colors.backgroundColor)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colors.textColor,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = expanded,
                onCheckedChange = { onExpandToggle() }
            )
        }
        Spacer(Modifier.height(8.dp))
        AnimatedContent(
            targetState = expanded,
            transitionSpec = {
                fadeIn() + expandVertically() togetherWith fadeOut() + shrinkVertically()
            }
        ) { isExpanded ->
            Column {
                Text(
                    text = description,
                    color = MaterialTheme.colors.textColor.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.fillMaxWidth()
                )
                if (isExpanded) {
                    Spacer(Modifier.height(8.dp))
                    content()
                }
            }
        }
    }
}
