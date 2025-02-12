package com.jefisu.authenticator.presentation.addkeymanually

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.optics.updateCopy
import com.jefisu.authenticator.domain.model.Account
import com.jefisu.authenticator.domain.model.Issuer
import com.jefisu.authenticator.domain.usecase.UseCases
import com.jefisu.authenticator.domain.util.DefaultIssuer
import com.jefisu.authenticator.domain.util.onError
import com.jefisu.authenticator.domain.util.onSuccess
import com.jefisu.authenticator.presentation.util.asUiText
import diglol.crypto.Hmac
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
        .onEach { unsavedChanges() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            _state.value
        )

    private var _account: Account? = null

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
        _state.updateCopy { AddKeyManuallyState.accountName set name }

        if (name.isNotEmpty()) {
            setIssuer(DefaultIssuer.getIssuer(name))
        } else {
            setIssuer(null)
        }
    }

    private fun setLogin(login: String) {
        _state.updateCopy { AddKeyManuallyState.login set login }
    }

    private fun setSecretKey(secretKey: String) {
        _state.updateCopy { AddKeyManuallyState.secretKey set secretKey }
    }

    private fun setIssuer(issuer: Issuer?) {
        _state.updateCopy { AddKeyManuallyState.issuer set issuer }
    }

    private fun setSearchQuery(query: String) {
        _state.updateCopy { AddKeyManuallyState.searchService set query }
    }

    private fun setAlgorithm(algorithm: Hmac.Type) {
        _state.updateCopy { AddKeyManuallyState.algorithm set algorithm }
    }

    private fun setRefreshPeriod(period: Int) {
        _state.updateCopy { AddKeyManuallyState.refreshPeriod set period }
    }

    private fun setDigits(digits: Int) {
        _state.updateCopy { AddKeyManuallyState.digits set digits }
    }

    private fun toggleExpandSettings() {
        _state.update { state ->
            AddKeyManuallyState.settingsExpanded.modify(state) { !it }
        }
    }

    private fun dismissError() {
        _state.updateCopy { AddKeyManuallyState.error set null }
    }

    private fun unsavedChanges() {
        val initialState = AddKeyManuallyState()
        val unsavedChanges = mapOf(
            _state.value.accountName to initialState.accountName,
            _state.value.secretKey to initialState.secretKey,
            _state.value.issuer to initialState.issuer,
            _state.value.algorithm to initialState.algorithm,
            _state.value.refreshPeriod to initialState.refreshPeriod,
            _state.value.digits to initialState.digits
        ).any { (new, old) ->
            new != old
        }
        _state.updateCopy { AddKeyManuallyState.unsavedChanges set unsavedChanges }
    }

    private fun save() {
        viewModelScope.launch {
            val account = Account(
                name = _state.value.accountName.ifEmpty {
                    _state.value.issuer?.identifier.orEmpty()
                },
                login = _state.value.login,
                secret = _state.value.secretKey,
                issuer = _state.value.issuer,
                refreshPeriod = _state.value.refreshPeriod,
                digitCount = _state.value.digits,
                algorithm = _state.value.algorithm
            )
            useCases.addAccount.execute(account)
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
                    _account = it
                    setAccountName(it.name)
                    setLogin(it.login)
                    setSecretKey(it.secret)
                    setIssuer(it.issuer)
                    setAlgorithm(it.algorithm)
                    setRefreshPeriod(it.refreshPeriod)
                    setDigits(it.digitCount)
                }
            }
        }
    }
}
