package com.jefisu.authenticator.domain.usecase

import com.jefisu.authenticator.data.FakeAccountRepository
import com.jefisu.authenticator.domain.util.Error
import com.jefisu.authenticator.util.TestUtil
import com.jefisu.authenticator.util.assertErrorResult
import com.jefisu.authenticator.util.assertSuccessResult
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class DeleteAccountUseCaseTest {

    private lateinit var deleteAccountUseCase: DeleteAccountUseCase
    private lateinit var repository: FakeAccountRepository

    @BeforeTest
    fun setup() {
        repository = FakeAccountRepository()
        deleteAccountUseCase = DeleteAccountUseCase(repository)
    }

    @Test
    fun executeWithValidAccountReturnsSuccessWhenRepositorySucceeds() = runTest {
        val result = deleteAccountUseCase.execute(TestUtil.VALID_ACCOUNT)
        assertSuccessResult(result, Unit)
    }

    @Test
    fun executeWithValidAccountReturnsUnknownErrorWhenRepositoryFails() = runTest {
        repository.forceNextOperationFailure()
        val result = deleteAccountUseCase.execute(TestUtil.VALID_ACCOUNT)
        assertErrorResult(result, Error.Unknown)
    }

    @Test
    fun executeMultipleTimesWithSameAccountReturnsConsistentResults() = runTest {
        val account = TestUtil.VALID_ACCOUNT
        repository.addAccount(account)
        val firstResult = deleteAccountUseCase.execute(account)
        assertSuccessResult(firstResult, Unit)

        repository.forceNextOperationFailure()
        val secondResult = deleteAccountUseCase.execute(account)
        assertErrorResult(secondResult, Error.Unknown)
    }
}
