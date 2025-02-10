@file:OptIn(ExperimentalMaterial3Api::class)

package com.jefisu.authenticator.presentation.addkeymanually.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import authenticator.composeapp.generated.resources.Res
import authenticator.composeapp.generated.resources.ic_authentication_logo
import authenticator.composeapp.generated.resources.search_service
import authenticator.composeapp.generated.resources.service_not_selected
import coil3.compose.SubcomposeAsyncImage
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import com.jefisu.authenticator.core.presentation.components.AnimatedTextFieldHint
import com.jefisu.authenticator.core.presentation.components.BottomSheet
import com.jefisu.authenticator.core.presentation.theme.colors
import com.jefisu.authenticator.domain.model.Issuer
import com.jefisu.authenticator.domain.util.DefaultIssuer
import com.jefisu.authenticator.presentation.util.getLogoUrl
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ServicePicker(
    searchText: String,
    onSearchChange: (String) -> Unit,
    selectedIssuer: Issuer?,
    onSelectIssuer: (Issuer) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(SheetDetent.Hidden)
    val iconRotation by animateFloatAsState(
        targetValue = if (!sheetState.isIdle) 180f else 0f,
        animationSpec = tween()
    )

    LaunchedEffect(Unit) {
        snapshotFlow { sheetState.currentDetent == SheetDetent.Hidden }.collectLatest { isHidden ->
            if (isHidden) onSearchChange("")
        }
    }

    BottomSheet(
        sheetState = sheetState,
        sheetContent = {
            Column {
                AnimatedTextFieldHint(
                    text = searchText,
                    onTextChange = onSearchChange,
                    hint = stringResource(Res.string.search_service),
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .padding(horizontal = 16.dp)
                )
                ServicesGrid(
                    searchText = searchText,
                    onSelectIssuer = { service ->
                        onSelectIssuer(service)
                        sheetState.currentDetent = SheetDetent.Hidden
                    }
                )
            }
        }
    ) {
        Surface(
            onClick = { sheetState.currentDetent = SheetDetent.FullyExpanded },
            color = MaterialTheme.colors.backgroundColor,
            contentColor = MaterialTheme.colors.textColor,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colors.textColor.copy(alpha = .3f)
            ),
            modifier = modifier
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                ServiceInfo(selectedIssuer)
                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Rounded.ArrowDropDown,
                    contentDescription = "Show services",
                    modifier = Modifier
                        .graphicsLayer { rotationZ = iconRotation }
                        .scale(1.5f)
                )
            }
        }
    }
}

@Composable
private fun ServicesGrid(
    searchText: String,
    onSelectIssuer: (Issuer) -> Unit
) {
    val searchResultIssuers by remember(searchText) {
        derivedStateOf {
            DefaultIssuer.entries.filter {
                it.identifier.contains(searchText, true)
            }
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.animateContentSize()
    ) {
        items(searchResultIssuers) { issuer ->
            ServiceGridItem(
                issuer = issuer,
                onSelect = onSelectIssuer,
                modifier = Modifier.animateItem()
            )
        }
    }
}

@Composable
private fun ServiceGridItem(
    issuer: Issuer,
    onSelect: (Issuer) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = { onSelect(issuer) },
        color = MaterialTheme.colors.cardColor,
        contentColor = MaterialTheme.colors.textColor,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(
                start = 4.dp,
                end = 4.dp,
                top = 12.dp,
                bottom = 8.dp
            )
        ) {
            ServiceLogo(
                issuer = issuer,
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = issuer.identifier,
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun ServiceInfo(issuer: Issuer?) {
    AnimatedContent(
        targetState = issuer,
        transitionSpec = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right) togetherWith
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
        }
    ) { service ->
        Row(verticalAlignment = Alignment.CenterVertically) {
            ServiceLogo(
                issuer = service,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = service?.identifier ?: stringResource(Res.string.service_not_selected),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun ServiceLogo(
    issuer: Issuer?,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = issuer?.url?.let(::getLogoUrl),
        contentDescription = issuer?.identifier,
        loading = {
            CircularProgressIndicator(Modifier.scale(.6f))
        },
        error = {
            Image(
                painter = painterResource(Res.drawable.ic_authentication_logo),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        },
        modifier = modifier
    )
}
