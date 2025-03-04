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
    fun executeWithMatchingNameReturnsMatchingAccounts() = runTest {
        val account = TestUtil.VALID_ACCOUNT.copy(name = "TestAccount")
        repository.addAccount(account)

        searchAccountsUseCase.execute("test").test {
            val emittedList = awaitItem()
            assertThat(emittedList).containsExactly(account)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun executeWithMatchingLoginReturnsMatchingAccounts() = runTest {
        val account = TestUtil.VALID_ACCOUNT.copy(login = "user@example.com")
        repository.addAccount(account)

        searchAccountsUseCase.execute("example").test {
            val emittedList = awaitItem()
            assertThat(emittedList).containsExactly(account)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun executeWithMatchingIssuerReturnsMatchingAccounts() = runTest {
        val account = TestUtil.VALID_ACCOUNT.copy(issuer = Issuer.GOOGLE)
        repository.addAccount(account)

        searchAccountsUseCase.execute("goog").test {
            val emittedList = awaitItem()
            assertThat(emittedList).containsExactly(account)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun executeWithCaseInsensitiveQueryReturnsMatchingAccounts() = runTest {
        val account = TestUtil.VALID_ACCOUNT.copy(name = "TestAccount")
        repository.addAccount(account)

        searchAccountsUseCase.execute("TEST").test {
            val emittedList = awaitItem()
            assertThat(emittedList).containsExactly(account)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun executeWithEmptyQueryReturnsEmptyList() = runTest {
        val account = TestUtil.VALID_ACCOUNT
        repository.addAccount(account)

        searchAccountsUseCase.execute("").test {
            val emittedList = awaitItem()
            assertThat(emittedList).isEmpty()
            awaitComplete()
        }
    }

    @Test
    fun executeWithNonMatchingQueryReturnsEmptyList() = runTest {
        val account = TestUtil.VALID_ACCOUNT.copy(name = "TestAccount")
        repository.addAccount(account)

        searchAccountsUseCase.execute("nomatch").test {
            val emittedList = awaitItem()
            assertThat(emittedList).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun executeWithMultipleUpdatesReturnsFilteredEmissions() = runTest {
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
    fun executeWithSpecialCharactersReturnsMatchingAccounts() = runTest {
        val account = TestUtil.VALID_ACCOUNT.copy(name = "Test@Account#1")
        repository.addAccount(account)

        searchAccountsUseCase.execute("@acc").test {
            val emittedList = awaitItem()
            assertThat(emittedList).containsExactly(account)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun executeWithNullIssuerReturnsMatchingAccounts() = runTest {
        val account = TestUtil.VALID_ACCOUNT.copy(name = "TestAccount", issuer = null)
        repository.addAccount(account)

        searchAccountsUseCase.execute("test").test {
            val emittedList = awaitItem()
            assertThat(emittedList).containsExactly(account)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun executeWithWhitespaceQueryReturnsEmptyList() = runTest {
        val account = TestUtil.VALID_ACCOUNT.copy(name = "TestAccount")
        repository.addAccount(account)

        searchAccountsUseCase.execute("   ").test {
            val emittedList = awaitItem()
            assertThat(emittedList).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }
}
