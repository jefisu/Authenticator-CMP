package com.jefisu.authenticator.domain.usecase

import com.jefisu.authenticator.data.FakeAccountRepository
import com.jefisu.authenticator.util.TestUtil
import com.jefisu.authenticator.util.assertErrorResult
import com.jefisu.authenticator.util.assertSuccessResult
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import com.jefisu.authenticator.domain.util.Error

class DeleteAccountUseCaseTest {

    private lateinit var deleteAccountUseCase: DeleteAccountUseCase
    private lateinit var repository: FakeAccountRepository

    @BeforeTest
    fun setup() {
        repository = FakeAccountRepository()
        deleteAccountUseCase = DeleteAccountUseCase(repository)
    }

    @Test
    fun `execute with valid account returns success when repository succeeds`() = runTest {
        val result = deleteAccountUseCase.execute(TestUtil.VALID_ACCOUNT)
        assertSuccessResult(result, Unit)
    }

    @Test
    fun `execute with valid account returns unknown error when repository fails`() = runTest {
        repository.forceNextOperationFailure()
        val result = deleteAccountUseCase.execute(TestUtil.VALID_ACCOUNT)
        assertErrorResult(result, Error.Unknown)
    }

    @Test
    fun `execute multiple times with same account returns consistent results`() = runTest {
        val account = TestUtil.VALID_ACCOUNT
        repository.addAccount(account)
        val firstResult = deleteAccountUseCase.execute(account)
        assertSuccessResult(firstResult, Unit)

        repository.forceNextOperationFailure()
        val secondResult = deleteAccountUseCase.execute(account)
        assertErrorResult(secondResult, Error.Unknown)
    }
}