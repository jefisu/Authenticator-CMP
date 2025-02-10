package com.jefisu.authenticator.presentation.qrscanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.optics.updateCopy
import com.jefisu.authenticator.domain.usecase.AddAccountUseCase
import com.jefisu.authenticator.domain.util.onError
import com.jefisu.authenticator.domain.util.onSuccess
import com.jefisu.authenticator.presentation.util.asUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QrScannerViewModel(
    private val addAccountUseCase: AddAccountUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(QrScannerState())
    val state = _state.asStateFlow()

    fun onAction(action: QrScannerAction) {
        when (action) {
            is QrScannerAction.OnQrCodeScanned -> processQrCode(action.content)
            is QrScannerAction.ToggleFlashlight -> toggleFlashlight()
            is QrScannerAction.DismissError -> dismissError()
            else -> Unit
        }
    }

    private fun processQrCode(totpUri: String) {
        viewModelScope.launch {
            addAccountUseCase.execute(totpUri)
                .onSuccess {
                    _state.updateCopy { QrScannerState.createdAccount set true }
                }
                .onError { error ->
                    _state.updateCopy { QrScannerState.error set error.asUiText() }
                }
        }
    }

    private fun toggleFlashlight() {
        _state.update { state -> QrScannerState.flashlightOn.modify(state) { !it } }
    }

    private fun dismissError() {
        _state.updateCopy { QrScannerState.error set null }
    }
}
