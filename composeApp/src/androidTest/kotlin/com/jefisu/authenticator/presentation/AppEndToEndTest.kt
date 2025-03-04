@file:OptIn(ExperimentalTestApi::class)

package com.jefisu.authenticator.presentation

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import com.jefisu.authenticator.core.presentation.navigation.AppNavHost
import com.jefisu.authenticator.di.sharedModule
import com.jefisu.authenticator.di.testModule
import com.jefisu.authenticator.domain.model.Algorithm
import com.jefisu.authenticator.domain.repository.AccountRepository
import com.jefisu.authenticator.presentation.util.TestTag
import com.jefisu.authenticator.util.TestUtil
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test

class AppEndToEndTest : KoinTest {

    @BeforeTest
    fun setUp() {
        stopKoin()
        startKoin {
            modules(sharedModule, testModule)
        }
    }

    @Test
    fun saveNewAccount() = runComposeUiTest {
        setContent {
            AppNavHost()
        }

        val account = TestUtil.VALID_ACCOUNT

        /* Navigate to AddKeyManuallyScreen */
        onNodeWithTag(TestTag.ARC_EXPANDING_FAB).performClick()
        onNodeWithTag(TestTag.ENTER_KEY_FAB).performClick()

        /* Fill the form */
        onNodeWithTag(TestTag.NAME_TEXT_FIELD).performTextInput(account.name)
        onNodeWithTag(TestTag.LOGIN_TEXT_FIELD).performTextInput(account.login)
        onNodeWithTag(TestTag.SECRET_TEXT_FIELD).performTextInput(account.secret)

        /* Open the issuer picker, search and select Google */
        onNodeWithTag(TestTag.ISSUER_PICKER).performClick()
        onNodeWithTag(TestTag.ISSUER_TEXT_FIELD).performTextInput("Google")
        onNodeWithTag(TestTag.ISSUER_LIST)
            .onChildAt(0)
            .performClick()

        /* Expand settings and select algorithm, refresh period and digit count */
        onNodeWithTag(TestTag.EXPAND_SETTINGS_SWITCH).performClick()

        /* Select algorithm */
        onNodeWithTag(TestTag.ALGORITHM_PICKER).performClick()
        onAllNodesWithTag(TestTag.PICKER_SHEET_LIST)
            .onFirst()
            .onChildren()
            .assertCountEquals(Algorithm.entries.size)[0]
            .performClick()

        /* Select refresh period */
        onNodeWithTag(TestTag.REFRESH_PERIOD_PICKER).performClick()
        onAllNodesWithTag(TestTag.PICKER_SHEET_LIST)[1]
            .onChildren()
            .assertCountEquals(3)[1]
            .performClick()

        /* Select digit count */
        onNodeWithTag(TestTag.DIGIT_COUNT_PICKER).performClick()
        onAllNodesWithTag(TestTag.PICKER_SHEET_LIST)[1]
            .onChildren()
            .assertCountEquals(3)[1]
            .performClick()

        /* Save the account */
        onNodeWithTag(TestTag.SAVE_ACCOUNT_ICON)
            .assertHasClickAction()
            .performClick()

        /* Check that the account was saved */
        onNodeWithText(account.login).assertExists()
    }

    @Test
    fun searchAccounts() = runComposeUiTest {
        setContent {
            AppNavHost()
        }

        /* Add 10 accounts */
        runTest {
            val repository by inject<AccountRepository>()
            (1..10).forEach {
                repository.addAccount(
                    TestUtil.VALID_ACCOUNT.copy(login = "login_$it")
                )
            }
        }

        /* Open the search bar */
        onNodeWithContentDescription("Search").performClick()

        /* Search for an account */
        val query = "login_1"
        onNodeWithTag(TestTag.SEARCH_TEXT_FIELD).performTextInput(query)

        /* Check that the account was found */
        onAllNodesWithText(query)
            .assertCountEquals(2)
            .onFirst()
            .assertTextContains(query)
    }
}