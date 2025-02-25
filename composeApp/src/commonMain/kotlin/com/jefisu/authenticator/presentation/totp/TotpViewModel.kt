package com.jefisu.authenticator.presentation.totp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.optics.copy
import arrow.optics.updateCopy
import com.jefisu.authenticator.core.presentation.components.LoadingOverlayController
import com.jefisu.authenticator.domain.model.QrCodeData
import com.jefisu.authenticator.domain.model.TwoFactorAuthAccount
import com.jefisu.authenticator.domain.usecase.UseCases
import com.jefisu.authenticator.domain.util.onError
import com.jefisu.authenticator.domain.util.onSuccess
import com.jefisu.authenticator.presentation.util.asUiText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class TotpViewModel(
    private val useCases: UseCases
) : ViewModel() {

    private val _state = MutableStateFlow(TotpState())
    val state = _state
        .onStart {
            loadTotpCodes()
            searchTotpCodes()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            _state.value
        )

    private var _refreshTotpJob: Job? = null

    fun onAction(action: TotpAction) = when (action) {
        is TotpAction.QrScannedFromImage -> addAccount(action.bytes)
        is TotpAction.DeleteAccount -> deleteAccount(action.account)
        TotpAction.DismissError -> dismissError()
        TotpAction.ToggleSearch -> toggleSearch()
        is TotpAction.SearchQueryChanged -> setSearchQuery(action.query)
        else -> Unit
    }

    private fun loadTotpCodes() {
        viewModelScope.launch {
            useCases.getAllAccounts.execute().collect { accounts ->
                val currentTotpCodes = _state.value.totpCodes
                val updatedTotpCodes = accounts.mapNotNull { newAccount ->
                    val existingTotpCode = currentTotpCodes.find { it.account.id == newAccount.id }

                    if (existingTotpCode == null || existingTotpCode.account != newAccount) {
                        TotpCode(
                            account = newAccount,
                            code = useCases.generateTotp.execute(newAccount),
                            remainingTime = newAccount.refreshPeriod
                        )
                    } else {
                        null
                    }
                }

                val filteredCurrent = currentTotpCodes.filter { current ->
                    updatedTotpCodes.none { it.account.id == current.account.id }
                }
                _state.updateCopy { TotpState.totpCodes set filteredCurrent + updatedTotpCodes }

                refreshTotpCode()
            }
        }
    }

    private fun refreshTotpCode() {
        _refreshTotpJob?.cancel()
        _refreshTotpJob = viewModelScope.launch {
            flow {
                while (isActive) {
                    emit(Unit)
                    delay(1_000L)
                }
            }.collect {
                if (_state.value.totpCodes.isEmpty()) return@collect

                val updatedTotpCodes = _state.value.totpCodes.map { totpCode ->
                    if (totpCode.remainingTime > 0) {
                        return@map totpCode.copy {
                            TotpCode.remainingTime set totpCode.remainingTime - 1
                        }
                    }

                    val newCode = useCases.generateTotp.execute(totpCode.account)
                    totpCode.copy {
                        TotpCode.code set newCode
                        TotpCode.remainingTime set totpCode.account.refreshPeriod
                    }
                }
                _state.updateCopy { TotpState.totpCodes set updatedTotpCodes }
            }
        }
    }

    private fun addAccount(bytes: ByteArray?) {
        viewModelScope.launch {
            useCases.addAccount.execute(QrCodeData.ImageBytes(bytes))
                .onSuccess {
                    _state.updateCopy { TotpState.error set null }
                }
                .onError {
                    _state.updateCopy { TotpState.error set it.asUiText() }
                }
            LoadingOverlayController.hideLoading()
        }
    }

    private fun deleteAccount(account: TwoFactorAuthAccount) {
        viewModelScope.launch {
            useCases.deleteAccount.execute(account)
                .onSuccess {
                    val currentTotpCodes = _state.value.totpCodes
                        .toMutableList()
                        .apply { removeAll { it.account.id == account.id } }

                    _state.updateCopy {
                        TotpState.totpCodes set currentTotpCodes
                        TotpState.error set null
                    }
                }
                .onError {
                    _state.updateCopy { TotpState.error set it.asUiText() }
                }
        }
    }

    private fun dismissError() {
        _state.updateCopy { TotpState.error set null }
    }

    private fun toggleSearch() {
        _state.update { state ->
            state.copy { TotpState.isSearching set !state.isSearching }
        }
    }

    private fun setSearchQuery(query: String) {
        _state.updateCopy { TotpState.searchQuery set query }
    }

    private fun searchTotpCodes() {
        viewModelScope.launch {
            _state
                .flatMapLatest { state ->
                    if (!state.isSearching) _state.updateCopy { TotpState.searchQuery set "" }

                    useCases.searchAccounts
                        .execute(state.searchQuery)
                        .map { accounts ->
                            state.totpCodes.filter { totpCode ->
                                accounts.any { it.id == totpCode.account.id }
                            }
                        }
                }
                .collect { searchResults ->
                    _state.updateCopy { TotpState.searchResults set searchResults }
                }
        }
    }
}
