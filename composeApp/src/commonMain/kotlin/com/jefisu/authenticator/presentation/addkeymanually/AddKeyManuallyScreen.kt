@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)

package com.jefisu.authenticator.presentation.addkeymanually

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import authenticator.composeapp.generated.resources.Res
import authenticator.composeapp.generated.resources.account_name
import authenticator.composeapp.generated.resources.algorithm
import authenticator.composeapp.generated.resources.change_standard_settings
import authenticator.composeapp.generated.resources.digits
import authenticator.composeapp.generated.resources.edit_account
import authenticator.composeapp.generated.resources.ic_save
import authenticator.composeapp.generated.resources.login
import authenticator.composeapp.generated.resources.new_account
import authenticator.composeapp.generated.resources.refresh_period
import authenticator.composeapp.generated.resources.seconds
import authenticator.composeapp.generated.resources.secret_key
import authenticator.composeapp.generated.resources.think_twice_before_change
import authenticator.composeapp.generated.resources.we_dont_recommend_change
import com.jefisu.authenticator.core.presentation.components.AnimatedTextFieldHint
import com.jefisu.authenticator.core.presentation.components.ShowErrorSnackbar
import com.jefisu.authenticator.core.presentation.sharedtransition.SharedTransitionKeys.FAB_EXPLODE_BOUNDS_KEY
import com.jefisu.authenticator.core.presentation.sharedtransition.sharedTransition
import com.jefisu.authenticator.core.presentation.theme.colors
import com.jefisu.authenticator.domain.model.Algorithm
import com.jefisu.authenticator.presentation.addkeymanually.components.ExpandableSettings
import com.jefisu.authenticator.presentation.addkeymanually.components.PickerSheetList
import com.jefisu.authenticator.presentation.addkeymanually.components.ServicePicker
import com.jefisu.authenticator.presentation.util.TestTag
import com.jefisu.authenticator.presentation.util.displayName
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddKeyManuallyScreen(
    onNavigateBack: () -> Unit
) {
    val viewModel = koinViewModel<AddKeyManuallyViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.state.collect { state ->
            if (state.saved) onNavigateBack()
        }
    }

    if (state.dataLoaded) {
        AddKeyManuallyScreenContent(
            state = state,
            onAction = { action ->
                if (action is AddKeyManuallyAction.NavigateBack) {
                    onNavigateBack()
                    return@AddKeyManuallyScreenContent
                }
                viewModel.onAction(action)
            }
        )
    }
}

