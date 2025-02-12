package com.jefisu.authenticator.presentation.addkeymanually

import com.jefisu.authenticator.domain.model.Algorithm
import com.jefisu.authenticator.domain.model.Issuer

sealed interface AddKeyManuallyAction {
    data class AccountNameChanged(val name: String) : AddKeyManuallyAction
    data class SecretKeyChanged(val secretKey: String) : AddKeyManuallyAction
    data class LoginChanged(val login: String) : AddKeyManuallyAction
    data class IssuerChanged(val issuer: Issuer) : AddKeyManuallyAction
    data class SearchQueryChanged(val query: String) : AddKeyManuallyAction
    data class AlgorithmChanged(val algorithm: Algorithm) : AddKeyManuallyAction
    data class RefreshPeriodChanged(val period: Int) : AddKeyManuallyAction
    data class DigitsChanged(val digits: Int) : AddKeyManuallyAction
    data object ToggleExpandSettings : AddKeyManuallyAction
    data object NavigateBack : AddKeyManuallyAction
    data object Save : AddKeyManuallyAction
    data object DismissError : AddKeyManuallyAction
}
