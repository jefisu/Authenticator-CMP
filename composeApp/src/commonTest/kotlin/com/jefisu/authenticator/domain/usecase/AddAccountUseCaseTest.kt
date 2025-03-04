@file:OptIn(ExperimentalCoroutinesApi::class)

package com.jefisu.authenticator.domain.usecase

import com.jefisu.authenticator.data.FakeAccountRepository
import com.jefisu.authenticator.data.FakeTotpQrScanner
import com.jefisu.authenticator.data.TotpUriParserImpl
import com.jefisu.authenticator.domain.model.QrCodeData
import com.jefisu.authenticator.domain.util.Error
import com.jefisu.authenticator.domain.validation.AccountValidationError
import com.jefisu.authenticator.domain.validation.NewAccountValidator
import com.jefisu.authenticator.domain.validation.QrValidationError
import com.jefisu.authenticator.util.TestUtil
import com.jefisu.authenticator.util.assertErrorResult
import com.jefisu.authenticator.util.assertSuccessResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class AddAccountUseCaseTest {

    private lateinit var addAccountUseCase: AddAccountUseCase
    private lateinit var repository: FakeAccountRepository
    private lateinit var qrScanner: FakeTotpQrScanner

    @BeforeTest
    fun setup() {
        repository = FakeAccountRepository()
        qrScanner = FakeTotpQrScanner()
        addAccountUseCase = AddAccountUseCase(
            repository = repository,
            qrScanner = qrScanner,
            rulesValidator = NewAccountValidator(),
            totpUriParser = TotpUriParserImpl()
        )
    }

    // TOTP URI Tests
    @Test
    fun executeWithValidTotpUriReturnsSuccess() = runTest {
        val result = addAccountUseCase.execute(QrCodeData.TotpUri(TestUtil.VALID_TOTP_URI))
        assertSuccessResult(result, Unit)
    }

    @Test
    fun executeWithInvalidTotpUriReturnsParsingError() = runTest {
        val result = addAccountUseCase.execute(QrCodeData.TotpUri(TestUtil.INVALID_TOTP_URI))
        assertErrorResult(result, QrValidationError.INVALID_PARSE_TOTP_URI)
    }

    @Test
    fun executeWithValidTotpUriWhenRepositoryFailsReturnsUnknownError() = runTest {
        repository.forceNextOperationFailure()
        val result = addAccountUseCase.execute(QrCodeData.TotpUri(TestUtil.VALID_TOTP_URI))
        assertErrorResult(result, Error.Unknown)
    }

    // QR Code Tests
    @Test
    fun executeWithValidQrBytesReturnsSuccess() = runTest {
        qrScanner.shouldReturn(TestUtil.VALID_TOTP_URI)
        val result = addAccountUseCase.execute(QrCodeData.ImageBytes(TestUtil.VALID_QR_BYTES))
        assertSuccessResult(result, Unit)
    }

    @Test
    fun executeWithNullQrCodeBytesReturnsInvalidQrError() = runTest {
        val result = addAccountUseCase.execute(QrCodeData.ImageBytes(bytes = null))
        assertErrorResult(result, QrValidationError.INVALID_QR_CODE)
    }

    @Test
    fun executeWithInvalidQrBytesReturnsExtractionFailedError() = runTest {
        qrScanner.shouldReturn(null)
        val result = addAccountUseCase.execute(QrCodeData.ImageBytes(TestUtil.INVALID_QR_BYTES))
        assertErrorResult(result, QrValidationError.QR_EXTRACTION_FAILED)
    }

    @Test
    fun executeWithEmptyQrBytesReturnsExtractionFailedError() = runTest {
        qrScanner.shouldReturn(null)
        val result = addAccountUseCase.execute(QrCodeData.ImageBytes(TestUtil.EMPTY_QR_BYTES))
        assertErrorResult(result, QrValidationError.QR_EXTRACTION_FAILED)
    }

    @Test
    fun executeWithExcessivelyLargeQrBytesReturnsExtractionFailedError() = runTest {
        qrScanner.shouldReturn(null)
        val largeBytes = ByteArray(10_000_000) { 0 }
        val result = addAccountUseCase.execute(QrCodeData.ImageBytes(largeBytes))
        assertErrorResult(result, QrValidationError.QR_EXTRACTION_FAILED)
    }

    @Test
    fun executeWithQrBytesExtractingInvalidUriReturnsParsingError() = runTest {
        qrScanner.shouldReturn(TestUtil.INVALID_TOTP_URI)
        val result = addAccountUseCase.execute(QrCodeData.ImageBytes(TestUtil.VALID_QR_BYTES))
        assertErrorResult(result, QrValidationError.INVALID_PARSE_TOTP_URI)
    }

    // Account Tests
    @Test
    fun executeWithValidAccountReturnsSuccess() = runTest {
        val result = addAccountUseCase.execute(TestUtil.VALID_ACCOUNT)
        assertSuccessResult(result, Unit)
    }

    @Test
    fun executeWithInvalidAccountSecretReturnsValidationError() = runTest {
        val invalidAccount = TestUtil.VALID_ACCOUNT.copy(secret = "INVALID")
        val result = addAccountUseCase.execute(invalidAccount)
        assertErrorResult(result, AccountValidationError.INVALID_SECRET)
    }

    @Test
    fun executeWithValidAccountWhenRepositoryFailsReturnsUnknownError() = runTest {
        repository.forceNextOperationFailure()
        val result = addAccountUseCase.execute(TestUtil.VALID_ACCOUNT)
        assertErrorResult(result, Error.Unknown)
    }

    @Test
    fun executeMultipleTimesWithSameValidAccountReturnsSuccessEachTime() = runTest {
        repeat(3) {
            val result = addAccountUseCase.execute(TestUtil.VALID_ACCOUNT)
            assertSuccessResult(result, Unit)
        }
    }
}
