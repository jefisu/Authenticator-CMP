package com.jefisu.authenticator.presentation.addkeymanually

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.optics.updateCopy
import com.jefisu.authenticator.domain.model.Algorithm
import com.jefisu.authenticator.domain.model.Issuer
import com.jefisu.authenticator.domain.model.TwoFactorAuthAccount
import com.jefisu.authenticator.domain.model.algorithm
import com.jefisu.authenticator.domain.model.digitCount
import com.jefisu.authenticator.domain.model.issuer
import com.jefisu.authenticator.domain.model.login
import com.jefisu.authenticator.domain.model.name
import com.jefisu.authenticator.domain.model.refreshPeriod
import com.jefisu.authenticator.domain.model.secret
import com.jefisu.authenticator.domain.usecase.UseCases
import com.jefisu.authenticator.domain.util.onError
import com.jefisu.authenticator.domain.util.onSuccess
import com.jefisu.authenticator.presentation.util.asUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddKeyManuallyViewModel(
    private val useCases: UseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(AddKeyManuallyState())
    val state = _state
        .onStart { retrieveAccount() }
        .onEach { checkUnsavedChanges() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            _state.value
        )

    private var _storedAccount: TwoFactorAuthAccount? = null

    fun onAction(action: AddKeyManuallyAction) = when (action) {
        is AddKeyManuallyAction.AccountNameChanged -> setAccountName(action.name)
        is AddKeyManuallyAction.LoginChanged -> setLogin(action.login)
        is AddKeyManuallyAction.SecretKeyChanged -> setSecretKey(action.secretKey)
        is AddKeyManuallyAction.IssuerChanged -> setIssuer(action.issuer)
        is AddKeyManuallyAction.SearchQueryChanged -> setSearchQuery(action.query)
        is AddKeyManuallyAction.AlgorithmChanged -> setAlgorithm(action.algorithm)
        is AddKeyManuallyAction.RefreshPeriodChanged -> setRefreshPeriod(action.period)
        is AddKeyManuallyAction.DigitsChanged -> setDigits(action.digits)
        AddKeyManuallyAction.ToggleExpandSettings -> toggleExpandSettings()
        AddKeyManuallyAction.Save -> save()
        AddKeyManuallyAction.DismissError -> dismissError()
        else -> Unit
    }

    private fun setAccountName(name: String) {
        _state.updateCopy { AddKeyManuallyState.account.name set name }

        if (name.isNotEmpty()) {
            setIssuer(Issuer.findByIdentifier(name))
        } else {
            setIssuer(null)
        }
    }

    private fun setLogin(login: String) {
        _state.updateCopy {
            AddKeyManuallyState.account.login set login
        }
    }

    private fun setSecretKey(secretKey: String) {
        val maxLength = _state.value.account.algorithm.length
        val limitedSecretKey = secretKey.take(maxLength)
        _state.updateCopy { AddKeyManuallyState.account.secret set limitedSecretKey }
    }

    private fun setIssuer(issuer: Issuer?) {
        _state.updateCopy { AddKeyManuallyState.account.issuer set issuer }
    }

    private fun setSearchQuery(query: String) {
        _state.updateCopy { AddKeyManuallyState.searchService set query }
    }

    private fun setAlgorithm(algorithm: Algorithm) {
        _state.updateCopy { AddKeyManuallyState.account.algorithm set algorithm }

        val secretKey = _state.value.account.secret
        if (secretKey.length > algorithm.length) setSecretKey(secretKey)
    }

    private fun setRefreshPeriod(period: Int) {
        _state.updateCopy { AddKeyManuallyState.account.refreshPeriod set period }
    }

    private fun setDigits(digits: Int) {
        _state.updateCopy { AddKeyManuallyState.account.digitCount set digits }
    }

    private fun toggleExpandSettings() {
        _state.update { state ->
            AddKeyManuallyState.settingsExpanded.modify(state) { !it }
        }
    }

    private fun dismissError() {
        _state.updateCopy { AddKeyManuallyState.error set null }
    }

    private fun checkUnsavedChanges() {
        val account = _state.value.account

        val hasUnsavedChanges = _storedAccount?.run {
            val fieldComparisons = listOf(
                account.name to name,
                account.login to login,
                account.secret to secret,
                account.issuer to issuer,
                account.algorithm to algorithm,
                account.refreshPeriod to refreshPeriod,
                account.digitCount to digitCount
            )
            fieldComparisons.any { (new, old) -> new != old }
        } ?: AddKeyManuallyState().let { initialState ->
            account.login != initialState.account.login
                    && account.secret != initialState.account.secret
        }
        _state.updateCopy { AddKeyManuallyState.unsavedChanges set hasUnsavedChanges }
    }

    private fun save() {
        viewModelScope.launch {
            useCases.addAccount.execute(_state.value.account)
                .onSuccess {
                    _state.updateCopy { AddKeyManuallyState.saved set true }
                }
                .onError {
                    _state.updateCopy { AddKeyManuallyState.error set it.asUiText() }
                }
        }
    }

    private fun retrieveAccount() {
        savedStateHandle.get<Int>("id")?.let { id ->
            viewModelScope.launch {
                val account = useCases.getAllAccounts.execute()
                    .firstOrNull()
                    ?.find { it.id == id }

                account?.let {
                    _storedAccount = it
                    _state.updateCopy { AddKeyManuallyState.account set it }
                    configDiffDetected()
                }
            }
        }
        _state.updateCopy { AddKeyManuallyState.dataLoaded set true }
    }

    private fun configDiffDetected() = with(_state.value.account) {
        val defaultAccount = AddKeyManuallyState().account
        val isDifferentFromDefault = listOf(
            algorithm to defaultAccount.algorithm,
            refreshPeriod to defaultAccount.refreshPeriod,
            digitCount to defaultAccount.digitCount
        ).any { (current, default) ->
            current != default
        }
        _state.updateCopy { AddKeyManuallyState.settingsExpanded set isDifferentFromDefault }
    }

}
