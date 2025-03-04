package com.jefisu.authenticator.domain.usecase

import app.cash.turbine.test
import com.jefisu.authenticator.data.FakeAccountRepository
import com.jefisu.authenticator.domain.model.Issuer
import com.jefisu.authenticator.util.TestUtil
import com.varabyte.truthish.assertThat
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class SearchAccountsUseCaseTest {

    private lateinit var repository: FakeAccountRepository
    private lateinit var searchAccountsUseCase: SearchAccountsUseCase

    @BeforeTest
    fun setup() {
        repository = FakeAccountRepository()
        searchAccountsUseCase = SearchAccountsUseCase(repository)
    }

    @Test
    fun `execute with matching name returns matching accounts`() = runTest {
        val account = TestUtil.VALID_ACCOUNT.copy(name = "TestAccount")
        repository.addAccount(account)

        searchAccountsUseCase.execute("test").test {
            val emittedList = awaitItem()
            assertThat(emittedList).containsExactly(account)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `execute with matching login returns matching accounts`() = runTest {
        val account = TestUtil.VALID_ACCOUNT.copy(login = "user@example.com")
        repository.addAccount(account)

        searchAccountsUseCase.execute("example").test {
            val emittedList = awaitItem()
            assertThat(emittedList).containsExactly(account)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `execute with matching issuer returns matching accounts`() = runTest {
        val account = TestUtil.VALID_ACCOUNT.copy(issuer = Issuer.GOOGLE)
        repository.addAccount(account)

        searchAccountsUseCase.execute("goog").test {
            val emittedList = awaitItem()
            assertThat(emittedList).containsExactly(account)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `execute with case-insensitive query returns matching accounts`() = runTest {
        val account = TestUtil.VALID_ACCOUNT.copy(name = "TestAccount")
        repository.addAccount(account)

        searchAccountsUseCase.execute("TEST").test {
            val emittedList = awaitItem()
            assertThat(emittedList).containsExactly(account)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `execute with empty query returns empty list`() = runTest {
        val account = TestUtil.VALID_ACCOUNT
        repository.addAccount(account)

        searchAccountsUseCase.execute("").test {
            val emittedList = awaitItem()
            assertThat(emittedList).isEmpty()
            awaitComplete()
        }
    }

    @Test
    fun `execute with non-matching query returns empty list`() = runTest {
        val account = TestUtil.VALID_ACCOUNT.copy(name = "TestAccount")
        repository.addAccount(account)

        searchAccountsUseCase.execute("nomatch").test {
            val emittedList = awaitItem()
            assertThat(emittedList).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `execute with multiple updates returns filtered emissions`() = runTest {
        val account1 = TestUtil.VALID_ACCOUNT.copy(name = "TestAccount1")
        val account2 = TestUtil.VALID_ACCOUNT.copy(name = "TestAccount2")

        searchAccountsUseCase.execute("test").test {
            val initialEmission = awaitItem()
            assertThat(initialEmission).isEmpty()

            repository.addAccount(account1)
            val firstEmission = awaitItem()
            assertThat(firstEmission).containsExactly(account1)

            repository.addAccount(account2)
            val secondEmission = awaitItem()
            assertThat(secondEmission).containsExactly(account1, account2)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `execute with special characters returns matching accounts`() = runTest {
        val account = TestUtil.VALID_ACCOUNT.copy(name = "Test@Account#1")
        repository.addAccount(account)

        searchAccountsUseCase.execute("@acc").test {
            val emittedList = awaitItem()
            assertThat(emittedList).containsExactly(account)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `execute with null issuer returns matching accounts`() = runTest {
        val account = TestUtil.VALID_ACCOUNT.copy(name = "TestAccount", issuer = null)
        repository.addAccount(account)

        searchAccountsUseCase.execute("test").test {
            val emittedList = awaitItem()
            assertThat(emittedList).containsExactly(account)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `execute with whitespace query returns empty list`() = runTest {
        val account = TestUtil.VALID_ACCOUNT.copy(name = "TestAccount")
        repository.addAccount(account)

        searchAccountsUseCase.execute("   ").test {
            val emittedList = awaitItem()
            assertThat(emittedList).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }
}