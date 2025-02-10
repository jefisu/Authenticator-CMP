@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.jefisu.authenticator.core.presentation.sharedtransition

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalInspectionMode

fun Modifier.sharedTransition(
    block: @Composable SharedTransitionScope.(AnimatedContentScope) -> Modifier
) = composed {
    val isPreviewMode = LocalInspectionMode.current
    if (isPreviewMode) return@composed this

    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: return@composed this

    val animatedContentScope = LocalAnimatedContentScope.current
        ?: return@composed this

    with(sharedTransitionScope) {
        block(animatedContentScope)
    }
}
