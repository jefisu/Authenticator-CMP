@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.jefisu.authenticator.core.presentation.sharedtransition

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.compositionLocalOf

val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }
