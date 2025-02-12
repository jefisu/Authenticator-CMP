@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class,
    ExperimentalSharedTransitionApi::class
)

package com.jefisu.authenticator.presentation.totp

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import authenticator.composeapp.generated.resources.Res
import authenticator.composeapp.generated.resources.app_name
import authenticator.composeapp.generated.resources.ic_google_plus
import authenticator.composeapp.generated.resources.ic_pen_field
import authenticator.composeapp.generated.resources.ic_qr_code_scan
import authenticator.composeapp.generated.resources.ic_search_image
import com.jefisu.authenticator.core.presentation.sharedtransition.SharedTransitionKeys.FAB_EXPLODE_BOUNDS_KEY
import com.jefisu.authenticator.core.presentation.sharedtransition.sharedTransition
import com.jefisu.authenticator.core.presentation.theme.LightOptCodeColor
import com.jefisu.authenticator.core.presentation.theme.colors
import com.jefisu.authenticator.core.util.Platform
import com.jefisu.authenticator.core.util.getPlatform
import com.jefisu.authenticator.domain.model.Account
import com.jefisu.authenticator.presentation.totp.components.ArcExpandingFAB
import com.jefisu.authenticator.presentation.totp.components.FABItem
import com.jefisu.authenticator.presentation.totp.components.SwipeableItemWithActions
import com.jefisu.authenticator.presentation.totp.components.TotpCodeItem
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TotpScreen(
    onNavigateQrScanner: () -> Unit,
    onNavigateEnterKeyManually: (Int?) -> Unit
) {
    val viewModel = koinViewModel<TotpViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    TotpScreenContent(
        state = state,
        onAction = { action ->
            when (action) {
                TotpAction.NavigateQrScanner -> onNavigateQrScanner()
                is TotpAction.NavigateEnterKeyManually -> onNavigateEnterKeyManually(action.accountId)
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun TotpScreenContent(
    state: TotpState,
    onAction: (TotpAction) -> Unit
) {
    val clipBoardManager = LocalClipboardManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var fabIsExpanded by rememberSaveable { mutableStateOf(false) }
    val imagePicker = rememberFilePickerLauncher(
        type = FilePickerFileType.Image,
        onResult = { images ->
        }
    )
    val fabsItems = remember {
        listOf(
            FABItem(
                iconRes = Res.drawable.ic_qr_code_scan,
                onClick = { onAction(TotpAction.NavigateQrScanner) }
            ),
            FABItem(
                iconRes = Res.drawable.ic_pen_field,
                onClick = { onAction(TotpAction.NavigateEnterKeyManually(accountId = null)) }
            ),
            FABItem(
                iconRes = Res.drawable.ic_search_image,
                onClick = imagePicker::launch
            )
        )
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { TotpTopAppBar(scrollBehavior) },
        floatingActionButton = {
            TotpFloatingActionButton(
                fabIsExpanded,
                fabsItems,
                onFabToggle = { fabIsExpanded = !fabIsExpanded }
            )
        }
    ) { innerPadding ->
        TotpCodeList(
            state = state,
            scrollBehavior = scrollBehavior,
            modifier = Modifier.padding(innerPadding),
            onTotpClick = { code ->
                clipBoardManager.setText(AnnotatedString(code))
                if (getPlatform() == Platform.IOS) {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = code,
                            withDismissAction = true,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            },
            onDeleteClick = { account ->
                onAction(TotpAction.DeleteAccount(account))
            },
            onEditClick = { account ->
                onAction(TotpAction.NavigateEnterKeyManually(account.id))
            }
        )
    }
}

@Composable
private fun TotpTopAppBar(scrollBehavior: TopAppBarScrollBehavior) {
    TopAppBar(
        title = { Text(stringResource(Res.string.app_name)) },
        actions = {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = "Menu",
                    modifier = Modifier.rotate(90f)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            actionIconContentColor = MaterialTheme.colors.iconColor,
            titleContentColor = MaterialTheme.colors.textColor
        ),
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun TotpFloatingActionButton(
    isExpanded: Boolean,
    items: List<FABItem>,
    onFabToggle: () -> Unit
) {
    val rotation by animateFloatAsState(targetValue = if (isExpanded) 45f else 0f)
    ArcExpandingFAB(
        isExpanded = isExpanded,
        items = items,
        content = {
            Image(
                painter = painterResource(Res.drawable.ic_google_plus),
                contentDescription = "Google Plus",
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer { rotationZ = rotation }
            )
        },
        onExpandToggle = onFabToggle,
        modifier = Modifier
            .padding(8.dp)
            .sharedTransition {
                Modifier.sharedBounds(
                    sharedContentState = rememberSharedContentState(
                        key = FAB_EXPLODE_BOUNDS_KEY
                    ),
                    animatedVisibilityScope = it,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                )
            }
    )
}

@Composable
private fun TotpCodeList(
    state: TotpState,
    onTotpClick: (String) -> Unit,
    onDeleteClick: (Account) -> Unit,
    onEditClick: (Account) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(state.totpAccounts.toList()) { (totp, account) ->
            var isRevealed by remember { mutableStateOf(false) }

            SwipeableItemWithActions(
                isRevealed = isRevealed,
                onExpanded = { isRevealed = true },
                onCollapsed = { isRevealed = false },
                endActions = {
                    IconButton(
                        onClick = {
                            isRevealed = false
                            onEditClick(account)
                        },
                        modifier = Modifier.scale(1.2f)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "Edit",
                            tint = LightOptCodeColor
                        )
                    }
                    IconButton(
                        onClick = {
                            isRevealed = false
                            onDeleteClick(account)
                        },
                        modifier = Modifier.scale(1.2f)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red
                        )
                    }
                },
                modifier = Modifier.animateItem()
            ) {
                TotpCodeItem(
                    account = account,
                    totp = { totp },
                    remainingTime = { state.timeUntilTotpRefresh },
                    onClick = { onTotpClick(totp) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
