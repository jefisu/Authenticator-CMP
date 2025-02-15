package com.jefisu.authenticator.domain.usecase

import com.jefisu.authenticator.domain.model.QrCodeData
import com.jefisu.authenticator.domain.model.TwoFactorAuthAccount
import com.jefisu.authenticator.domain.repository.AccountRepository
import com.jefisu.authenticator.domain.util.Error
import com.jefisu.authenticator.domain.util.Result
import com.jefisu.authenticator.domain.util.parseTotpUri
import com.jefisu.authenticator.domain.validation.QrValidationError
import com.jefisu.authenticator.domain.validation.accountRulesValidator
import com.jefisu.authenticator.platform.TotpQrScanner

class AddAccountUseCase(
    private val repository: AccountRepository,
    private val qrScanner: TotpQrScanner,
) {
    suspend fun execute(data: QrCodeData): Result<Unit, Error> {
        val totpUri = when (data) {
            is QrCodeData.TotpUri -> data.uri
            is QrCodeData.ImageBytes -> {
                val bytes = data.bytes ?: return Result.Error(QrValidationError.INVALID_QR_CODE)
                qrScanner.extractTotpUri(bytes)
            }
        } ?: return Result.Error(QrValidationError.QR_EXTRACTION_FAILED)

        val extractedAccount = parseTotpUri(totpUri)
            ?: return Result.Error(QrValidationError.INVALID_PARSE_TOTP_URI)

        val validation = accountRulesValidator.validate(extractedAccount)
        validation
            .takeIf { !it.sucessfully }
            ?.run { return Result.Error(error!!) }

        return repository.addAccount(extractedAccount)
    }


    suspend fun execute(account: TwoFactorAuthAccount): Result<Unit, Error> {
        val validationResult = accountRulesValidator.validate(account)
        validationResult
            .takeIf { !it.sucessfully }
            ?.run { return Result.Error(error!!) }

        return repository.addAccount(account)
    }
}