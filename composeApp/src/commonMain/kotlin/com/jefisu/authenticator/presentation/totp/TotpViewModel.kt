package com.jefisu.authenticator.presentation.totp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.optics.updateCopy
import com.jefisu.authenticator.core.util.TotpConstants.REFRESH_INTERVAL
import com.jefisu.authenticator.domain.usecase.UseCases
import com.jefisu.authenticator.domain.util.onError
import com.jefisu.authenticator.domain.util.onSuccess
import com.jefisu.authenticator.presentation.util.asUiText
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TotpViewModel(
    private val useCases: UseCases
) : ViewModel() {

    private val _state = MutableStateFlow(TotpState())
    val state = _state
        .onStart {
            loadTotpAccounts()
            refreshTotpCode()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            _state.value
        )

    fun onAction(action: TotpAction) = when (action) {
        is TotpAction.QrScannedFromImage -> addAccount(action.totpUri)
        else -> Unit
    }

    private fun loadTotpAccounts() {
        viewModelScope.launch {
            useCases.getAllAccounts.execute().collect { accounts ->
                val currentAccounts = _state.value.totpAccounts.values
                val newTotpAccounts = accounts
                    .filterNot { currentAccounts.contains(it) }
                    .associateBy { useCases.generateTotp.execute(it) }
                _state.update { state ->
                    TotpState.totpAccounts.modify(state) { it + newTotpAccounts }
                }
            }
        }
    }

    private fun generateTotpCode() {
        viewModelScope.launch {
            val newTotpAccounts = _state.value.totpAccounts.values
                .associateBy { useCases.generateTotp.execute(it) }
            _state.updateCopy { TotpState.totpAccounts set newTotpAccounts }
        }
    }

    private fun refreshTotpCode() {
        flow {
            while (true) {
                emit(Unit)
                delay(1_000L)
            }
        }.onEach {
            _state.update { state ->
                TotpState.timeUntilTotpRefresh.modify(state) { currentTime ->
                    val time = currentTime - 1
                    if (time > 0) return@modify time
                    REFRESH_INTERVAL.also { generateTotpCode() }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun addAccount(totpUri: String) {
        viewModelScope.launch {
            useCases.addAccount.execute(totpUri)
                .onSuccess {
                    _state.updateCopy { TotpState.error set null }
                }
                .onError {
                    _state.updateCopy { TotpState.error set it.asUiText() }
                }
        }
    }
}
