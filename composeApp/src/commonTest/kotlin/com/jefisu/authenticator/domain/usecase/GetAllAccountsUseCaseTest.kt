package com.jefisu.authenticator.domain.usecase

import app.cash.turbine.test
import com.jefisu.authenticator.data.FakeAccountRepository
import com.jefisu.authenticator.util.TestUtil
import com.varabyte.truthish.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class GetAllAccountsUseCaseTest {

    private lateinit var getAllAccountsUseCase: GetAllAccountsUseCase
    private lateinit var repository: FakeAccountRepository

    @BeforeTest
    fun setup() {
        repository = FakeAccountRepository()
        getAllAccountsUseCase = GetAllAccountsUseCase(repository)
    }

    @Test
    fun executeReturnsFlowEmittingListOfAccountsWhenRepositoryHasData() = runTest {
        val account1 = TestUtil.VALID_ACCOUNT
        val account2 = account1.copy(secret = "DIFFERENT_SECRET")
        val accounts = listOf(account1, account2).onEach {
            repository.addAccount(it)
        }

        getAllAccountsUseCase.execute().test {
            val list = awaitItem()
            assertThat(list.size).isEqualTo(accounts.size)
            assertThat(list).containsExactly(accounts)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun executeReturnsFlowEmittingEmptyListWhenRepositoryHasNoAccounts() = runTest {
        getAllAccountsUseCase.execute().test {
            assertThat(awaitItem()).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun executeCalledMultipleTimesReturnsSameFlowInstance() = runTest {
        val account = TestUtil.VALID_ACCOUNT
        repository.addAccount(account)

        val flow1 = getAllAccountsUseCase.execute()
        val flow2 = getAllAccountsUseCase.execute()
        val result1 = flow1.first()
        val result2 = flow2.first()

        assertThat(result2).isEqualTo(result1)
        assertThat(result1.size).isEqualTo(1)
        assertThat(result1.contains(account)).isTrue()
    }
}