@Composable
fun AddKeyManuallyScreenContent(
    state: AddKeyManuallyState,
    onAction: (AddKeyManuallyAction) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }

    ShowErrorSnackbar(
        error = state.error?.asString(),
        snackbarHostState = snackbarHostState,
        onDismiss = { onAction(AddKeyManuallyAction.DismissError) }
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AddKeyManuallyTopAppBar(
                isEditMode = state.isEditMode,
                hasUnsavedChanges = state.unsavedChanges,
                onNavigateBack = { onAction(AddKeyManuallyAction.NavigateBack) },
                onSave = { onAction(AddKeyManuallyAction.Save) }
            )
        },
        modifier = Modifier.sharedTransition {
            Modifier.sharedBounds(
                sharedContentState = rememberSharedContentState(key = FAB_EXPLODE_BOUNDS_KEY),
                animatedVisibilityScope = it,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            AnimatedTextFieldHint(
                text = state.account.name,
                onTextChange = { onAction(AddKeyManuallyAction.AccountNameChanged(it)) },
                hint = stringResource(Res.string.account_name),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions {
                    focusManager.moveFocus(FocusDirection.Down)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag(TestTag.NAME_TEXT_FIELD)
            )
            Spacer(Modifier.height(8.dp))
            AnimatedTextFieldHint(
                text = state.account.login,
                onTextChange = { onAction(AddKeyManuallyAction.LoginChanged(it)) },
                hint = stringResource(Res.string.login),
                keyboardActions = KeyboardActions {
                    focusManager.clearFocus()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag(TestTag.LOGIN_TEXT_FIELD)
            )
            Spacer(Modifier.height(8.dp))
            AnimatedTextFieldHint(
                text = state.account.secret,
                onTextChange = { onAction(AddKeyManuallyAction.SecretKeyChanged(it)) },
                hint = stringResource(Res.string.secret_key),
                keyboardActions = KeyboardActions {
                    focusManager.clearFocus()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag(TestTag.SECRET_TEXT_FIELD)
            )
            Spacer(Modifier.height(24.dp))
            ServicePicker(
                selectedIssuer = state.account.issuer,
                searchText = state.searchService,
                onSearchChange = { onAction(AddKeyManuallyAction.SearchQueryChanged(it)) },
                onSelectIssuer = { onAction(AddKeyManuallyAction.IssuerChanged(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag(TestTag.ISSUER_PICKER)
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            ExpandableSettingsSection(
                state = state,
                onAction = onAction
            )
        }
    }
}

@Composable
private fun AddKeyManuallyTopAppBar(
    isEditMode: Boolean,
    hasUnsavedChanges: Boolean,
    onNavigateBack: () -> Unit,
    onSave: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(
                    if (isEditMode) {
                        Res.string.edit_account
                    } else {
                        Res.string.new_account
                    }
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(
                onClick = onSave,
                enabled = hasUnsavedChanges,
                modifier = Modifier.testTag(TestTag.SAVE_ACCOUNT_ICON)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_save),
                    contentDescription = "Save account"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            navigationIconContentColor = MaterialTheme.colors.textColor,
            titleContentColor = MaterialTheme.colors.textColor,
            actionIconContentColor = MaterialTheme.colors.textColor
        )
    )
}

@Composable
private fun ExpandableSettingsSection(
    state: AddKeyManuallyState,
    onAction: (AddKeyManuallyAction) -> Unit
) {
    val timeUnit = stringResource(Res.string.seconds).lowercase()
    val digits = stringResource(Res.string.digits).lowercase()

    ExpandableSettings(
        expanded = state.settingsExpanded,
        onExpandToggle = { onAction(AddKeyManuallyAction.ToggleExpandSettings) },
        title = stringResource(Res.string.change_standard_settings),
        description = stringResource(
            if (state.settingsExpanded) {
                Res.string.think_twice_before_change
            } else {
                Res.string.we_dont_recommend_change
            }
        ),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        PickerSheetList(
            title = stringResource(Res.string.algorithm),
            items = Algorithm.entries.map { it.displayName() },
            selectedItem = state.account.algorithm.displayName(),
            onSelectItem = {
                onAction(AddKeyManuallyAction.AlgorithmChanged(Algorithm.valueOf(it)))
            },
            modifier = Modifier.testTag(TestTag.ALGORITHM_PICKER)
        )
        Spacer(Modifier.height(8.dp))
        PickerSheetList(
            title = stringResource(Res.string.refresh_period),
            items = (30..90 step 30).map { "$it $timeUnit" },
            selectedItem = "${state.account.refreshPeriod} $timeUnit",
            onSelectItem = { periodString ->
                onAction(AddKeyManuallyAction.RefreshPeriodChanged(onlyDigits(periodString)))
            },
            modifier = Modifier.testTag(TestTag.REFRESH_PERIOD_PICKER)
        )
        Spacer(Modifier.height(8.dp))
        PickerSheetList(
            title = stringResource(Res.string.digits),
            items = (3..9 step 3).map { "$it $digits" },
            selectedItem = state.account.digitCount.toString(),
            onSelectItem = { digitsString ->
                onAction(AddKeyManuallyAction.DigitsChanged(onlyDigits(digitsString)))
            },
            modifier = Modifier.testTag(TestTag.DIGIT_COUNT_PICKER)
        )
    }
}

private fun onlyDigits(text: String): Int {
    return text.filter { it.isDigit() }.toIntOrNull() ?: 0
}
